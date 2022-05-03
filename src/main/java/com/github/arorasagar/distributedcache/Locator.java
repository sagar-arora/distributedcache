package com.github.arorasagar.distributedcache;

import java.util.ArrayList;
import java.util.List;
import com.github.arorasagar.distributedcache.cluster.ClusterMembership;

public class Locator {
    private final ClusterMembership clusterMembership;

    public Locator(Configuration configuration, ClusterMembership clusterMembership){
        this.clusterMembership = clusterMembership;
    }

    public List<ContactInformation> locateRowKey(List<Destination> destinations) {
        List<ContactInformation> contactInformation = new ArrayList<ContactInformation>();
        for (Destination destinatrion : destinations){
            contactInformation.add(new ContactInformation(destinatrion,
                    clusterMembership.findHostnameForId(destinatrion.getDestinationId())));
        }
        return contactInformation;
    }

    public Response locate(List<Destination> destinations) {
        return new Response().withProperty("payload", locateRowKey(destinations));
    }
}
