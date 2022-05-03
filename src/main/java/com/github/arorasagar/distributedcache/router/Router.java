package com.github.arorasagar.distributedcache.router;

import com.github.arorasagar.distributedcache.Destination;
import com.github.arorasagar.distributedcache.ServerId;
import com.github.arorasagar.distributedcache.Keyspace;
import com.github.arorasagar.distributedcache.Token;
import com.github.arorasagar.distributedcache.cluster.ClusterMembership;

import java.util.List;

public interface Router {

    /**
     * Determine which hosts a message can be sent to. (in the future keyspace should hold a node list)
     * @param local
     * @param requestKeyspace
     * @return all hosts a given request can be routed to
     */
    public List<Destination> routesTo(ServerId local, Keyspace requestKeyspace,
                                      ClusterMembership clusterMembership, Token token);

}
