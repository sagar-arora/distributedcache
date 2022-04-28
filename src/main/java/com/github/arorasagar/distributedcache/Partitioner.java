package com.github.arorasagar.distributedcache;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class Partitioner {

    private static HashFunction hashFunction;
    public Partitioner() {
        hashFunction = Hashing.murmur3_32();
    }

    public Integer getPartition(String s) {
        HashCode hashCode = hashFunction.hashString(s, StandardCharsets.UTF_8);
        return hashCode.asInt();
    }
}
