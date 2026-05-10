package com.example.phasmatic.data.ai.callbacks;

import com.example.phasmatic.data.model.MessageLLM;

public interface MessagesCallback {
    void onSuccess(java.util.ArrayList<MessageLLM> messages);
    void onError(String error);
}