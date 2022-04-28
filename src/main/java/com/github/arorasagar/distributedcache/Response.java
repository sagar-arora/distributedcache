package com.github.arorasagar.distributedcache;

import java.util.HashMap;

@SuppressWarnings("serial")
public class Response extends HashMap<String,Object> implements BaseResponse {

    public Response withProperty(String key, Object value){
        this.put(key, value);
        return this;
    }

}