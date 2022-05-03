package com.github.arorasagar.distributedcache;

import com.github.arorasagar.distributedcache.metadata.StoreMetadata;

import java.io.IOException;

public abstract class Store {
    protected final Keyspace keyspace;
    protected final StoreMetadata storeMetadata;

    public Store(Keyspace keyspace, StoreMetadata cfmd) {
        this.keyspace = keyspace;
        this.storeMetadata = cfmd;
    }

    public StoreMetadata getStoreMetadata() {
        return storeMetadata;
    }

    public Keyspace getKeyspace() {
        return keyspace;
    }

    public abstract void init() throws IOException;

    public abstract void shutdown() throws IOException;

}
