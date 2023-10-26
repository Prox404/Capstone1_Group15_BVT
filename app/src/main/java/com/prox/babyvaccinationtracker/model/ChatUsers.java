package com.prox.babyvaccinationtracker.model;

import java.util.Map;

public class ChatUsers {
    private Map<String, Boolean> conversations;

    public ChatUsers() {
        // Default constructor required for Firebase
    }

    public ChatUsers(Map<String, Boolean> conversations) {
        this.conversations = conversations;
    }

    public Map<String, Boolean> getConversations() {
        return conversations;
    }

    public void setConversations(Map<String, Boolean> conversations) {
        this.conversations = conversations;
    }
}
