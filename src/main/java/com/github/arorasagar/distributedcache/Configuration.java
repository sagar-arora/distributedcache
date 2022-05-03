package com.github.arorasagar.distributedcache;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.arorasagar.distributedcache.metadata.MetaDataStorage;
import com.github.arorasagar.distributedcache.metadata.XmlStorage;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Builder
public class Configuration {

    private int transportPort = 7070;
    private String transportHost = "127.0.0.1";
    private String metaDataStorageClass = XmlStorage.class.getName();
    private List<Node> nodes = new ArrayList<>();
    private String dataDirectory;
    private boolean localMode = true;
    private String id;
    public Configuration() {
    }

    public boolean isLocalMode() {
        return localMode;
    }

    public String getId() {
        return this.id;
    }

    public void setDataDirectory(String dataDirectory) {
        this.dataDirectory = dataDirectory;
    }

    public String getDataDirectory() {
        return dataDirectory;
    }

    public int getTransportPort() {
        return transportPort;
    }

    public void setTransportPort(int transportPort) {
        this.transportPort = transportPort;
    }

    public String getTransportHost() {
        return transportHost;
    }

    public void setTransportHost(String transportHost) {
        this.transportHost = transportHost;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes() {
        this.nodes = nodes;
    }

    public static Configuration fromJsonFile(File jsonFile) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Configuration
                configuration = objectMapper.readValue(jsonFile, Configuration.class);

       return configuration;
    }

    public String getMetaDataStorageClass() {
        return metaDataStorageClass;
    }

}
