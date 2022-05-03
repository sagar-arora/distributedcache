package com.github.arorasagar.distributedcache.coordinator;

import com.github.arorasagar.distributedcache.Keyspace;
import com.github.arorasagar.distributedcache.KvStore;
import com.github.arorasagar.distributedcache.Response;
import com.github.arorasagar.distributedcache.Store;
import com.github.arorasagar.distributedcache.messages.BaseMessage;
import com.github.arorasagar.distributedcache.messages.GetKVMessageRequest;
import com.github.arorasagar.distributedcache.messages.PutKVMessageRequest;

public class LocalKeyValueAction extends LocalAction {
    public LocalKeyValueAction(BaseMessage message, Keyspace ks, Store cf) {
        super(message, ks, cf);
    }

    @Override
    public Response handleRequest() {
        KvStore kvStore = (KvStore) columnFamily;
            if (message instanceof GetKVMessageRequest){
                GetKVMessageRequest g = (GetKVMessageRequest) message;
                String s = kvStore.get(g.getKey());
                Response r = new Response();
                r.put("payload", s);
                return r;
            } else if ( message instanceof PutKVMessageRequest){
                PutKVMessageRequest s = (PutKVMessageRequest) message;
                kvStore.put(s.getKey(), s.getVal());
                return new Response();
            } else {
                throw new RuntimeException("Does not support this type of message");
            }
    }
}
