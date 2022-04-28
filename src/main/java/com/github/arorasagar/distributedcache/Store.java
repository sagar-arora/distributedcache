package com.github.arorasagar.distributedcache;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Store {
    Map<String, String> keyVals = new HashMap<>();

    Partitioner partitioner;
    private static final Logger LOGGER = LoggerFactory.getLogger(Store.class);

    class StoreMetadata {
        Map<Integer, Node> tokenMap = new HashMap<>();

        public StoreMetadata() {
            List<Node> nodes = configuration.getNodes();
            if (nodes != null) {
                for (Node node : nodes) {
                    int loc = partitioner.getPartition(node.toString());
                    LOGGER.info("Position for node: {} is {}", node.toString(), loc);
                    tokenMap.put(loc, node);
                }
            }
        }
    }

    StoreMetadata storeMetadata;
    Configuration configuration;

    public Store(Configuration configuration) {
        this.configuration = configuration;
        this.partitioner = new Partitioner();
        this.storeMetadata = new StoreMetadata();
    }

    public void add(String key, String val) {
        this.keyVals.put(key, val);
    }
}
