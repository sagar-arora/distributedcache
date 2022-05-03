package com.github.arorasagar.distributedcache.messages;

public interface Routable {
    String determineRoutingInformation();
    boolean getReRoute();
    void setReRoute(boolean reRoute);
}
