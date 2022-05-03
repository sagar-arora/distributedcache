package com.github.arorasagar.distributedcache.cluster;

import com.github.arorasagar.distributedcache.Configuration;
import com.github.arorasagar.distributedcache.Node;
import com.github.arorasagar.distributedcache.ServerId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ConfigurationClusterMembership extends ClusterMembership {

    public static String HOSTS = "hosts";

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationClusterMembership.class);
    private List<ClusterMember> live;

    public ConfigurationClusterMembership(Configuration configuration, ServerId serverId) {
        super(configuration, serverId);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void init() {
        LOGGER.info("initializing ClusterMembership...");
        live = new ArrayList<ClusterMember>();
        List<Node> nodes = null;

        if (configuration.getNodes() != null) {
            nodes = configuration.getNodes();
        } else {
            nodes = new ArrayList<>();
        }

        for (Node node : nodes) {
            LOGGER.info("initializing cluster member {}", node.toString());
            ClusterMember cm = new ClusterMember();
            cm.setHost(node.getAddress());
            cm.setId(node.getId());
            cm.setPort(node.getPort());
            live.add(cm);
        }
    }

    @Override
    public void shutdown() {
    }

    @Override
    public List<ClusterMember> getLiveMembers() {
        return live;
    }

    @Override
    public List<ClusterMember> getDeadMembers() {
        return new ArrayList<>();
    }

}
