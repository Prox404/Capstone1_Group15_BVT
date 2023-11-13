package com.admin.babyvaccinationtracker.model;

public class Admin {
    private String admin_id;
    private String admin_name;
    private String admin_email;
    private String admin_phone;
    private String admin_avatar;
    private String admin_password;

    public String getAdmin_password() {
        return admin_password;
    }

    public void setAdmin_password(String admin_password) {
        this.admin_password = admin_password;
    }

    public Admin() {
    }

    public String getAdmin_id() {
        return admin_id;
    }

    public Admin(String admin_id, String admin_name, String admin_email, String admin_phone, String admin_avatar) {
        this.admin_id = admin_id;
        this.admin_name = admin_name;
        this.admin_email = admin_email;
        this.admin_phone = admin_phone;
        this.admin_avatar = admin_avatar;
    }

    public Admin( String admin_name, String admin_email, String admin_phone, String admin_avatar) {
        this.admin_name = admin_name;
        this.admin_email = admin_email;
        this.admin_phone = admin_phone;
        this.admin_avatar = admin_avatar;
    }

    public void setAdmin_id(String admin_id) {
        this.admin_id = admin_id;
    }

    public String getAdmin_name() {
        return admin_name;
    }

    public void setAdmin_name(String admin_name) {
        this.admin_name = admin_name;
    }

    public String getAdmin_email() {
        return admin_email;
    }

    public void setAdmin_email(String admin_email) {
        this.admin_email = admin_email;
    }

    public String getAdmin_phone() {
        return admin_phone;
    }

    public void setAdmin_phone(String admin_phone) {
        this.admin_phone = admin_phone;
    }

    public String getAdmin_avatar() {
        return admin_avatar;
    }

    public void setAdmin_avatar(String admin_avatar) {
        this.admin_avatar = admin_avatar;
    }
}
