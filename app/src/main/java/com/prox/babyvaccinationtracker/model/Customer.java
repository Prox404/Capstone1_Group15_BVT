package com.prox.babyvaccinationtracker.model;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Customer {
    private String customer_id;
    private String cus_name;
    private String cus_birthday;
    private String cus_address;
    private String cus_phone;
    private String cus_email;
    private String cus_gender;
    private String cus_ethnicity;
    private String cus_password;
    private String cus_avatar;
    private ArrayList<Baby> babies;

    public ArrayList<Baby> getBabies() {
        return babies;
    }

    public void setBabies(ArrayList<Baby> babies) {
        this.babies = babies;
    }

    public Customer(String customer_id, String cus_name, String cus_birthday, String cus_address, String cus_phone, String cus_email, String cus_gender, String cus_ethnicity, String cus_password, String cus_avatar, ArrayList<Baby> babies) {
        this.customer_id = customer_id;
        this.cus_name = cus_name;
        this.cus_birthday = cus_birthday;
        this.cus_address = cus_address;
        this.cus_phone = cus_phone;
        this.cus_email = cus_email;
        this.cus_gender = cus_gender;
        this.cus_ethnicity = cus_ethnicity;
        this.cus_password = cus_password;
        this.cus_avatar = cus_avatar;
        this.babies = babies;
    }

    public Customer(String cus_name, String cus_birthday, String cus_address, String cus_phone, String cus_email, String cus_gender, String cus_ethnicity, String cus_password, String cus_avatar) {
        this.cus_name = cus_name;
        this.cus_birthday = cus_birthday;
        this.cus_address = cus_address;
        this.cus_phone = cus_phone;
        this.cus_email = cus_email;
        this.cus_gender = cus_gender;
        this.cus_ethnicity = cus_ethnicity;
        this.cus_password = cus_password;
        this.cus_avatar = cus_avatar;
    }

    public Customer() {
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getCus_name() {
        return cus_name;
    }

    public void setCus_name(String cus_name) {
        this.cus_name = cus_name;
    }

    public String getCus_birthday() {
        return cus_birthday;
    }

    public void setCus_birthday(String cus_birthday) {
        this.cus_birthday = cus_birthday;
    }

    public String getCus_address() {
        return cus_address;
    }

    public void setCus_address(String cus_address) {
        this.cus_address = cus_address;
    }

    public String getCus_phone() {
        return cus_phone;
    }

    public void setCus_phone(String cus_phone) {
        this.cus_phone = cus_phone;
    }

    public String getCus_email() {
        return cus_email;
    }

    public void setCus_email(String cus_email) {
        this.cus_email = cus_email;
    }

    public String getCus_gender() {
        return cus_gender;
    }

    public void setCus_gender(String cus_gender) {
        this.cus_gender = cus_gender;
    }

    public String getCus_ethnicity() {
        return cus_ethnicity;
    }

    public void setCus_ethnicity(String cus_ethnicity) {
        this.cus_ethnicity = cus_ethnicity;
    }

    public String getCus_password() {
        return cus_password;
    }

    public void setCus_password(String cus_password) {
        this.cus_password = cus_password;
    }

    public String getCus_avatar() {
        return cus_avatar;
    }

    public void setCus_avatar(String cus_avatar) {
        this.cus_avatar = cus_avatar;
    }
}
