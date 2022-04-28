package com.github.arorasagar.distributedcache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Server {
    private final Coordinator coordinator;
    private final HttpJsonTransport transport;
    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);
    private final Keyspace keyspace;
    public Server(Configuration configuration) {
        this.keyspace = new Keyspace(configuration);
        this.coordinator = new Coordinator(this);
        this.transport = new HttpJsonTransport(configuration, coordinator);
    }

    public void init() {
        transport.init();
    }

    public static void main(String[] args) throws IOException {
        String configPath = args[0];
        File f = new File(configPath);
        Configuration config = Configuration.fromJsonFile(f);
        Server s = new Server(config);
        LOGGER.info("Starting the server at the port: {}", config.getTransportPort());
        s.init();
    }

    public Keyspace getKeyspace() {
        return keyspace;
    }
}
