package com.example.decyra.backend.ai.callbacks;

/**
 * The interface Rerank callback.
 */
public interface RerankCallback {
    /**
     * On success.
     *
     * @param rankedContext the ranked context
     */
    void onSuccess(String rankedContext);
}