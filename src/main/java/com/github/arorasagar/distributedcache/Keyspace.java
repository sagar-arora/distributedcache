package com.github.arorasagar.distributedcache;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class Keyspace {

    Map<String, Store> keyspaces = new HashMap<>();
    private static final Gson GSON = new GsonBuilder().enableComplexMapKeySerialization().create();
    private static final Logger LOGGER = LoggerFactory.getLogger(Keyspace.class);

    Configuration configuration;
    public Keyspace(Configuration configuration) {
        this.configuration = configuration;
    }

    public void addKeyspace(String name) {
        Store store = new Store(configuration);
       // LOGGER.info("Creating new Store : {}", GSON.toJson(store));
        addKeyspace(name, store);
    }

    public void addKeyspace(String name, Store store) {
        keyspaces.put(name, store);
    }

    public Store getKeyspace(String name) {
        return keyspaces.get(name);
    }
}
