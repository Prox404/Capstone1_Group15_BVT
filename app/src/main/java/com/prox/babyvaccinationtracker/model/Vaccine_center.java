package com.prox.babyvaccinationtracker.model;

import java.util.ArrayList;

public class Vaccine_center {
    String center_name;
    String activity_certificate;
    String center_address;
    String hotline;
    String work_time;

    ArrayList<Vaccines> vaccines;

    public void setVaccines(ArrayList<Vaccines> vaccines) {
        this.vaccines = vaccines;
    }

    public Vaccine_center(String center_name, String activity_certificate, String center_address, String hotline, String work_time, ArrayList<Vaccines> vaccines) {
        this.center_name = center_name;
        this.activity_certificate = activity_certificate;
        this.center_address = center_address;
        this.hotline = hotline;
        this.work_time = work_time;
        this.vaccines = vaccines;
    }

    public ArrayList<Vaccines> getVaccines() {
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

    public Vaccine_center(){

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
}