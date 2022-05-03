package com.github.arorasagar.distributedcache.messages;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetKVMessageRequest extends KeyValueMessage {
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String determineRoutingInformation() {
        return key;
    }
}
