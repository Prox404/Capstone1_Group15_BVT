package com.admin.babyvaccinationtracker.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BlackList {
    String blacklist_id;
    String user_name;
    String user_email;
    String duration;
    String reason;

    public BlackList() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.duration = dateFormat.format(currentDate);
    }

    public BlackList(String blacklist_id, String cus_name, String cus_email, String duration, String reason) {
        this.blacklist_id = blacklist_id;
        this.user_name = cus_name;
        this.user_email = cus_email;
        this.duration = duration;
        this.reason = reason;
    }

    public String getBlacklist_id() {
        return blacklist_id;
    }

    public void setBlacklist_id(String blacklist_id) {
        this.blacklist_id = blacklist_id;
    }

    public String getCus_name() {
        return user_name;
    }

    public void setCus_name(String cus_name) {
        this.user_name = cus_name;
    }

    public String getCus_email() {
        return user_email;
    }

    public void setCus_email(String cus_email) {
        this.user_email = cus_email;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
