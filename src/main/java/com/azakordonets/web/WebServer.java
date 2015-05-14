package com.azakordonets.web;

import com.azakordonets.utils.ParseUtil;
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
public class WebServer {

    public static void main(String[] args) throws Exception {
        String url = args[0];
        int port = ParseUtil.parseInt(args[1]);

        URI baseUri = UriBuilder.fromUri(url).port(port).build();
        ResourceConfig config = new ResourceConfig().register(new ResetPasswordHandler(url, port, new TokensPool()));
        JdkHttpServerFactory.createHttpServer(baseUri, config);
    }

}
