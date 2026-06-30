package com.mlops.api;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

@Provider
public class ModelDeprecatedMapper implements ExceptionMapper<ModelDeprecatedException> {
    @Override
    public Response toResponse(ModelDeprecatedException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "FORBIDDEN");
        error.put("message", ex.getMessage());
        return Response.status(Response.Status.FORBIDDEN).entity(error).build(); // 403 Forbidden 
    }
}