package com.github.arorasagar.distributedcache.coordinator;


import com.github.arorasagar.distributedcache.Response;
import com.github.arorasagar.distributedcache.messages.BaseMessage;

import java.util.List;

public interface ResultMerger {
    Response merge(List<Response> responses, BaseMessage message);
}

