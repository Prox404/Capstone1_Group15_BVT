package com.prox.babyvaccinationtracker.model;

import java.io.Serializable;
import java.util.Date;

public class NotificationMessage implements Serializable, Comparable<NotificationMessage> {
    private String title;
    private String message;
    private Date date;

    public NotificationMessage(String title,String user_id,  String baby_id, String message, Date date) {
        this.title = title;
        this.message = message;
        this.date = date;
        this.user_id = user_id;
        this.baby_id = baby_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    private String user_id;

    public String getBaby_id() {
        return baby_id;
    }

    public void setBaby_id(String baby_id) {
        this.baby_id = baby_id;
    }

    private String baby_id;

    public NotificationMessage(String title, String message, Date date) {
        this.title = title;
        this.message = message;
        this.date = date;
    }

    public NotificationMessage(String title, String user_id, String message, Date date) {
        this.title = title;
        this.message = message;
        this.date = date;
        this.user_id = user_id;
    }

    public NotificationMessage() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int compareTo(NotificationMessage notificationMessage) {
        return this.date.compareTo(notificationMessage.date);
    }
}
