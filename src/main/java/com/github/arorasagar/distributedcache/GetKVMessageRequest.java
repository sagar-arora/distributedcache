package com.github.arorasagar.distributedcache;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;

@Getter
@Setter
@Builder
//@JsonTypeName("getKVMessageRequest")
@NoArgsConstructor
@AllArgsConstructor
public class GetKVMessageRequest extends KeyspaceMessage {
    private String key;
}
