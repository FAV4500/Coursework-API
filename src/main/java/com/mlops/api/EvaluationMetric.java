package com.mlops.api;

public class EvaluationMetric {
    private String id;
    private double accuracyScore;
    private double lossScore;
    private long timestamp;

    public EvaluationMetric() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public double getAccuracyScore() { return accuracyScore; }
    public void setAccuracyScore(double accuracyScore) { this.accuracyScore = accuracyScore; }

    public double getLossScore() { return lossScore; }
    public void setLossScore(double lossScore) { this.lossScore = lossScore; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}