package com.vaccinecenter.babyvaccinationtracker.model;

import java.io.Serializable;
import java.util.Date;

public class NotificationMessage implements Serializable, Comparable<NotificationMessage> {
    private String title;
    private String message;
    private Date date;
    private String user_id;
    private String baby_id;

    private String notification_id;

    private String type;

    public String getNotification_id() {
        return notification_id;
    }

    public NotificationMessage(String title, String message, Date date, String user_id, String baby_id, String notification_id, String type) {
        this.title = title;
        this.message = message;
        this.date = date;
        this.user_id = user_id;
        this.baby_id = baby_id;
        this.notification_id = notification_id;
        this.type = type;
    }

    public void setNotification_id(String notification_id) {
        this.notification_id = notification_id;
    }

    public String getType() {
        return type;
    }

    public NotificationMessage(String title, String message, Date date, String user_id, String baby_id, String type) {
        this.title = title;
        this.message = message;
        this.date = date;
        this.user_id = user_id;
        this.baby_id = baby_id;
        this.type = type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public NotificationMessage(String title, String user_id, String baby_id, String message, Date date) {
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



    public String getBaby_id() {
        return baby_id;
    }

    public void setBaby_id(String baby_id) {
        this.baby_id = baby_id;
    }



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
    public String toString() {
        return "NotificationMessage{" +
                "title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", date=" + date +
                ", user_id='" + user_id + '\'' +
                ", baby_id='" + baby_id + '\'' +
                '}';
    }

    @Override
    public int compareTo(NotificationMessage notificationMessage) {
        return this.date.compareTo(notificationMessage.date);
    }
}

