package com.example.decyra.data.ai.callbacks;

public interface EmbeddingCallback {
    void onSuccess(float[] embedding);
    void onError(String error);
}
