package com.github.arorasagar.distributedcache.coordinator;

import com.github.arorasagar.distributedcache.Destination;
import com.github.arorasagar.distributedcache.Response;
import com.github.arorasagar.distributedcache.client.Client;
import com.github.arorasagar.distributedcache.messages.BaseMessage;

import java.util.concurrent.Callable;

public class RemoteMessageCallable extends CompletableCallable implements Callable<Response> {

    private final Client client;
    private final Destination destination;
    private final BaseMessage message;

    public RemoteMessageCallable(Client client, BaseMessage message, Destination destination){
        this.client = client;
        this.message = message;
        this.destination = destination;
    }

    @Override
    public Response call() throws Exception {
        Response r = null;
        try {
            r = client.post(message);
            complete = true;
        } catch (RuntimeException ex){
            r = new Response();
            r.put("exception", ex.getMessage());
        }
        return r;
    }

    public Destination getDestination() {
        return destination;
    }

    public BaseMessage getMessage() {
        return message;
    }
}

