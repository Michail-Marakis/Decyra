package com.example.decyra.backend.domain;

/**
 * The type Message.
 */
public class Message {
    /**
     * The Id.
     */
    public String id;
    /**
     * The Conversation id.
     */
    public String conversationId;
    /**
     * The Sender id.
     */
    public String sender_id;
    /**
     * The Receiver id.
     */
    public String receiver_id;
    /**
     * The Message text.
     */
    public String messageText;
    /**
     * The Time message.
     */
    public String timeMessage;
    /**
     * The Status of message.
     */
    public String statusOfMessage;

    /**
     * Instantiates a new Message.
     */
    public Message() {
    }

    /**
     * Instantiates a new Message.
     *
     * @param id              the id
     * @param conversationId  the conversation id
     * @param sender_id       the sender id
     * @param receiver_id     the receiver id
     * @param messageText     the message text
     * @param timeMessage     the time message
     * @param statusOfMessage the status of message
     */
    public Message(String id, String conversationId, String sender_id, String receiver_id, String messageText, String timeMessage, String statusOfMessage) {
        this.id = id;
        this.conversationId = conversationId;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.messageText = messageText;
        this.timeMessage = timeMessage;
        this.statusOfMessage = statusOfMessage;
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
     * Gets conversation id.
     *
     * @return the conversation id
     */
    public String getConversationId() {
        return conversationId;
    }

    /**
     * Sets conversation id.
     *
     * @param conversationId the conversation id
     */
    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    /**
     * Gets sender id.
     *
     * @return the sender id
     */
    public String getSender_id() {
        return sender_id;
    }

    /**
     * Sets sender id.
     *
     * @param sender_id the sender id
     */
    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    /**
     * Gets receiver id.
     *
     * @return the receiver id
     */
    public String getReceiver_id() {
        return receiver_id;
    }

    /**
     * Sets receiver id.
     *
     * @param receiver_id the receiver id
     */
    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    /**
     * Gets message text.
     *
     * @return the message text
     */
    public String getMessageText() {
        return messageText;
    }

    /**
     * Sets message text.
     *
     * @param messageText the message text
     */
    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    /**
     * Gets time message.
     *
     * @return the time message
     */
    public String getTimeMessage() {
        return timeMessage;
    }

    /**
     * Sets time message.
     *
     * @param timeMessage the time message
     */
    public void setTimeMessage(String timeMessage) {
        this.timeMessage = timeMessage;
    }

    /**
     * Gets status of message.
     *
     * @return the status of message
     */
    public String getStatusOfMessage() {
        return statusOfMessage;
    }

    /**
     * Sets status of message.
     *
     * @param statusOfMessage the status of message
     */
    public void setStatusOfMessage(String statusOfMessage) {
        this.statusOfMessage = statusOfMessage;
    }
}
