package com.azakordonets.web;

import com.azakordonets.utils.ServerProperties;
import com.azakordonets.web.entities.TokensPool;
import com.azakordonets.web.handlers.ResetPasswordHandler;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

/**
 * Created by Andrew Zakordonets
 * Date : 12/05/2015.
 */
class WebServer {

    public static void main(String[] args) throws Exception {
        ServerProperties props = new ServerProperties();
        final String url = String.format("%s%s/", props.getProperty("protocol"), props.getProperty("host"));
        URI baseUri = UriBuilder.fromUri(url).port(8181).build();
        ResourceConfig config = new ResourceConfig().packages(ResetPasswordHandler.class.getPackage().getName());
        TokensPool.getInstance();
        JdkHttpServerFactory.createHttpServer(baseUri, config);
    }

}
