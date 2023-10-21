package com.prox.babyvaccinationtracker.model;

public class Vaccine_item {
    private String vaccinePrice;
    private String vaccineName;

    public Vaccine_item(String vaccineName, String vaccinePrice) {
        this.vaccineName = vaccineName;
        this.vaccinePrice = vaccinePrice;
    }
    public String getVaccineName() {
        return vaccineName;
    }
    public String getVaccinePrice() {
        return vaccinePrice;
    }
}
