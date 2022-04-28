package com.github.arorasagar.distributedcache.router;

import com.github.arorasagar.distributedcache.Destination;
import com.github.arorasagar.distributedcache.Keyspace;
import com.github.arorasagar.distributedcache.ServerId;
import com.github.arorasagar.distributedcache.Token;

import java.util.List;

public class TokenRouter implements Router {

    @Override
    public List<Destination> routesTo(ServerId local, Keyspace requestKeyspace, Token token) {
        return null;
    }
}
