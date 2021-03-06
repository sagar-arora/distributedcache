package com.github.arorasagar.distributedcache.metadata;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class StoreMetadata {

    private String name;
    private ConcurrentMap<String,Object> properties;

    public StoreMetadata(){
        properties = new ConcurrentHashMap<String,Object>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = new ConcurrentHashMap<>(properties);
    }
}
