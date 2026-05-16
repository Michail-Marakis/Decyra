package com.example.decyra.data.model;

public class MessageLLM {

    private String id;
    private String conversationId;
    private String role;
    private String content;
    private long timestamp;

    public MessageLLM() {}

    public MessageLLM(String id, String conversationId, String role, String content, long timestamp) {
        this.id = id;
        this.conversationId = conversationId;
        this.role = role;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getId() { return id; }
    public String getConversationId() { return conversationId; }
    public String getRole() { return role; }
    public String getContent() { return content; }
    public long getTimestamp() { return timestamp; }

    public void setId(String id) { this.id = id; }
    public void setConversationId(String conversationId) { this.conversationId = conversationId; }
    public void setRole(String role) { this.role = role; }
    public void setContent(String content) { this.content = content; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}