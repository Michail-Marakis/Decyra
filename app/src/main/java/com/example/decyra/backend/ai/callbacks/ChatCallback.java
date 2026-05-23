package com.example.decyra.backend.ai.callbacks;

/**
 * The interface Chat callback.
 */
public interface ChatCallback {
    /**
     * On success.
     *
     * @param reply the reply
     */
    void onSuccess(String reply);

    /**
     * On error.
     *
     * @param error the error
     */
    void onError(String error);
}
