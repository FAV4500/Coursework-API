package com.mlops.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataStore {
    public static final Map<String, MLWorkspace> workspaces = new ConcurrentHashMap<>();
    public static final Map<String, MachineLearningModel> models = new ConcurrentHashMap<>();
    public static final Map<String, List<EvaluationMetric>> metrics = new ConcurrentHashMap<>();
}