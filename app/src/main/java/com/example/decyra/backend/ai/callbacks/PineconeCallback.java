package com.example.decyra.backend.ai.callbacks;

import org.json.JSONArray;

/**
 * The interface Pinecone callback.
 */
public interface PineconeCallback {
    /**
     * On success.
     *
     * @param context the context
     */
    void onSuccess(JSONArray context);

    /**
     * On error.
     *
     * @param errorMessage the error message
     */
    void onError(String errorMessage);
}