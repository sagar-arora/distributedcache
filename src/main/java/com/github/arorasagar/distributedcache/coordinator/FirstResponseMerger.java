package com.github.arorasagar.distributedcache.coordinator;

import com.github.arorasagar.distributedcache.Response;
import com.github.arorasagar.distributedcache.messages.BaseMessage;
import com.github.arorasagar.distributedcache.messages.GetKVMessageRequest;
import com.github.arorasagar.distributedcache.messages.PutKVMessageRequest;

import java.util.List;

public class FirstResponseMerger implements ResultMerger {

    @Override
    public Response merge(List<Response> responses, BaseMessage message) {
        if (message instanceof PutKVMessageRequest) {
            return new Response();
        } else if (message instanceof GetKVMessageRequest) {
            return firstResponseMerger(responses);
        }

        return new Response().withProperty("exception", "unsupported operation " + message);
    }

    private Response firstResponseMerger(List<Response> responses) {
        if (responses.size() >= 1) {
            return responses.get(0);
        }

        return new Response();
    }
}
