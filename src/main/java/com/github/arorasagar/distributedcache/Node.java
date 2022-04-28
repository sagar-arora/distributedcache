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

    public String toString() {
        return "[address= " + address + ", port=" + port + "]";
    }
}
