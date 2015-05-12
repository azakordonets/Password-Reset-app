package com.azakordonets.web;

import com.azakordonets.web.handlers.ResetPasswordHandler;
import com.sun.net.httpserver.HttpServer;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

/**
 * Created by Andrew Zakordonets
 * Date : 12/05/2015.
 */
public class WebServer {

    public static void main(String[] args) throws Exception {
        URI baseUri = UriBuilder.fromUri("http://localhost/").port(8181).build();
        ResourceConfig config = new ResourceConfig().packages(ResetPasswordHandler.class.getPackage().getName());
        HttpServer server = JdkHttpServerFactory.createHttpServer(baseUri, config);
    }

}
