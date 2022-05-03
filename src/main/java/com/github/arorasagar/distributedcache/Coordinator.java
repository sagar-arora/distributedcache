package com.github.arorasagar.distributedcache;

import com.github.arorasagar.distributedcache.client.Client;
import com.github.arorasagar.distributedcache.client.MetaDataClient;
import com.github.arorasagar.distributedcache.coordinator.LocalAction;
import com.github.arorasagar.distributedcache.coordinator.LocalActionCallable;
import com.github.arorasagar.distributedcache.coordinator.RemoteMessageCallable;
import com.github.arorasagar.distributedcache.coordinator.ResultMerger;
import com.github.arorasagar.distributedcache.messages.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import com.github.arorasagar.distributedcache.cluster.*;

public class Coordinator {

    Server server;
    private static final Gson GSON = new GsonBuilder().enableComplexMapKeySerialization().create();
    MetadataManager metadataManager;
    ClusterMembership clusterMembership;
    private ConcurrentMap<ClusterMember,MetaDataClient> clients = new ConcurrentHashMap<>();
    private final Configuration configuration;
    ExecutorService metaExecutor;
    private final Locator locator;
    ExecutorService executor;
    private ConcurrentMap<Destination, Client> mapping;

    public Coordinator(Server server) {
        this.server = server;
        this.configuration = server.getConfiguration();
        this.metadataManager = server.getMetadataManager();
        this.clusterMembership = server.getClusterMembership();
        this.metaExecutor = Executors.newFixedThreadPool(4);
        this.locator = new Locator(server.getConfiguration(), server.getClusterMembership());
        this.executor = Executors.newFixedThreadPool(4);
        this.mapping = new ConcurrentHashMap<>();
    }

    private Client clientForDestination(Destination destination){
        Client client = mapping.get(destination);
        if (client != null) {
            return client;
        }
        for (ClusterMember cm : clusterMembership.getLiveMembers()){
            if (cm.getId().equals(destination.getDestinationId())){
                Client cc = new Client(cm.getHost(), configuration.getTransportPort(),10000,10000);
                mapping.putIfAbsent(destination, cc);
                return cc;
            }
        }
        for (ClusterMember cm : clusterMembership.getDeadMembers()){
            if (cm.getId().equals(destination.getDestinationId())){
                Client cc = new Client(cm.getHost(), configuration.getTransportPort(),10000,10000);
                mapping.putIfAbsent(destination, cc);
                return cc;
            }
        }
        throw new RuntimeException(String.format(
                "destination %s does not exist. Live members %s. Dead members %s", destination.getDestinationId(),
                clusterMembership.getLiveMembers(), clusterMembership.getDeadMembers()));
    }

    Logger LOGGER = LoggerFactory.getLogger(Coordinator.class);

    public BaseResponse handle(BaseMessage baseMessage) {

        if (baseMessage instanceof CreateOrUpdateKeyspace) {
            LOGGER.info("Got request for CreateOrUpdateKeyspace: {}", GSON.toJson(baseMessage));
            handleCreateOrUpdateKeyspace((CreateOrUpdateKeyspace) baseMessage);
            LOGGER.info("Finished CreateOrUpdateKeyspace request");
        }
        else if (baseMessage instanceof CreateOrUpdateStore) {
            LOGGER.info("Got request for CreateOrUpdateStore: {}", GSON.toJson(baseMessage));
            handleCreateOrUpdateStore((CreateOrUpdateStore) baseMessage);
            LOGGER.info("Finished CreateOrUpdateStore request");
        }
        KeyspaceMessage keyspaceMessage = (KeyspaceMessage) baseMessage;
        String key = keyspaceMessage.getKeyspace();
        Keyspace keyspace = server.getKeyspaces().get(key);
        if (keyspace == null) {
            throw new RuntimeException("keyspace is not found " + baseMessage);
        }

        if (baseMessage instanceof LocatorMessage) {
            LOGGER.info("Got request for LocatorMessage: {}", GSON.toJson(baseMessage));
            LocatorMessage locatorMessage = (LocatorMessage) baseMessage;
            Integer tokenKey = keyspace.getKeyspaceMetaData().getPartitioner().getPartition(locatorMessage.getRow());
            Token token = new Token(String.valueOf(tokenKey), locatorMessage.getRow());
            List<Destination> destinations = destinationsForToken(token, keyspace);
            Response response = locator.locate(destinations);
            LOGGER.info("LocatorMessage resp: {}", GSON.toJson(response));
            return response;
        }
        if (baseMessage instanceof KeyValueMessage) {
            LOGGER.info("Got request for PutKVMessageRequest: {}", GSON.toJson(baseMessage));
            GetKVMessageRequest getKVMessageRequest = (GetKVMessageRequest) baseMessage;
            Integer tokenKey = keyspace.getKeyspaceMetaData().getPartitioner().getPartition(getKVMessageRequest.getKey());
            Token token = new Token(String.valueOf(tokenKey), getKVMessageRequest.getKey());
            List<Destination> destinations = destinationsForToken(token, keyspace);
        }
/*        if (baseMessage instanceof GetKVMessageRequest) {
            LOGGER.info("Got request for PutKVMessageRequest: {}", GSON.toJson(baseMessage));
            GetKVMessageRequest getKVMessageRequest = (GetKVMessageRequest) baseMessage;
            Integer tokenKey = keyspace.getKeyspaceMetaData().getPartitioner().getPartition(getKVMessageRequest.getKey());
            Token token = new Token(String.valueOf(tokenKey), getKVMessageRequest.getKey());
            List<Destination> destinations = destinationsForToken(token, keyspace);

        } else if (baseMessage instanceof PutKVMessageRequest) {
            LOGGER.info("Got request for PutKVMessageRequest: {}", GSON.toJson(baseMessage));
            PutKVMessageRequest putKVMessageRequest = (PutKVMessageRequest) baseMessage;
            Integer tokenKey = keyspace.getKeyspaceMetaData().getPartitioner().getPartition(putKVMessageRequest.getKey());
            Token token = new Token(String.valueOf(tokenKey), putKVMessageRequest.getKey());
            List<Destination> destinations = destinationsForToken(token, keyspace);

        }*/
        return null;
    }

    public MetaDataClient clientForClusterMember(ClusterMember clusterMember){
        MetaDataClient c = clients.get(clusterMember);
        if (c == null) {
            c = new MetaDataClient(clusterMember.getHost(), clusterMember
                    .getPort(), 10000, 10000);
            clients.putIfAbsent(clusterMember, c);
        }
        return c;
    }

    private Response handleCreateOrUpdateStore(final CreateOrUpdateStore message){
        metadataManager.createOrUpdateStore(message.getKeyspace(), message.getStore(), message.getProperties());
        if (message.isShouldReroute()) {
            message.setShouldReroute(false);
            List<Callable<Void>> calls = new ArrayList<>();
            for (ClusterMember clusterMember : clusterMembership.getLiveMembers()) {
                final MetaDataClient c = clientForClusterMember(clusterMember);
                Callable<Void> call = new Callable<Void>(){
                    public Void call() throws Exception {
                        c.createOrUpdateStore(
                                message.getKeyspace(), message.getStore(), message.getProperties(), false);
                        return null;
                    }};
                calls.add(call);
            }
            try {
                List<Future<Void>> res = metaExecutor.invokeAll(calls, 10, TimeUnit.SECONDS);
                //todo return results to client
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        return new Response();
    }

    private Response handleCreateOrUpdateKeyspace(final CreateOrUpdateKeyspace message) {

        metadataManager.createOrUpdateKeyspace(
                (String) message.getKeyspace(),
                (Map<String,Object>) message.getProperties() == null ? new HashMap<>() : message.getProperties());
        //TODO this is hokey
        if (message.isShouldReRoute()) {
            message.setShouldReRoute(false);
            List<Callable<Void>> calls = new ArrayList<>();
            for (ClusterMember clusterMember : clusterMembership.getLiveMembers()){
                final MetaDataClient c = clientForClusterMember(clusterMember);
                Callable<Void> call = new Callable<Void>(){
                    public Void call() throws Exception {
                        try {
                            c.createOrUpdateKeyspace(
                                    (String) message.getKeyspace(),
                                    (Map<String,Object>) message.getProperties(), false
                            );
                        } catch (RuntimeException ex){
                            ex.printStackTrace();
                        }
                        return null;
                    }};
                calls.add(call);
            }
            try {
                List<Future<Void>> res = metaExecutor.invokeAll(calls, 10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }

        return new Response();
    }

    public List<Destination> destinationsForToken(Token token, Keyspace keyspace){
        return keyspace.getKeyspaceMetaData().getRouter()
                .routesTo(server.getServerId(), keyspace, clusterMembership, token);
    }


    public Response handleMessage(Token token, final BaseMessage message, List<Destination> destinations,
                                  long timeoutInMs, Destination destinationLocal, LocalAction action, ResultMerger merger) {
        if (destinations.size() == 0){
            throw new RuntimeException("No place to route message");
        }
        if (destinations.size() == 1 && destinations.contains(destinationLocal)) {
            return action.handleReqest();
        }
        if (((Routable) message).getReRoute()) {
            return action.handleReqest();
        }
        if (!((Routable) message).getReRoute()){
            ((Routable) message).setReRoute(true);
        }
        ExecutorCompletionService<Response> completionService = new ExecutorCompletionService<>(executor);
        List<RemoteMessageCallable> remote = new ArrayList<>();
        List<Future<Response>> remoteFutures = new ArrayList<>();
        for (final Destination destination : destinations) {
            if (destination.equals(destinationLocal)) {
                completionService.submit(new LocalActionCallable(action));
            } else {
                RemoteMessageCallable r = new RemoteMessageCallable(clientForDestination(destination), message, destination);
                remote.add(r);
                remoteFutures.add(completionService.submit(r));
            }
        }
        long start = System.currentTimeMillis();
        long deadline = start + timeoutInMs;
        Response response = handleAll(start, deadline, completionService, destinations, merger, message);

        return response;
    }

    private Response handleAll(long start, long deadline,
                               ExecutorCompletionService<Response> completionService, List<Destination> destinations,
                               ResultMerger merger, BaseMessage message) {
        List<Response> responses = new ArrayList<>();
        while (start <= deadline) {
            Response r = null;
            try {
                Future<Response> future = completionService.poll(deadline - start, TimeUnit.MILLISECONDS);
                r = future.get();
                if (r != null) {
                    responses.add(r);
                }
            } catch (InterruptedException | ExecutionException e) {
                return new Response().withProperty("exception", "coordinator timeout");
            }
            if (r == null) {
                return new Response().withProperty("exception", "coordinator timeout");
            }
            if (r.containsKey("exception")) {
                return r;
            }
            if (responses.size() == destinations.size()) {
                break;
            }
            start = System.currentTimeMillis();
        }
        if (responses.size() == destinations.size()) {
            return merger.merge(responses, message);
        } else {
            return new Response().withProperty("exception", "coordinator timeout");
        }
    }
}
