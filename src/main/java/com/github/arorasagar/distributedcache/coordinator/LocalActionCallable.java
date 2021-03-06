package com.github.arorasagar.distributedcache.coordinator;

import com.github.arorasagar.distributedcache.Response;

import java.util.concurrent.Callable;

public class LocalActionCallable extends CompletableCallable implements Callable<Response> {

    private final LocalAction action;

    public LocalActionCallable(final LocalAction action) {
        this.action = action;
    }

    @Override
    public Response call() throws Exception {
        Response r = null;
        try {
            r = action.handleReqest();
        } catch (RuntimeException ex){
            r = new Response();
            r.put("exception", ex.getMessage());
            ex.printStackTrace();
        }
        return r;
    }

}
