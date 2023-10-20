package com.prox.babyvaccinationtracker.model;

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
    String vaccine_type;
    boolean deleted;

    public Vaccines() {
    }

    public Vaccines(String vaccine_name, String vac_effectiveness, String post_vaccination_reactions, String origin, String vaccination_target_group, String contraindications, String quantity, String dosage, String unit, String date_of_entry, String price, ArrayList<String> vaccine_image, String vaccine_type, boolean deleted) {
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
        this.vaccine_type = vaccine_type;
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

    public String getVaccine_type() {
        return vaccine_type;
    }

    public void setVaccine_type(String vaccine_type) {
        this.vaccine_type = vaccine_type;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    //    private Map map(){
//        Map<String, String> mymap = new HashMap<String, String>();
//        mymap.put("vaccine_name", this.vaccine_name);
//        mymap.put("vac_effectiveness",this.vac_effectiveness);
//        mymap.put("post_vaccination_reactions", this.post_vaccination_reactions);
//        mymap.put("origin", this.origin);
//        mymap.put("vaccination_target_group",this.vaccination_target_group+"");
//        mymap.put("contraindications",this.contraindications);
//        mymap.put("quantity",this.quantity+"");
//        mymap.put("dosage",this.dosage+"");
//        mymap.put("unit",this.unit);
//        mymap.put("date_of_entry",this.date_of_entry);
//        mymap.put("price",this.price+"");
//        //mymap.put("vaccine_image",this.vaccine_image);
//        Log.i("vaccine model", "map: " + mymap.toString());
//        return mymap;
//    }
//    public void pushDataFisebase(){
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("Vaccines");
//        myRef.push().setValue(this.map());
//        String uniqueID = UUID.randomUUID().toString();
//        myRef.child(uniqueID).child("vaccine_name").setValue(this.vaccine_image);
//        myRef.child(uniqueID).child("vac_effectiveness").setValue(this.vac_effectiveness);
//        myRef.child(uniqueID).child("post_vaccination_reactions").setValue(this.post_vaccination_reactions);
//        myRef.child(uniqueID).child("origin").setValue(this.origin);
//        myRef.child(uniqueID).child("vaccination_target_group").setValue(this.vaccination_target_group);
//        myRef.child(uniqueID).child("contraindications").setValue(this.contraindications);
//        myRef.child(uniqueID).child("quantity").setValue(this.quantity);
//        myRef.child(uniqueID).child("dosage").setValue(this.dosage);
//        myRef.child(uniqueID).child("unit").setValue(this.unit);
//        myRef.child(uniqueID).child("date_of_entry").setValue(this.date_of_entry);
//        myRef.child(uniqueID).child("price").setValue(this.price);
//        myRef.child(uniqueID).child("vaccine_image").setValue(this.vaccine_image);
//
//    }

}
