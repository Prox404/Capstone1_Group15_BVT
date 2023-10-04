package com.prox.babyvaccinationtracker.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Vaccines {
    String vaccine_name;
    String vac_effectiveness;
    String post_vaccination_reactions;
    String origin;
    int vaccination_target_group;
    String contraindications;
    int quantity;
    int dosage;
    String unit;
    String date_of_entry;
    double price;
    public Vaccines(
             String vaccine_name,
             String vac_effectiveness,
             String post_vaccination_reactions,
             String origin,
             int vaccination_target_group,
             String contraindications,
             int quantity,
             int dosage,
             String unit,
             String date_of_entry,
             double price
             ){
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
    }
    public Vaccines(){
        vaccine_name ="";
        vac_effectiveness ="";
        post_vaccination_reactions = "";
        origin = "";
        vaccination_target_group = 0;
        contraindications = "";
        quantity = 0;
        dosage = 0;
        unit = "";
        date_of_entry = "";
        price = 0;
    }
    private Map map(){
        Map<String, String> mymap = new HashMap<String, String>();
        mymap.put("vaccine_name", this.vaccine_name);
        mymap.put("vac_effectiveness",this.vac_effectiveness);
        mymap.put("post_vaccination_reactions", this.post_vaccination_reactions);
        mymap.put("origin", this.origin);
        mymap.put("vaccination_target_group",this.vaccination_target_group+"");
        mymap.put("contraindications",this.contraindications);
        mymap.put("quantity",this.quantity+"");
        mymap.put("dosage",this.dosage+"");
        mymap.put("unit",this.unit);
        mymap.put("date_of_entry",this.date_of_entry);
        mymap.put("price",this.price+"");
        return mymap;
    }
    public void pushDataFisebase(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Vaccines");
        myRef.push().setValue(this.map());
    }

}
