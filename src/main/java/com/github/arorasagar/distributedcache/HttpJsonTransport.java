package com.github.arorasagar.distributedcache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.arorasagar.distributedcache.messages.BaseMessage;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class HttpJsonTransport {

    private static Logger LOGGER = Logger.getLogger(HttpJsonTransport.class);
    private org.eclipse.jetty.server.Server server;
    private static ObjectMapper MAPPER = new ObjectMapper();
    private final Configuration configuration;
    private final AtomicBoolean RUNNING = new AtomicBoolean(false);
    private final Coordinator coordinator;

    public HttpJsonTransport(Configuration configuration, Coordinator cordinator){
        this.configuration = configuration;
        this.coordinator = cordinator;
       // MAPPER.disable(DeserializationConfig.FAIL_ON_UNKNOWN_PROPERTIES);
     //   MAPPER.registerSubtypes(GetKVMessageRequest.class, PutKVMessageRequest.class);
    }

    public void init() {
        server = new Server();
        ServerConnector s = new ServerConnector(server);
        s.setHost(configuration.getTransportHost());
        s.setPort(configuration.getTransportPort());
        server.addConnector(s);
        server.setHandler(getHandler());
        try {
            server.start();
            RUNNING.set(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void shutdown() {
        try {
            server.stop();

            RUNNING.set(false);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private Handler getHandler(){
        AbstractHandler handler = new AbstractHandler() {
            @Override
            public void handle(String target, Request request, HttpServletRequest servletRequest,
                               HttpServletResponse response) throws IOException, ServletException {
                //String url = request.getRequestURI();

                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json;charset=utf-8");
                try {
                    BaseMessage message = MAPPER.readValue(request.getInputStream(), BaseMessage.class);
                    MAPPER.writeValue(response.getOutputStream(), coordinator.handle(message));
                } catch (Exception ex){
                    ex.printStackTrace();
                    LOGGER.debug(ex);
                    Response r = new Response();
                    r.put("exception", ex.getMessage());
                    MAPPER.writeValue(response.getOutputStream(), r);
                }
                response.getOutputStream().close();
                request.setHandled(true);

            }
        };
        return handler;
    }
}
