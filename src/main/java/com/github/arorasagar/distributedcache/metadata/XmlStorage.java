package com.github.arorasagar.distributedcache.metadata;

import com.github.arorasagar.distributedcache.Configuration;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.Map;

public class XmlStorage implements MetaDataStorage {

    public static final String SCHEMA_NAME = "schema.xml";

    @Override
    public void persist(Configuration configuration, Map<String, KeyspaceAndStoreMetadata> writeThis) {
        XMLEncoder e ;
        try {

            e = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(
                    new File(configuration.getDataDirectory(), SCHEMA_NAME))));
        } catch (FileNotFoundException ex){
            throw new RuntimeException("im dead", ex);
        }
        e.writeObject(writeThis);
        e.close();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, KeyspaceAndStoreMetadata> read(Configuration configuration) {
        XMLDecoder d;
        try {
            d = new XMLDecoder(
                    new BufferedInputStream(
                            new FileInputStream(new File(configuration.getDataDirectory(), SCHEMA_NAME))));
        } catch (FileNotFoundException e) {
            return null;
        }
        Object result = d.readObject();
        d.close();
        return (Map<String, KeyspaceAndStoreMetadata>) result;
    }

}
