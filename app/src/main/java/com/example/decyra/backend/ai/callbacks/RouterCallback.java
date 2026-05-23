package com.example.decyra.backend.ai.callbacks;

/**
 * The interface Router callback.
 */
public interface RouterCallback {
    /**
     * On result.
     *
     * @param isFollowUp the is follow up
     */
    void onResult(boolean isFollowUp);
}