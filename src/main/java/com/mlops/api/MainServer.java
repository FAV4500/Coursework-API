package com.mlops.api;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import java.net.URI;

public class MainServer {
    public static final String BASE_URI = "http://localhost:8080/api/v1/";

    public static void main(String[] args) {
        try {
            final HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), new RestApp());
            System.out.println(String.format("Jersey app started at %sapi/v1\nHit Ctrl-C to stop it...", BASE_URI));
            Thread.currentThread().join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}