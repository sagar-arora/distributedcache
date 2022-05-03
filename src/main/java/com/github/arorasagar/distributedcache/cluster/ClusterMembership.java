package com.github.arorasagar.distributedcache.cluster;

import com.github.arorasagar.distributedcache.Configuration;
import com.github.arorasagar.distributedcache.ServerId;

;
import java.util.List;

public abstract class ClusterMembership {

    protected Configuration configuration;
    protected ServerId serverId;

    public ClusterMembership(Configuration configuration, ServerId serverId){
        this.configuration = configuration;
        this.serverId = serverId;
    }

    public abstract void init();
    public abstract void shutdown();
    public abstract List<ClusterMember> getLiveMembers();
    public abstract List<ClusterMember> getDeadMembers();

    public String findHostnameForId(String id) {
        if (id.equalsIgnoreCase(serverId.getU())) {
            return configuration.getTransportHost();
        }
        for (ClusterMember cm : getLiveMembers()){
            if (id.equals(cm.getId())){
                return cm.getHost();
            }
        }
        for (ClusterMember cm : getDeadMembers()){
            if (id.equals(cm.getId())){
                return cm.getHost();
            }
        }
        return null;
    }

/*    public static ClusterMembership createFrom(Configuration configuration, ServerId serverId){
        try {
            Constructor<?> cons = Class.forName(configuration.getClusterMembershipClass()).getConstructor(Configuration.class, ServerId.class);
            return (ClusterMembership) cons.newInstance(configuration, serverId);
        } catch (NoSuchMethodException | SecurityException | ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }*/
}
