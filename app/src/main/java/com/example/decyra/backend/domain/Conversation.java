package com.example.decyra.backend.domain;

/**
 * The type Conversation.
 */
public class Conversation {
    /**
     * The Id.
     */
    public String id;
    /**
     * The Last message.
     */
    public String lastMessage;
    /**
     * The Left user id.
     */
    public String leftUser_id;
    /**
     * The Right user id.
     */
    public String rightUser_id;
    /**
     * The Time last message.
     */
    public String timeLastMessage;

    /**
     * The Other name.
     */
    public String otherName;
    /**
     * The Other user id.
     */
    public String otherUserId;
    /**
     * The Other profile url.
     */
    public String otherProfileUrl;

    /**
     * Instantiates a new Conversation.
     */
    public Conversation() {
    }

    /**
     * Instantiates a new Conversation.
     *
     * @param id              the id
     * @param lastMessage     the last message
     * @param leftUser_id     the left user id
     * @param rightUser_id    the right user id
     * @param timeLastMessage the time last message
     */
    public Conversation(String id, String lastMessage, String leftUser_id, String rightUser_id, String timeLastMessage) {
        this.id = id;
        this.lastMessage = lastMessage;
        this.leftUser_id = leftUser_id;
        this.rightUser_id = rightUser_id;
        this.timeLastMessage = timeLastMessage;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets last message.
     *
     * @return the last message
     */
    public String getLastMessage() {
        return lastMessage;
    }

    /**
     * Sets last message.
     *
     * @param lastMessage the last message
     */
    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    /**
     * Gets left user id.
     *
     * @return the left user id
     */
    public String getLeftUser_id() {
        return leftUser_id;
    }

    /**
     * Sets left user id.
     *
     * @param leftUser_id the left user id
     */
    public void setLeftUser_id(String leftUser_id) {
        this.leftUser_id = leftUser_id;
    }

    /**
     * Gets right user id.
     *
     * @return the right user id
     */
    public String getRightUser_id() {
        return rightUser_id;
    }

    /**
     * Sets right user id.
     *
     * @param rightUser_id the right user id
     */
    public void setRightUser_id(String rightUser_id) {
        this.rightUser_id = rightUser_id;
    }

    /**
     * Gets time last message.
     *
     * @return the time last message
     */
    public String getTimeLastMessage() {
        return timeLastMessage;
    }

    /**
     * Sets time last message.
     *
     * @param timeLastMessage the time last message
     */
    public void setTimeLastMessage(String timeLastMessage) {
        this.timeLastMessage = timeLastMessage;
    }
}
