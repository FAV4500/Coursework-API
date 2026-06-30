package com.mlops.api;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Path("/workspaces")
public class WorkspaceResource {

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllWorkspaces() {
        Collection<MLWorkspace> workspaceList = DataStore.workspaces.values();
        return Response.ok(new ArrayList<>(workspaceList)).build();
    }

    @GET
    @Path("/{workspaceId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWorkspaceById(@PathParam("workspaceId") String workspaceId) {
        MLWorkspace workspace = DataStore.workspaces.get(workspaceId);
        if (workspace == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(workspace).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createWorkspace(MLWorkspace workspace) {
        if (workspace.getId() == null || workspace.getId().trim().isEmpty()) {
            workspace.setId("WS-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        DataStore.workspaces.put(workspace.getId(), workspace);
        URI location = uriInfo.getAbsolutePathBuilder().path(workspace.getId()).build();
        return Response.created(location).entity(workspace).build();
    }

    @DELETE
    @Path("/{workspaceId}")
    public Response deleteWorkspace(@PathParam("workspaceId") String workspaceId) {
        MLWorkspace workspace = DataStore.workspaces.get(workspaceId);
        if (workspace == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (workspace.getModelIds() != null && !workspace.getModelIds().isEmpty()) {
            throw new WorkspaceNotEmptyException("Cannot delete workspace: " + workspaceId + " still has models assigned.");
        }
        DataStore.workspaces.remove(workspaceId);
        return Response.noContent().build();
    }
}