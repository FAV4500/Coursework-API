package com.mlops.api;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EvaluationMetricResource {
    private final String modelId;

    public EvaluationMetricResource(String modelId) {
        this.modelId = modelId;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMetricsHistory() {
        List<EvaluationMetric> metricList = DataStore.metrics.get(modelId);
        if (metricList == null) {
            metricList = new ArrayList<>();
        }
        return Response.ok(metricList).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response appendMetric(EvaluationMetric metric) {
        MachineLearningModel model = DataStore.models.get(modelId);
        if (model == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if ("DEPRECATED".equalsIgnoreCase(model.getStatus())) {
            throw new ModelDeprecatedException("Model " + modelId + " is DEPRECATED and cannot accept metrics.");
        }
        metric.setId(UUID.randomUUID().toString());
        metric.setTimestamp(System.currentTimeMillis());
        DataStore.metrics.computeIfAbsent(modelId, k -> new ArrayList<>()).add(metric);
        model.setLatestAccuracy(metric.getAccuracyScore());
        return Response.status(Response.Status.CREATED).entity(metric).build();
    }
}