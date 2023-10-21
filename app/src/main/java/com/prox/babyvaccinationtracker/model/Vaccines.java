package com.prox.babyvaccinationtracker.model;


import java.util.ArrayList;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Vaccines {
    String vaccine_name;
    String vac_effectiveness;
    String post_vaccination_reactions;
    String origin;
    String vaccination_target_group;
    String contraindications;
    String quantity;
    String dosage;
    String unit;
    String date_of_entry;
    String price;
    ArrayList<String> vaccine_image;
    boolean deleted;

    public Vaccines() {
    }

    public Vaccines(String vaccine_name, String vac_effectiveness, String post_vaccination_reactions, String origin, String vaccination_target_group, String contraindications, String quantity, String dosage, String unit, String date_of_entry, String price, ArrayList<String> vaccine_image, boolean deleted) {
        this.vaccine_name = vaccine_name;
        this.vac_effectiveness = vac_effectiveness;
        this.post_vaccination_reactions = post_vaccination_reactions;
        this.origin = origin;
        this.vaccination_target_group = vaccination_target_group;
        this.contraindications = contraindications;
        this.quantity = quantity;
        this.dosage = dosage;
        this.unit = unit;
        this.date_of_entry = date_of_entry;
        this.price = price;
        this.vaccine_image = vaccine_image;
        this.deleted = deleted;
    }

    public String getVaccine_name() {
        return vaccine_name;
    }

    public void setVaccine_name(String vaccine_name) {
        this.vaccine_name = vaccine_name;
    }

    public String getVac_effectiveness() {
        return vac_effectiveness;
    }

    public void setVac_effectiveness(String vac_effectiveness) {
        this.vac_effectiveness = vac_effectiveness;
    }

    public String getPost_vaccination_reactions() {
        return post_vaccination_reactions;
    }

    public void setPost_vaccination_reactions(String post_vaccination_reactions) {
        this.post_vaccination_reactions = post_vaccination_reactions;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getVaccination_target_group() {
        return vaccination_target_group;
    }

    public void setVaccination_target_group(String vaccination_target_group) {
        this.vaccination_target_group = vaccination_target_group;
    }

    public String getContraindications() {
        return contraindications;
    }

    public void setContraindications(String contraindications) {
        this.contraindications = contraindications;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDate_of_entry() {
        return date_of_entry;
    }

    public void setDate_of_entry(String date_of_entry) {
        this.date_of_entry = date_of_entry;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public ArrayList<String> getVaccine_image() {
        return vaccine_image;
    }

    public void setVaccine_image(ArrayList<String> vaccine_image) {
        this.vaccine_image = vaccine_image;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

}
