package com.prox.babyvaccinationtracker.model;

public class Vaccination_Registration {
    Baby baby;
    Customer cus;
    String cus_phone;
    Vaccines vaccine;
    Vaccine_center center;
    String regist_created_at;
    int status;
    String post_vaccination;
    public  Vaccination_Registration(){}

    public Vaccination_Registration(Baby baby, Customer cus, String cus_phone, Vaccines vaccine, Vaccine_center center, String regist_created_at, int status, String post_vaccination) {
        this.baby = baby;
        this.cus = cus;
        this.cus_phone = cus_phone;
        this.vaccine = vaccine;
        this.center = center;
        this.regist_created_at = regist_created_at;
        this.status = status;
        this.post_vaccination = post_vaccination;
    }

    public Baby getBaby() {
        return baby;
    }

    public void setBaby(Baby baby) {
        this.baby = baby;
    }

    public Customer getCus() {
        return cus;
    }

    public void setCus(Customer cus) {
        this.cus = cus;
    }

    public String getCus_phone() {
        return cus_phone;
    }

    public void setCus_phone(String cus_phone) {
        this.cus_phone = cus_phone;
    }

    public Vaccines getVaccine() {
        return vaccine;
    }

    public void setVaccine(Vaccines vaccine) {
        this.vaccine = vaccine;
    }

    public Vaccine_center getCenter() {
        return center;
    }

    public void setCenter(Vaccine_center center) {
        this.center = center;
    }

    public String getRegist_created_at() {
        return regist_created_at;
    }

    public void setRegist_created_at(String regist_created_at) {
        this.regist_created_at = regist_created_at;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPost_vaccination() {
        return post_vaccination;
    }

    public void setPost_vaccination(String post_vaccination) {
        this.post_vaccination = post_vaccination;
    }
}
