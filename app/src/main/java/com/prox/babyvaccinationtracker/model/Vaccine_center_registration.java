package com.prox.babyvaccinationtracker.model;

public class Vaccine_center_registration {
    Vaccine_center center;
    int status;
    String  registration_date;

    public Vaccine_center_registration() {
    }

    public Vaccine_center_registration(Vaccine_center center, int status, String registration_date) {
        this.center = center;
        this.status = status;
        this.registration_date = registration_date;
    }

    public Vaccine_center getCenter() {
        return center;
    }

    public void setCenter(Vaccine_center center) {
        this.center = center;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRegistration_date() {
        return registration_date;
    }

    public void setRegistration_date(String registration_date) {
        this.registration_date = registration_date;
    }
}
