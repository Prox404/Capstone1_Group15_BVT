package com.admin.babyvaccinationtracker.model;

import java.io.Serializable;
import java.util.HashMap;

public class Vaccine_center implements Serializable {
    String center_id;
    String center_name;
    String activity_certificate;
    String center_address;
    String hotline;
    String work_time;
    String center_image;
    String center_email;
    String center_password;
    HashMap<String,Vaccines> vaccines;
    String center_address2;
    public String getCenter_address2() {
        return center_address2;
    }

    public void setCenter_address2(String center_address2) {
        this.center_address2 = center_address2;
    }

    public Vaccine_center(String center_id, String center_name, String activity_certificate, String center_address, String hotline, String work_time, String center_image, String center_email, String center_password, HashMap<String, Vaccines> vaccines, String center_address2) {
        this.center_id = center_id;
        this.center_name = center_name;
        this.activity_certificate = activity_certificate;
        this.center_address = center_address;
        this.hotline = hotline;
        this.work_time = work_time;
        this.center_image = center_image;
        this.center_email = center_email;
        this.center_password = center_password;
        this.vaccines = vaccines;
        this.center_address2 = center_address2;
    }

    public Vaccine_center(String center_id, String center_name, String activity_certificate, String center_address, String hotline, String work_time, String center_image, String center_email, String center_password, String center_address2) {
        this.center_id = center_id;
        this.center_name = center_name;
        this.activity_certificate = activity_certificate;
        this.center_address = center_address;
        this.hotline = hotline;
        this.work_time = work_time;
        this.center_image = center_image;
        this.center_email = center_email;
        this.center_password = center_password;
        this.center_address2 = center_address2;
    }


    public Vaccine_center(String center_id, String center_name, String activity_certificate, String center_address, String hotline, String work_time, String center_image, String center_email, String center_password) {
        this.center_id = center_id;
        this.center_name = center_name;
        this.activity_certificate = activity_certificate;
        this.center_address = center_address;
        this.hotline = hotline;
        this.work_time = work_time;
        this.center_image = center_image;
        this.center_email = center_email;
        this.center_password = center_password;
    }

    public Vaccine_center(String center_id, String center_name, String activity_certificate, String center_address, String hotline, String work_time, HashMap<String, Vaccines> vaccines) {
        this.center_id = center_id;
        this.center_name = center_name;
        this.activity_certificate = activity_certificate;
        this.center_address = center_address;
        this.hotline = hotline;
        this.work_time = work_time;
        this.vaccines = vaccines;
    }

    public Vaccine_center(){}

    public Vaccine_center(String center_name, String activity_certificate, String center_address, String hotline, String work_time, String center_image) {
        this.center_name = center_name;
        this.activity_certificate = activity_certificate;
        this.center_address = center_address;
        this.hotline = hotline;
        this.work_time = work_time;
        this.center_image = center_image;
    }

    public String getCenter_email() {
        return center_email;
    }

    public void setCenter_email(String center_email) {
        this.center_email = center_email;
    }

    public String getCenter_password() {
        return center_password;
    }

    public void setCenter_password(String center_password) {
        this.center_password = center_password;
    }

    public String getCenter_id() {
        return center_id;
    }

    public String getCenter_name() {
        return center_name;
    }

    public String getActivity_certificate() {
        return activity_certificate;
    }

    public String getCenter_address() {
        return center_address;
    }

    public String getHotline() {
        return hotline;
    }

    public String getWork_time() {
        return work_time;
    }

    public void setCenter_id(String center_id) {
        this.center_id = center_id;
    }

    public String getCenter_image() {
        return center_image;
    }

    public HashMap<String, Vaccines> getVaccines() {
        return vaccines;
    }


    public void setCenter_name(String center_name) {
        this.center_name = center_name;
    }

    public void setActivity_certificate(String activity_certificate) {
        this.activity_certificate = activity_certificate;
    }

    public void setCenter_address(String center_address) {
        this.center_address = center_address;
    }

    public void setHotline(String hotline) {
        this.hotline = hotline;
    }

    public void setWork_time(String work_time) {
        this.work_time = work_time;
    }

    public void setCenter_image(String center_image) {
        this.center_image = center_image;
    }

    public void setVaccines(HashMap<String, Vaccines> vaccines) {
        this.vaccines = vaccines;
    }
}