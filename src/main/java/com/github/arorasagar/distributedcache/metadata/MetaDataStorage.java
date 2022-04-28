package com.github.arorasagar.distributedcache.metadata;

import com.github.arorasagar.distributedcache.Configuration;

import java.util.Map;

public interface MetaDataStorage {
    void persist(Configuration configuration, Map<String,KeyspaceAndStoreMetadata> writeThis);
    Map<String,KeyspaceAndStoreMetadata> read(Configuration configuration);
}
