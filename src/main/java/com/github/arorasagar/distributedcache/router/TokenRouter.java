package com.github.arorasagar.distributedcache.router;

import com.github.arorasagar.distributedcache.Destination;
import com.github.arorasagar.distributedcache.Keyspace;
import com.github.arorasagar.distributedcache.ServerId;
import com.github.arorasagar.distributedcache.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import com.github.arorasagar.distributedcache.cluster.ClusterMembership;

public class TokenRouter implements Router {

    public static final String TOKEN_MAP_KEY = "token_map";
    public static final String REPLICATION_FACTOR = "replication_factor";

    private TreeMap<String,String> getTokenMap(Keyspace requestKeyspace) {
        Map<String,String> s = (Map<String, String>) requestKeyspace
                .getKeyspaceMetaData().getProperties().get(TOKEN_MAP_KEY);
        return new TreeMap<String,String>(s);
    }

    private int getReplicationFactor(Keyspace requestKeyspace){
        Integer replicationFactor = (Integer) requestKeyspace
                .getKeyspaceMetaData().getProperties().get(REPLICATION_FACTOR);
        int rf = 1;
        if (replicationFactor != null){
            rf = replicationFactor;
        }
        return rf;
    }

    @Override
    public List<Destination> routesTo(ServerId local, Keyspace requestKeyspace,
                                      ClusterMembership clusterMembership, Token token) {
        TreeMap<String, String> tokenMap = getTokenMap(requestKeyspace);
        int rf = getReplicationFactor(requestKeyspace);
        if (rf > tokenMap.size()) {
            throw new IllegalArgumentException("Replication factor > than token map size");
        }
        List<Destination> destinations = new ArrayList<Destination>();
        Map.Entry<String,String> ceilingEntry = tokenMap.ceilingEntry(token.getToken());
        if (ceilingEntry == null){
            ceilingEntry = tokenMap.firstEntry();
        }
        destinations.add(new Destination(ceilingEntry.getValue()));
        for (int i = 1; i < rf; i++) {
            ceilingEntry = tokenMap.higherEntry(ceilingEntry.getKey());
            if (ceilingEntry == null) {
                ceilingEntry = tokenMap.firstEntry();
            }
            destinations.add(new Destination(ceilingEntry.getValue()));
        }
        return destinations;
    }
}
