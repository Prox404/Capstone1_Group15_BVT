package com.prox.babyvaccinationtracker.model;

public class Vaccination_Registration {
    Baby baby_name;
    Customer cus_name;
    String cus_phone;
    Vaccines vaccine_name;
    Vaccine_center center_name;
    String regist_created_at;
    boolean status;
    String post_vaccination;

    public Vaccination_Registration(Baby baby_name, Customer cus_name, String cus_phone, Vaccines vaccine_name, Vaccine_center center_name, String regist_created_at, boolean status, String post_vaccination) {
        this.baby_name = baby_name;
        this.cus_name = cus_name;
        this.cus_phone = cus_phone;
        this.vaccine_name = vaccine_name;
        this.center_name = center_name;
        this.regist_created_at = regist_created_at;
        this.status = status;
        this.post_vaccination = post_vaccination;
    }

    public Baby getBaby_name() {
        return baby_name;
    }

    public Customer getCus_name() {
        return cus_name;
    }

    public String getCus_phone() {
        return cus_phone;
    }

    public Vaccines getVaccine_name() {
        return vaccine_name;
    }

    public Vaccine_center getCenter_name() {
        return center_name;
    }

    public String getRegist_created_at() {
        return regist_created_at;
    }

    public boolean isStatus() {
        return status;
    }

    public String getPost_vaccination() {
        return post_vaccination;
    }

    public void setBaby_name(Baby baby_name) {
        this.baby_name = baby_name;
    }

    public void setCus_name(Customer cus_name) {
        this.cus_name = cus_name;
    }

    public void setCus_phone(String cus_phone) {
        this.cus_phone = cus_phone;
    }

    public void setVaccine_name(Vaccines vaccine_name) {
        this.vaccine_name = vaccine_name;
    }

    public void setCenter_name(Vaccine_center center_name) {
        this.center_name = center_name;
    }

    public void setRegist_created_at(String regist_created_at) {
        this.regist_created_at = regist_created_at;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setPost_vaccination(String post_vaccination) {
        this.post_vaccination = post_vaccination;
    }
}
