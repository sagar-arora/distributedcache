package com.github.arorasagar.distributedcache;

import lombok.*;

@Getter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@NoArgsConstructor
public class Node {
    private  String address;
    private  int port;
    private String id;
    public String toString() {
        return address + ":" + port;
    }
}
