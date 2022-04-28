package com.github.arorasagar.distributedcache;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Coordinator {

    Server server;
    Keyspace keyspace;
    private static final Gson GSON = new GsonBuilder().enableComplexMapKeySerialization().create();

    public Coordinator(Server server) {
        this.server = server;
        this.keyspace = server.getKeyspace();
    }

    Logger LOGGER = LoggerFactory.getLogger(Coordinator.class);

    public BaseResponse handle(BaseMessage baseMessage) {


        KeyspaceMessage keyspaceMessage = (KeyspaceMessage) baseMessage;
        String key = keyspaceMessage.getKeyspace();

        if (baseMessage instanceof CreateKeyspace) {
            LOGGER.info("Got request for CreateKeyspace: {}", GSON.toJson(baseMessage));
            this.keyspace.addKeyspace(((CreateKeyspace) baseMessage).getKeyspace());
            LOGGER.info("Finished creating keyspace");

        } else if (baseMessage instanceof GetKVMessageRequest) {
            LOGGER.info("Got request for PutKVMessageRequest: {}", baseMessage);
        } else if (baseMessage instanceof PutKVMessageRequest) {
            LOGGER.info("Got request for PutKVMessageRequest: {}", baseMessage);
        }
        return null;
    }
}
