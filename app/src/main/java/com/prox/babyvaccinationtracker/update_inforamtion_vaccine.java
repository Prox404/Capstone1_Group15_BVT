package com.prox.babyvaccinationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cloudinary.android.MediaManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prox.babyvaccinationtracker.model.Vaccines;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class update_inforamtion_vaccine extends AppCompatActivity {
    EditText vaccine_name,vac_effectiveness,post_vaccination_reactions,origin,vaccination_target_group,contraindications,quantity,dosage,unit,date_of_entry,price;
    Button btn_update, btn_delete,btn_add_new_image;

    Vaccines vaccine;

    RecyclerView recyclerview_image;
    RecyclerAdapter recyclerAdapter;
    ArrayList<Uri> uri_image;



    String id = "-NgF0yRkjf0QaniK70wF"; // id vaccine

    Map config = new HashMap();
    private void configCloudinary() {
        config.put("cloud_name", "du42cexqi");
        config.put("api_key", "346965553513552");
        config.put("api_secret", "SguEwSEbwQNgOgHRTkyxeuG-478");
        MediaManager.init(this, config);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_inforamtion_vaccine);

        vaccine_name = findViewById(R.id.update_vaccine_name);
        vac_effectiveness = findViewById(R.id.update_vac_effectiveness);
        post_vaccination_reactions = findViewById(R.id.update_post_vaccination_reactions);
        origin = findViewById(R.id.update_orgin);
        vaccination_target_group = findViewById(R.id.update_vaccination_target_group);
        contraindications = findViewById(R.id.update_contraindications);
        quantity = findViewById(R.id.update_quantity);
        dosage = findViewById(R.id.update_dosage);
        unit = findViewById(R.id.update_unit);
        date_of_entry = findViewById(R.id.update_date_of_entry);
        price = findViewById(R.id.update_price);

        btn_delete = findViewById(R.id.btn_delete);
        btn_update = findViewById(R.id.btn_update);
        btn_add_new_image = findViewById(R.id.btn_add_new_image);


        recyclerview_image = findViewById(R.id.update_recyclerview_image);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Vaccines");
        configCloudinary();
        // load firebase
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                vaccine = new Vaccines();
                try{
                    vaccine = snapshot.child(id).getValue(Vaccines.class);

                }catch (Exception e){
                    Toast.makeText(update_inforamtion_vaccine.this, "Có gì đó đang lỗi", Toast.LENGTH_SHORT).show();
                    return;
                }

                vaccine_name.setText(vaccine.getVaccine_name());
                vac_effectiveness.setText(vaccine.getVac_effectiveness());
                post_vaccination_reactions.setText(vaccine.getPost_vaccination_reactions());
                origin.setText(vaccine.getOrigin());
                vaccination_target_group.setText(vaccine.getVaccination_target_group());
                contraindications.setText(vaccine.getContraindications());
                quantity.setText(vaccine.getQuantity());
                dosage.setText(vaccine.getDosage());
                unit.setText(vaccine.getUnit());
                date_of_entry.setText(vaccine.getDate_of_entry());
                price.setText(vaccine.getPrice());

                uri_image = new ArrayList<>();

                for(String a : vaccine.getVaccine_image()){
                    Uri uri = Uri.parse(a);
                    uri_image.add(uri);
                }

                recyclerAdapter = new RecyclerAdapter(uri_image,update_inforamtion_vaccine.this);
                recyclerview_image.setLayoutManager(new GridLayoutManager(update_inforamtion_vaccine.this, 4));
                recyclerview_image.setAdapter(recyclerAdapter);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Database error: " + error.getMessage());
            }
        });




        // button delete
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean check =  vaccine.isDeleted();
                if(check == true){
                    return;
                }
                else if (check == false){
                    check = true;
                    reference.child(id).child("deleted").setValue(check).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Data was successfully pushed
                                // You can perform any actions here, such as displaying a message
                                Toast.makeText(update_inforamtion_vaccine.this, "vaccine deleted successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                // Data push failed
                                // Handle the error here
                                Toast.makeText(update_inforamtion_vaccine.this, "failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }

}