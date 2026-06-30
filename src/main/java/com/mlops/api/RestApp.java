package com.mlops.api;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import javax.ws.rs.ApplicationPath;

@ApplicationPath("/api/v1")
public class RestApp extends ResourceConfig {
    public RestApp() {
        register(JacksonFeature.class);
        
        // Resources
        register(DiscoveryResource.class);
        register(WorkspaceResource.class);
        register(ModelResource.class);
        
        // Exception Mappers
        register(WorkspaceNotEmptyMapper.class);
        register(LinkedWorkspaceNotFoundMapper.class);
        register(ModelDeprecatedMapper.class);
        register(GlobalExceptionMapper.class); // Added [cite: 162]
        
        // Logging Filters
        register(LoggingFilter.class); // Added [cite: 166]
    }
}