package com.admin.babyvaccinationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.admin.babyvaccinationtracker.Adapter.CenterRegistrationAdapter;
import com.admin.babyvaccinationtracker.model.Vaccine_center_registration;

import java.util.ArrayList;

public class Admin_management_of_vaccine_center_registration extends AppCompatActivity {

    RecyclerView confirm_vaccine_center_registration;

    ArrayList<Vaccine_center_registration> registrations = new ArrayList<>();
    CenterRegistrationAdapter adapter;

    ImageView imageView_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_management_of_vaccine_center_registration);

        imageView_back = findViewById(R.id.imageView_back);

        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        confirm_vaccine_center_registration = findViewById(R.id.confirm_vaccine_center_registration);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference vaccineRef = database.getReference("Vaccine_center_registration");

        vaccineRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                registrations.clear();
                adapter = new CenterRegistrationAdapter(Admin_management_of_vaccine_center_registration.this,registrations);
                adapter.notifyDataSetChanged();
                for(DataSnapshot snap : snapshot.getChildren()){
                    int status = snap.child("status").getValue(int.class);
                    if(status==0){
                        Vaccine_center_registration regi = snap.getValue(Vaccine_center_registration.class);
                        regi.setCenter_registration_id(snap.getKey());
                        registrations.add(regi);
                    }
                }
                confirm_vaccine_center_registration.setLayoutManager(new GridLayoutManager(Admin_management_of_vaccine_center_registration.this,1));
                confirm_vaccine_center_registration.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Firebasseeeee", ""+error);
            }
        });


    }
}