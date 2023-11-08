package com.vaccinecenter.babyvaccinationtracker.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Post {
    private String content;
    private ArrayList<String> image_url;
    private User user;
    private String created_at;

    public Post(String content, User user, String created_at, int likes) {
        this.content = content;
        this.user = user;
        this.created_at = created_at;
        this.likes = likes;
    }

    public Post(String content, User user, String created_at, ArrayList<String> hashtags, int likes) {
        this.content = content;
        this.user = user;
        this.created_at = created_at;
        this.hashtags = hashtags;
        this.likes = likes;
    }

    private ArrayList<String> hashtags;

    public Post() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = new Date();
        this.created_at = formatter.format(date);
        this.likes = 0;
    }

    public Post(String content, ArrayList<String> image_url, User user, String created_at, ArrayList<String> hashtags, int likes) {
        this.content = content;
        this.image_url = image_url;
        this.user = user;
        this.created_at = created_at;
        this.hashtags = hashtags;
        this.likes = likes;
    }

    public Post(String content, ArrayList<String> image_url, User user, String created_at, ArrayList<String> hashtags, int likes, HashMap<String, Comment> comments) {
        this.content = content;
        this.image_url = image_url;
        this.user = user;
        this.created_at = created_at;
        this.hashtags = hashtags;
        this.likes = likes;
        this.comments = comments;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<String> getImage_url() {
        return image_url;
    }

    public void setImage_url(ArrayList<String> image_url) {
        this.image_url = image_url;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public ArrayList<String> getHashtags() {
        return hashtags;
    }

    public void setHashtags(ArrayList<String> hashtags) {
        this.hashtags = hashtags;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public HashMap<String, Comment> getComments() {
        return comments;
    }

    public void setComments(HashMap<String, Comment> comments) {
        this.comments = comments;
    }

    private String post_id;
    private int likes = 0;
    private HashMap<String, Comment> comments;



}
