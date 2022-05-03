package com.github.arorasagar.distributedcache.coordinator;

import com.github.arorasagar.distributedcache.Keyspace;
import com.github.arorasagar.distributedcache.Response;
import com.github.arorasagar.distributedcache.Store;
import com.github.arorasagar.distributedcache.messages.BaseMessage;

public abstract class LocalAction {

    protected BaseMessage message;
    protected Keyspace keyspace;
    protected Store columnFamily;

    public LocalAction(BaseMessage message, Keyspace ks, Store cf){
        this.message = message;
        this.keyspace = ks;
        this.columnFamily = cf;
    }

    public abstract Response handleRequest();
}
