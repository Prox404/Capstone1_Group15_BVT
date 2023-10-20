package com.prox.babyvaccinationtracker.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class VaccinationCertificate {
    private String vaccineCertificate_id;
    private Baby baby;
    private String qr;
    private Vaccines vaccine;
    private Vaccine_center center;
    private String vaccineCertificate_Created_at;
    private String side_effects_response;
    private String side_effects;

    public VaccinationCertificate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = new Date();
        vaccineCertificate_Created_at = formatter.format(date);
    }

    public VaccinationCertificate(String vaccineCertificate_id, Baby baby, String qr, Vaccines vaccine, Vaccine_center center, String vaccineCertificate_Created_at, String side_effects_response, String side_effects) {
        this.vaccineCertificate_id = vaccineCertificate_id;
        this.baby = baby;
        this.qr = qr;
        this.vaccine = vaccine;
        this.center = center;
        this.vaccineCertificate_Created_at = vaccineCertificate_Created_at;
        this.side_effects_response = side_effects_response;
        this.side_effects = side_effects;
    }

    public VaccinationCertificate(Baby baby, String qr, Vaccines vaccine, Vaccine_center center, String vaccineCertificate_Created_at, String side_effects_response, String side_effects) {
        this.baby = baby;
        this.qr = qr;
        this.vaccine = vaccine;
        this.center = center;
        this.vaccineCertificate_Created_at = vaccineCertificate_Created_at;
        this.side_effects_response = side_effects_response;
        this.side_effects = side_effects;
    }

    public VaccinationCertificate(Baby baby, String qr, Vaccines vaccine, Vaccine_center center, String vaccineCertificate_Created_at) {
        this.baby = baby;
        this.qr = qr;
        this.vaccine = vaccine;
        this.center = center;
        this.vaccineCertificate_Created_at = vaccineCertificate_Created_at;
    }

    public String getVaccineCertificate_id() {
        return vaccineCertificate_id;
    }

    public void setVaccineCertificate_id(String vaccineCertificate_id) {
        this.vaccineCertificate_id = vaccineCertificate_id;
    }

    public Baby getBaby() {
        return baby;
    }

    public void setBaby(Baby baby) {
        this.baby = baby;
    }

    @Override
    public String toString() {
        return "VaccinationCertificate{" +
                "baby=" + baby.toString() +
                ", qr='" + qr + '\'' +
                ", vaccine=" + vaccine.toString() +
                ", center=" + center.toString() +
                ", vaccineCertificate_Created_at='" + vaccineCertificate_Created_at + '\'' +
                '}';
    }

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
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

    public String getSide_effects_response() {
        return side_effects_response;
    }

    public void setSide_effects_response(String side_effects_response) {
        this.side_effects_response = side_effects_response;
    }

    public String getSide_effects() {
        return side_effects;
    }

    public void setSide_effects(String side_effects) {
        this.side_effects = side_effects;
    }

    public String getVaccineCertificate_Created_at() {
        return vaccineCertificate_Created_at;
    }

    public void setVaccineCertificate_Created_at(String vaccineCertificate_Created_at) {
        this.vaccineCertificate_Created_at = vaccineCertificate_Created_at;
    }

}