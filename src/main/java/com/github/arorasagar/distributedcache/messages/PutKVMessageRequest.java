package com.github.arorasagar.distributedcache.messages;

import com.github.arorasagar.distributedcache.messages.KeyspaceMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PutKVMessageRequest extends KeyValueMessage {
    String key;
    String val;

    @Override
    public String determineRoutingInformation() {
        return key;
    }
}
