package com.prox.babyvaccinationtracker.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Conversation implements java.io.Serializable{
    private String conversation_id;

    private String conversation_name;

    @Override
    public String toString() {
        return "Conversation{" +
                "conversation_id='" + conversation_id + '\'' +
                ", messages=" + messages +
                ", users=" + users +
                '}';
    }

    public String getConversation_name() {
        return conversation_name;
    }

    public void setConversation_name(String conversation_name) {
        this.conversation_name = conversation_name;
    }

    private HashMap<String, Message> messages;
    private Map<String, Boolean> users;

    public String getConversation_id() {
        return conversation_id;
    }

    public Conversation() {
    }

    public void setConversation_id(String conversation_id) {
        this.conversation_id = conversation_id;
    }

    public HashMap<String, Message> getMessages() {
        return messages;
    }

    public void setMessages(HashMap<String, Message> messages) {
        this.messages = messages;
    }

    public Map<String, Boolean> getUsers() {
        return users;
    }

    public void setUsers(Map<String, Boolean> users) {
        this.users = users;
    }


    public Conversation(HashMap<String, Message> messages, Map<String, Boolean> users) {
        this.messages = messages;
        this.users = users;
    }

    public Message getLastMessage(){
        Message lastMessage = null;
        if (messages != null && !messages.isEmpty()) {
            // Lặp qua tất cả các tin nhắn để tìm tin nhắn cuối cùng
            for (Message message : messages.values()) {
                if (lastMessage == null || message.getMess_created_at().compareTo(lastMessage.getMess_created_at()) > 0) {
                    lastMessage = message;
                }
            }
        }
        return lastMessage;
    }
}
