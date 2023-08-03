package com.bing.testautomation.support.utils.testcontext;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.Set;

public class ScenarioContext {

    private final Multimap<String, Object> scenarioContext = ArrayListMultimap.create();

    public void setContext(Context key, Object value) {
        scenarioContext.put(key.toString(), value);
    }

    public void removeContext(Context key, Object value) {
        scenarioContext.remove(key.toString(), value);
    }

    public Object getContext(Context key){
        Set<String> keys = scenarioContext.keySet();

        if (!keys.contains(key.toString()))
            throw new RuntimeException("Could not find scenarioContext '" + key + "'.");

        return scenarioContext.get(key.toString()).size() == 1
                ? scenarioContext.get(key.toString()).iterator().next()
                : scenarioContext.get(key.toString());
    }

    public Boolean isContains(Context key){
        return scenarioContext.containsKey(key.toString());
    }

    public void clearScenarioContext() {
        scenarioContext.clear();
    }
}
