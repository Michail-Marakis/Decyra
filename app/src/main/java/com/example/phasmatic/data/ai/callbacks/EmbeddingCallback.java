package com.example.phasmatic.data.ai.callbacks;

public interface EmbeddingCallback {
    void onSuccess(float[] embedding);
    void onError(String error);
}
