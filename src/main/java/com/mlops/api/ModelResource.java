package com.mlops.api;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Path("/models")
public class ModelResource {

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getModels(@QueryParam("status") String status) {
        List<MachineLearningModel> allModels = new ArrayList<>(DataStore.models.values());
        if (status != null && !status.trim().isEmpty()) {
            allModels = allModels.stream()
                    .filter(m -> m.getStatus().equalsIgnoreCase(status.trim()))
                    .collect(Collectors.toList());
        }
        return Response.ok(allModels).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerModel(MachineLearningModel model) {
        if (model.getWorkspaceId() == null || !DataStore.workspaces.containsKey(model.getWorkspaceId())) {
            throw new LinkedWorkspaceNotFoundException("Workspace ID not found: " + model.getWorkspaceId());
        }
        model.setId("MOD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        DataStore.models.put(model.getId(), model);

        MLWorkspace workspace = DataStore.workspaces.get(model.getWorkspaceId());
        if (workspace.getModelIds() == null) {
            workspace.setModelIds(new ArrayList<>());
        }
        workspace.getModelIds().add(model.getId());

        URI location = uriInfo.getAbsolutePathBuilder().path(model.getId()).build();
        return Response.created(location).entity(model).build();
    }

    @Path("/{modelId}/metrics")
    public EvaluationMetricResource getEvaluationMetricResource(@PathParam("modelId") String modelId) {
        return new EvaluationMetricResource(modelId);
    }
}