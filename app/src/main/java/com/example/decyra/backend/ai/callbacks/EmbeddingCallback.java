package com.example.decyra.backend.ai.callbacks;

/**
 * The interface Embedding callback.
 */
public interface EmbeddingCallback {
    /**
     * On success.
     *
     * @param embedding the embedding
     */
    void onSuccess(float[] embedding);

    /**
     * On error.
     *
     * @param error the error
     */
    void onError(String error);
}
