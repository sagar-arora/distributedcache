package com.github.arorasagar.distributedcache.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.arorasagar.distributedcache.BaseMessage;
import com.github.arorasagar.distributedcache.BaseResponse;
import com.github.arorasagar.distributedcache.Response;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Client {

    protected ObjectMapper MAPPER = new ObjectMapper();

    private DefaultHttpClient client = new DefaultHttpClient();
    private ClientConnectionManager mgr;

    private static final int connectionTimeoutInMillis = 10000;
    private static final int socketTimeoutInMillis = 10000;

    private final String host;
    private final int port;
    private final int connectionTimeoutMillis;
    private final int socketTimeoutMillis;


    @SuppressWarnings("deprecation")
    public Client(String host, int port, int connectionTimeoutMillis, int socketTimeoutMillis) {
        this.host = host;
        this.port = port;
        this.connectionTimeoutMillis = connectionTimeoutMillis;
        this.socketTimeoutMillis = socketTimeoutMillis;
        client = new DefaultHttpClient();
        mgr = client.getConnectionManager();
        HttpParams params = client.getParams();
        HttpConnectionParams.setConnectionTimeout(params, connectionTimeoutMillis);
        HttpConnectionParams.setSoTimeout(params, socketTimeoutMillis);
        client = new DefaultHttpClient(new ThreadSafeClientConnManager(params,
                mgr.getSchemeRegistry()), params);
    }

    public Response post(BaseMessage request)
            throws IOException, IllegalStateException, UnsupportedEncodingException, RuntimeException {
        HttpPost postRequest = new HttpPost("http://" + host + ":" + port);
        ByteArrayEntity input = new ByteArrayEntity(MAPPER.writeValueAsBytes(request));
        input.setContentType("application/json");
        postRequest.setEntity(input);
        HttpResponse response = client.execute(postRequest);
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatusLine().getStatusCode());
        }
        Response r = MAPPER.readValue(response.getEntity().getContent(), Response.class);
        response.getEntity().getContent().close();
        return r;
    }

    public BaseResponse post(BaseMessage request, TypeReference tr)
            throws IOException, IllegalStateException, UnsupportedEncodingException, RuntimeException {
        HttpPost postRequest = new HttpPost("http://" + host + ":" + port);
        ByteArrayEntity input = new ByteArrayEntity(MAPPER.writeValueAsBytes(request));
        input.setContentType("application/json");
        postRequest.setEntity(input);
        HttpResponse response = client.execute(postRequest);
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatusLine().getStatusCode());
        }
        BaseResponse r = (BaseResponse) MAPPER.readValue(response.getEntity().getContent(), tr);
        response.getEntity().getContent().close();
        return r;
    }


    public void shutdown() {
        mgr.shutdown();
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public int getConnectionTimeoutMillis() {
        return connectionTimeoutMillis;
    }

    public int getSocketTimeoutMillis() {
        return socketTimeoutMillis;
    }

}