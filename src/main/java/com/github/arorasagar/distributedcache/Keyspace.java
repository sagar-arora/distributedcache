package com.github.arorasagar.distributedcache;

import com.github.arorasagar.distributedcache.metadata.KeyspaceMetadata;

import java.util.HashMap;
import java.util.Map;

public class Keyspace {

    Map<String, Store> stores = new HashMap<>();
    Configuration configuration;
    KeyspaceMetadata keyspaceMetadata;

    public Keyspace(Configuration configuration) {
        this.configuration = configuration;
    }

    public void addKeyspace(String name) {
        Store store = new Store(configuration);
        addKeyspace(name, store);
    }

    public void addKeyspace(String name, Store store) {
        stores.put(name, store);
    }

    public Store getKeyspace(String name) {
        return stores.get(name);
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
