package com.example.decyra.backend.ai.callbacks;

import com.example.decyra.backend.domain.MessageLLM;

/**
 * The interface Messages callback.
 */
public interface MessagesCallback {
    /**
     * On success.
     *
     * @param messages the messages
     */
    void onSuccess(java.util.ArrayList<MessageLLM> messages);

    /**
     * On error.
     *
     * @param error the error
     */
    void onError(String error);
}