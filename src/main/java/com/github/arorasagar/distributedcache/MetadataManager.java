package com.github.arorasagar.distributedcache;

import com.github.arorasagar.distributedcache.metadata.*;
import com.github.arorasagar.distributedcache.router.TokenRouter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class MetadataManager {

    public static final String SYSTEM_KEYSPACE = "system";
    private MetaDataStorage metaDataStorage;
    private final Server server;
    private final Configuration configuration;
    private static final Logger LOGGER = LoggerFactory.getLogger(MetadataManager.class);

    public MetadataManager(Configuration configuration,
                           Server server) {
        this.configuration = configuration;
        this.server = server;
        this.metaDataStorage = new XmlStorage();
    }

    public void init() {
        createKeyspaces();
    }

    private void addSystemKeyspace(){
        Keyspace system = new Keyspace(configuration);
        KeyspaceMetadata ksmd = new KeyspaceMetadata();
        ksmd.setName(SYSTEM_KEYSPACE);
        ksmd.setPartitioner( new Partitioner());
        ksmd.setRouter(new TokenRouter());
        system.setKeyspaceMetadata(ksmd);
        server.getKeyspaces().put(SYSTEM_KEYSPACE, system);
    }

    private void createKeyspaces() {
        Map<String, KeyspaceAndStoreMetadata> meta = read();
        addSystemKeyspace();
        if (!(meta == null)) {
            for (Map.Entry<String, KeyspaceAndStoreMetadata> keyspaceEntry : meta.entrySet()){
                Keyspace k = new Keyspace(configuration);
                k.setKeyspaceMetadata(keyspaceEntry.getValue().getKeyspaceMetaData());
                for (Map.Entry<String, StoreMetadata> columnFamilyEntry : keyspaceEntry.getValue().getColumnFamilies().entrySet()){
                    KvStore columnFamily = new KvStore(k, columnFamilyEntry.getValue());
                    try {
                        columnFamily.init();
                    } catch (Exception e) {
                        System.out.println("Exception occurred");
                    }
                    k.getStores().put(columnFamilyEntry.getKey(), columnFamily);
                }
                server.getKeyspaces().put(keyspaceEntry.getKey(), k);
            }
        }
    }

    public void createOrUpdateKeyspace(String keyspaceName, Map<String,Object> properties){
        LOGGER.info("updating or creating the keyspace: {}", keyspaceName);
        KeyspaceMetadata kmd = new KeyspaceMetadata(keyspaceName, properties);
        Keyspace keyspace = new Keyspace(configuration);
        Map<String, String> tokenMap = createTokenMap(keyspaceName);
        properties.put(TokenRouter.TOKEN_MAP_KEY, tokenMap);
        keyspace.setKeyspaceMetadata(kmd);
        kmd.setProperties(properties);
        Keyspace result = server.getKeyspaces().putIfAbsent(keyspaceName, keyspace);
        if (result != null) {
            result.getKeyspaceMetaData().setProperties(properties);
        }
        persistMetadata();
    }

    private void persistMetadata(){
        Map<String,KeyspaceAndStoreMetadata> meta = new HashMap<>();
        for (Map.Entry<String, Keyspace> entry : server.getKeyspaces().entrySet()){
            KeyspaceAndStoreMetadata kfmd = new KeyspaceAndStoreMetadata();
            kfmd.setKeyspaceMetaData(entry.getValue().getKeyspaceMetaData());
            for (Map.Entry<String, KvStore> cfEntry : entry.getValue().getStores().entrySet()){
                kfmd.getColumnFamilies().put(cfEntry.getKey(), cfEntry.getValue().getStoreMetadata());
            }
            meta.put(entry.getKey(), kfmd);
        }
        persist(meta);
    }

    Map<String, String> createTokenMap(String keyspace) {
        Partitioner partitioner = new Partitioner();
        Map<String, String> tokenMap = new HashMap<>();
            List<Node> nodes = configuration.getNodes();
            nodes.add(Node.builder()
                    .address(configuration.getTransportHost())
                            .port(configuration.getTransportPort())
                            .id(configuration.getId())
                    .build());

                for (Node node : nodes) {
                    int loc = partitioner.getPartition(node.toString());
                    LOGGER.info("Position for node: {} is {}", node.toString(), loc);
                    tokenMap.put(String.valueOf(loc), node.toString());
                }
            return tokenMap;
    }

    public void createOrUpdateStore(String keyspaceName, String store, Map<String,Object> properties){
        //server.getKeyspaces().get(keyspaceName).createStore(store, properties);
        Store existingKvStore = server.getKeyspaces().get(keyspaceName).getStores().get(store);
        if (existingKvStore == null) {
            server.getKeyspaces().get(keyspaceName).createStore(store, properties);
        } else {
            //TODO thread safe?
            existingKvStore.getStoreMetadata().setProperties(properties);
        }
        persistMetadata();
    }

    public Collection<String> listKeyspaces() {
        return server.getKeyspaces().keySet();
    }

    public Collection<String> listStores(String keyspace){
        Keyspace ks = server.getKeyspaces().get(keyspace);
        return ks.getStores().keySet();
    }

    public KeyspaceMetadata getKeyspaceMetadata(String keyspace){
        return server.getKeyspaces().get(keyspace).getKeyspaceMetaData();
    }

    public StoreMetadata getStoreMetadata(String keyspace, String store) {
        return server.getKeyspaces().get(keyspace).getStores().get(store).getStoreMetadata();
    }

    public Map<String,KeyspaceAndStoreMetadata> read(){
        return metaDataStorage.read(configuration);
    }

    public void persist(Map<String,KeyspaceAndStoreMetadata> meta){
        metaDataStorage.persist(configuration, meta);
    }
}
