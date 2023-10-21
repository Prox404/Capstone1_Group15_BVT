package com.prox.babyvaccinationtracker.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Vaccine_center implements Serializable {
    String center_id;
    String center_name;
    String activity_certificate;
    String center_address;
    String hotline;
    String work_time;
    String center_image;
    HashMap<String,Vaccines> vaccines;

    public Vaccine_center(String center_id, String center_name, String activity_certificate, String center_address, String hotline, String work_time, HashMap<String, Vaccines> vaccines) {
        this.center_id = center_id;
        this.center_name = center_name;
        this.activity_certificate = activity_certificate;
        this.center_address = center_address;
        this.hotline = hotline;
        this.work_time = work_time;
        this.vaccines = vaccines;
    }
    public  Vaccine_center(){}

    @Override
    public String toString() {
        return "Vaccine_center{" +
                "center_id='" + center_id + '\'' +
                ", center_name='" + center_name + '\'' +
                ", activity_certificate='" + activity_certificate + '\'' +
                ", center_address='" + center_address + '\'' +
                ", hotline='" + hotline + '\'' +
                ", work_time='" + work_time + '\'' +
                ", center_image='" + center_image + '\'' +
                '}';
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

    public HashMap<String, Vaccines> getVaccines() {
        return vaccines;
    }

    public void setCenter_id(String center_id) {
        this.center_id = center_id;
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

    public void setVaccines(HashMap<String, Vaccines> vaccines) {
        this.vaccines = vaccines;
    }
}
