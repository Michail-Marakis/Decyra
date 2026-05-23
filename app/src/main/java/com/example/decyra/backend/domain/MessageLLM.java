package com.example.decyra.backend.domain;

/**
 * The type Message llm.
 */
public class MessageLLM {

    private String id;
    private String conversationId;
    private String role;
    private String content;
    private long timestamp;

    /**
     * Instantiates a new Message llm.
     */
    public MessageLLM() {}

    /**
     * Instantiates a new Message llm.
     *
     * @param id             the id
     * @param conversationId the conversation id
     * @param role           the role
     * @param content        the content
     * @param timestamp      the timestamp
     */
    public MessageLLM(String id, String conversationId, String role, String content, long timestamp) {
        this.id = id;
        this.conversationId = conversationId;
        this.role = role;
        this.content = content;
        this.timestamp = timestamp;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() { return id; }

    /**
     * Gets conversation id.
     *
     * @return the conversation id
     */
    public String getConversationId() { return conversationId; }

    /**
     * Gets role.
     *
     * @return the role
     */
    public String getRole() { return role; }

    /**
     * Gets content.
     *
     * @return the content
     */
    public String getContent() { return content; }

    /**
     * Gets timestamp.
     *
     * @return the timestamp
     */
    public long getTimestamp() { return timestamp; }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) { this.id = id; }

    /**
     * Sets conversation id.
     *
     * @param conversationId the conversation id
     */
    public void setConversationId(String conversationId) { this.conversationId = conversationId; }

    /**
     * Sets role.
     *
     * @param role the role
     */
    public void setRole(String role) { this.role = role; }

    /**
     * Sets content.
     *
     * @param content the content
     */
    public void setContent(String content) { this.content = content; }

    /**
     * Sets timestamp.
     *
     * @param timestamp the timestamp
     */
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}