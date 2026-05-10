package com.example.phasmatic.data.ai.callbacks;

import org.json.JSONArray;

public interface PineconeCallback {
    void onSuccess(JSONArray context);
    void onError(String errorMessage);
}