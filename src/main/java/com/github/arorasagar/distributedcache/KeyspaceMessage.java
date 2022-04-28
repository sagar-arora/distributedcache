package com.github.arorasagar.distributedcache;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class KeyspaceMessage extends BaseMessage {
    public String keyspace;
}
