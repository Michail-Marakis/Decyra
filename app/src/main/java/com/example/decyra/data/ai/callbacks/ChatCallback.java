package com.example.phasmatic.data.ai.callbacks;

public interface ChatCallback {
    void onSuccess(String reply);
    void onError(String error);
}
