package com.github.arorasagar.distributedcache.metadata;

import com.github.arorasagar.distributedcache.Partitioner;
import com.github.arorasagar.distributedcache.router.Router;
import com.github.arorasagar.distributedcache.router.TokenRouter;

import java.util.Map;

public class KeyspaceMetadata {
    private String name;
    private Map<String, Object> properties;
    private transient Partitioner partitioner;
    private transient Router router;

    //serialization
    public KeyspaceMetadata() {

    }

    public KeyspaceMetadata(String name, Map<String, Object> properties) {
        this.name = name;
        setProperties(properties);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Partitioner getPartitioner() {
        return partitioner;
    }

    public void setPartitioner(Partitioner partitioner) {
        this.partitioner = partitioner;
    }

    public Router getRouter() {
        return router;
    }

    public void setRouter(Router router) {
        this.router = router;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
        partitioner = new Partitioner();
        router = new TokenRouter();
    }

    @Override
    public String toString() {
        return "KeyspaceMetaData [name=" + name + ", properties=" + properties + "]";
    }
}