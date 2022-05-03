package com.github.arorasagar.distributedcache.messages;

public class LocatorMessage extends KeyspaceMessage {

    private String row;

    public LocatorMessage(){

    }

    public String getKeyspace() {
        return keyspace;
    }

    public void setKeyspace(String keyspace) {
        this.keyspace = keyspace;
    }

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

}
