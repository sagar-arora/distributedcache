package com.github.arorasagar.distributedcache;

import com.github.arorasagar.distributedcache.metadata.KeyspaceMetadata;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Keyspace {

    ConcurrentHashMap<String, KvStore> stores = new ConcurrentHashMap<>();
    Configuration configuration;
    KeyspaceMetadata keyspaceMetadata;

    public Keyspace(Configuration configuration) {
        this.configuration = configuration;
    }

    public void addKeyspace(String name, KvStore kvStore) {
        stores.put(name, kvStore);
    }

    public ConcurrentHashMap<String, KvStore> getStores() {
        return stores;
    }

    public KeyspaceMetadata getKeyspaceMetaData() {
        return keyspaceMetadata;
    }

    public void createStore(String name, Map<String, Object> properties) {

    }

    public void setKeyspaceMetadata(KeyspaceMetadata keyspaceMetadata) {
        this.keyspaceMetadata = keyspaceMetadata;
    }
}
