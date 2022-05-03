package com.github.arorasagar.distributedcache.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.arorasagar.distributedcache.ContactInformation;
import com.github.arorasagar.distributedcache.messages.CreateOrUpdateStore;
import com.github.arorasagar.distributedcache.Response;
import com.github.arorasagar.distributedcache.messages.CreateOrUpdateKeyspace;
import com.github.arorasagar.distributedcache.messages.LocatorMessage;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MetaDataClient extends Client {

    public MetaDataClient(String host, int port, int c, int s) {
        super(host, port, c, s);
    }


    public void createOrUpdateKeyspace(String keyspace, Map<String,Object> properties, boolean isClient) throws ClientException {
        CreateOrUpdateKeyspace k = new CreateOrUpdateKeyspace();
        k.setKeyspace(keyspace);
        if(isClient){
            k.setShouldReRoute(true);
        }
        k.setProperties(properties);
        try {
            Response response = post(k);
        } catch (IOException | RuntimeException e) {
            throw new ClientException(e);
        }
    }

    public void createOrUpdateStore(String keyspace, String store,  Map<String,Object> properties, boolean isClient) throws ClientException {
        CreateOrUpdateStore m = new CreateOrUpdateStore();
        m.setKeyspace(keyspace);
        m.setStore(store);
        m.setProperties(properties);
        if (isClient){
            m.setShouldReroute(true);
        }
        try {
            Response response = post(m);
        } catch (IOException | RuntimeException e) {
            throw new ClientException(e);
        }
    }

/*    public Collection<String> listKeyspaces() throws ClientException {
        ListKeyspaces m = new ListKeyspaces();
        try {
            Response response = post(m);
            return (Collection<String>) response.get("payload");
        } catch (IOException | RuntimeException e) {
            throw new ClientException(e);
        }
    }*/


    public List<ContactInformation> getLocationForRowKey(String keyspace, String store, String rowkey) throws ClientException{
        LocatorMessage m = new LocatorMessage();
        m.setKeyspace(keyspace);
        m.setRow(rowkey);
        TypeReference<List<ContactInformation>> tf = new TypeReference<List<ContactInformation>>() {};
        try {
            Response response = post(m);
            ObjectMapper om = new ObjectMapper();
            return (List<ContactInformation>) om.convertValue(response.get("payload"), tf);
        } catch (IOException | RuntimeException e) {
            throw new ClientException(e);
        }
    }
}
