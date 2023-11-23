package com.prox.babyvaccinationtracker.model;

public class User {
    String user_id;
    String user_name;
    String user_avatar;
    String user_role;

    public User() {
    }

    public User(String user_id, String user_name, String user_avatar, String user_role) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_avatar = user_avatar;
        this.user_role = user_role;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public String getUser_role() {
        return user_role;
    }

    public void setUser_role(String user_role) {
        this.user_role = user_role;
    }
}
