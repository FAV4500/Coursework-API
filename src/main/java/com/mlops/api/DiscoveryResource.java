package com.mlops.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/")
public class DiscoveryResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDiscoveryInfo() {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("version", "1.0.0");
        metadata.put("adminContact", "favour.philips@westminster.ac.uk");
        
        Map<String, String> collections = new HashMap<>();
        collections.put("workspaces", "/api/v1/workspaces");
        collections.put("models", "/api/v1/models");
        metadata.put("collections", collections);

        return Response.ok(metadata).build();
    }
}