package com.github.arorasagar.distributedcache;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")

@JsonSubTypes({
        //metadata
        @JsonSubTypes.Type(value = com.github.arorasagar.distributedcache.GetKVMessageRequest.class, name = "GetKVMessageRequest"),
        @JsonSubTypes.Type(value = com.github.arorasagar.distributedcache.PutKVMessageRequest.class, name = "PutKVMessageRequest"),
        @JsonSubTypes.Type(value = CreateKeyspace.class, name = "CreateKeyspace"),
})
public class BaseMessage {
}
