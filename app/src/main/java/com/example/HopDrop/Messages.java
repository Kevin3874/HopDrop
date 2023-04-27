package com.example.HopDrop;

public class Messages {
    private String content;
    private String recipientUserId;

    public Messages() {
        // Default constructor required for calls to DataSnapshot.getValue(Message.class)
    }

    public Messages(String content, String recipientUserId) {
        this.content = content;
        this.recipientUserId = recipientUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRecipientUserId() {
        return recipientUserId;
    }

    public void setRecipientUserId(String recipientUserId) {
        this.recipientUserId = recipientUserId;
    }
}

