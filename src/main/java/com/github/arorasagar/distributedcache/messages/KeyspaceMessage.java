package com.github.arorasagar.distributedcache.messages;

import com.github.arorasagar.distributedcache.messages.BaseMessage;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class KeyspaceMessage extends BaseMessage {
    public String keyspace;
}
