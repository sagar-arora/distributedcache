package com.github.arorasagar.distributedcache;

import com.fasterxml.jackson.annotation.JsonTypeName;

public class PutKVMessageRequest extends KeyspaceMessage {
    String key;
    String val;
}
