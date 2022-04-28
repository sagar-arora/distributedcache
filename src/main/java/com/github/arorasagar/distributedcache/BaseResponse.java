package com.github.arorasagar.distributedcache;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")

@JsonSubTypes({
        //rpc
        @JsonSubTypes.Type(value = Response.class, name = "Response"),
})
public interface BaseResponse {
}
