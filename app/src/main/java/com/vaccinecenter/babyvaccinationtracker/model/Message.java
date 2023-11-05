package com.vaccinecenter.babyvaccinationtracker.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements java.io.Serializable, Comparable<Message> {
    private String message_id;
    private String user_name;
    private String mess_content;
    private String mess_created_at;

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getMess_content() {
        return mess_content;
    }

    public void setMess_content(String mess_content) {
        this.mess_content = mess_content;
    }

    public String getMess_created_at() {
        return mess_created_at;
    }

    public void setMess_created_at(String mess_created_at) {
        this.mess_created_at = mess_created_at;
    }



    public Message(String user_name, String mess_content) {
        this.user_name = user_name;
        this.mess_content = mess_content;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        this.mess_created_at = formatter.format(date);
    }

    public Message() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        this.mess_created_at = formatter.format(date);
    }

    @Override
    public String toString() {
        return "Message{" +
                "user_name='" + user_name + '\'' +
                ", mess_content='" + mess_content + '\'' +
                ", mess_created_at='" + mess_created_at + '\'' +
                '}';
    }

    @Override
    public int compareTo(Message message) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date(message.getMess_created_at());
        Date date2 = new Date(this.getMess_created_at());
        return date2.compareTo(date);
    }
}
