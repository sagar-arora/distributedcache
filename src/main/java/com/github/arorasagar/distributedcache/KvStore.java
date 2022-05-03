package com.github.arorasagar.distributedcache;


import com.github.arorasagar.distributedcache.metadata.StoreMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class KvStore extends Store {
    Map<String, String> keyVals = new HashMap<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(KvStore.class);


/*
    StoreMetadata storeMetadata;
    Configuration configuration;

    public KvStore(Configuration configuration) {
        this.configuration = configuration;
        this.partitioner = new Partitioner();
        this.storeMetadata = new StoreMetadata();
    }*/

    public KvStore(Keyspace keyspace, StoreMetadata storeMetadata) {
        super(keyspace, storeMetadata);
    }

    @Override
    public void init() throws IOException {

    }

    @Override
    public void shutdown() throws IOException {

    }

    public void put(String key, String val) {
        this.keyVals.put(key, val);
    }

    public String get(String key) {
        return this.keyVals.get(key);
    }
}
