package com.prox.babyvaccinationtracker.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Vaccine_center {
    String center_name;
    String activity_certificate;
    String center_address;
    String hotline;
    String work_time;
    String center_image;

    HashMap<String,Vaccines> vaccines;

    public Vaccine_center(){}

    public Vaccine_center(String center_name, String activity_certificate, String center_address, String hotline, String work_time, String center_image, HashMap<String, Vaccines> vaccines) {
        this.center_name = center_name;
        this.activity_certificate = activity_certificate;
        this.center_address = center_address;
        this.hotline = hotline;
        this.work_time = work_time;
        this.center_image = center_image;
        this.vaccines = vaccines;
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
