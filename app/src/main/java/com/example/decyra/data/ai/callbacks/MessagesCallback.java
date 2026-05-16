package com.example.decyra.data.ai.callbacks;

import com.example.decyra.data.model.MessageLLM;

public interface MessagesCallback {
    void onSuccess(java.util.ArrayList<MessageLLM> messages);
    void onError(String error);
}