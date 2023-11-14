package com.vaccinecenter.babyvaccinationtracker.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Comment {
    private String content;
    private User user;
    private String created_at;
    private String comment_id;

    @Override
    public String toString() {
        return "Comment{" +
                "content='" + content + '\'' +
                ", user=" + user +
                ", created_at='" + created_at + '\'' +
                ", comment_id='" + comment_id + '\'' +
                ", replies=" + replies +
                '}';
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    HashMap<String, Comment> replies;

    public void setContent(String content) {
        this.content = content;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public HashMap<String, Comment> getReplies() {
        return replies;
    }

    public void setReplies(HashMap<String, Comment> replies) {
        this.replies = replies;
    }

    public Comment(String content, User user, String created_at) {
        this.content = content;
        this.user = user;
        this.created_at = created_at;
    }

    public Comment(String content, User user, String created_at, HashMap<String, Comment> replies) {
        this.content = content;
        this.user = user;
        this.created_at = created_at;
        this.replies = replies;
    }

    public Comment() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = new Date();
        this.created_at = formatter.format(date);
    }

    public String getContent() {
        return content;
    }

    public User getUser() {
        return user;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getComment_id() {
        return comment_id;
    }

}
