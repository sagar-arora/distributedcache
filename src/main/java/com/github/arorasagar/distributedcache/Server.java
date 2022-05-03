package com.github.arorasagar.distributedcache;

import com.github.arorasagar.distributedcache.cluster.ConfigurationClusterMembership;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import com.github.arorasagar.distributedcache.cluster.ClusterMembership;

public class Server {
    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    private final Coordinator coordinator;
    private final HttpJsonTransport transport;
    private final ConcurrentMap<String,Keyspace> keyspaces;
    private final Configuration configuration;
    private final MetadataManager metadataManager;
    private final ServerId serverId;
    private final ClusterMembership clusterMembership;

    public Server(Configuration configuration) {
        this.configuration = configuration;
        this.metadataManager = new MetadataManager(configuration, this);
        this.serverId = new ServerId(configuration);
        this.clusterMembership = new ConfigurationClusterMembership(configuration, serverId);
        this.coordinator = new Coordinator(this);
        this.transport = new HttpJsonTransport(configuration, coordinator);
        this.keyspaces = new ConcurrentHashMap<>();
    }

    public void init() {
        transport.init();
        serverId.init();
        metadataManager.init();
        clusterMembership.init();
    }

    public MetadataManager getMetadataManager() {
        return metadataManager;
    }

    public ClusterMembership getClusterMembership() {
        return this.clusterMembership;
    }

    public static void main(String[] args) throws IOException {
        String configPath = args[0];
        File f = new File(configPath);
        Configuration config = Configuration.fromJsonFile(f);
        Server s = new Server(config);
        if (config != null) {
            config.setDataDirectory(config.getDataDirectory() + "\\" + config.getTransportPort());
        }
        LOGGER.info("Starting the server at the port: {}", config.getTransportPort());
        s.init();
    }

    public ConcurrentMap<String, Keyspace> getKeyspaces() {
        return keyspaces;
    }

    public void put(String keyspace, String columnFamily, String rowkey, String column, String value, long time){
        Keyspace ks = keyspaces.get(keyspace);
        KvStore cf = ks.getStores().get(columnFamily);
    }

    public void put(String keyspace, String columnFamily, String rowkey, String column, String value, long time, long ttl){
        Keyspace ks = keyspaces.get(keyspace);
        KvStore cf = ks.getStores().get(columnFamily);
    }


    public Configuration getConfiguration() {
        return configuration;
    }

    public ServerId getServerId() {
        return serverId;
    }
}
