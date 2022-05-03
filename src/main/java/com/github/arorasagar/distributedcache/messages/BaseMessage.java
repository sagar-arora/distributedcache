package com.github.arorasagar.distributedcache.messages;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")

@JsonSubTypes({
        //metadata
        @JsonSubTypes.Type(value = GetKVMessageRequest.class, name = "GetKVMessageRequest"),
        @JsonSubTypes.Type(value = PutKVMessageRequest.class, name = "PutKVMessageRequest"),
        @JsonSubTypes.Type(value = CreateOrUpdateStore.class, name = "CreateOrUpdateStore"),
        @JsonSubTypes.Type(value = CreateOrUpdateKeyspace.class, name = "CreateOrUpdateKeyspace"),
        @JsonSubTypes.Type(value = LocatorMessage.class, name = "LocatorMessage"),
})
public class BaseMessage {
}
