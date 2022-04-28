package com.github.arorasagar.distributedcache.metadata;

import java.util.HashMap;
import java.util.Map;

public class KeyspaceAndStoreMetadata {
    private KeyspaceMetadata keyspaceMetaData;
    private Map<String,StoreMetadata> columnFamilies;

    public KeyspaceAndStoreMetadata(){
        columnFamilies = new HashMap<>();
    }

    public KeyspaceMetadata getKeyspaceMetaData() {
        return keyspaceMetaData;
    }

    public void setKeyspaceMetaData(KeyspaceMetadata keyspaceMetaData) {
        this.keyspaceMetaData = keyspaceMetaData;
    }

    public Map<String, StoreMetadata> getColumnFamilies() {
        return columnFamilies;
    }

    public void setColumnFamilies(Map<String, StoreMetadata> columnFamilies) {
        this.columnFamilies = columnFamilies;
    }

    @Override
    public String toString() {
        return "KeyspaceAndColumnFamilyMetaData [keyspaceMetaData=" + keyspaceMetaData
                + ", columnFamilies=" + columnFamilies + "]";
    }
}
