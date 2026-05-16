package com.example.decyra.data.ai.callbacks;

public interface ChatCallback {
    void onSuccess(String reply);
    void onError(String error);
}
