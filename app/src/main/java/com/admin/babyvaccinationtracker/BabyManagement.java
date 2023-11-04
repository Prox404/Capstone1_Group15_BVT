package com.admin.babyvaccinationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.admin.babyvaccinationtracker.Adapter.BabyHealthAdapter;
import com.admin.babyvaccinationtracker.model.Baby;
import com.admin.babyvaccinationtracker.model.Customer;
import com.admin.babyvaccinationtracker.model.VaccinationCertificate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BabyManagement extends AppCompatActivity {

    RecyclerView recyclerViewBaby;
    List<Baby>  babyList= new ArrayList<>();
    List<Baby>  filterList= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baby_management);

        recyclerViewBaby = findViewById(R.id.recyclerViewBaby);

        DatabaseReference customersRef = FirebaseDatabase.getInstance().getReference().child("users").child("customers");

        customersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if (dataSnapshot.exists()) {
                        DataSnapshot babiesSnapshot = dataSnapshot.child("babies");
                        for (DataSnapshot babySnapshot : babiesSnapshot.getChildren()) {
                            Baby baby = babySnapshot.getValue(Baby.class);
                            baby.setBaby_id(babySnapshot.getKey());
                            if (baby != null) {
                                babyList.add(baby);
                                Log.i("BabyManagement", "onDataChange: " + baby.toString());
                            }
                        }
                    }
                }
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BabyManagement.this);
                recyclerViewBaby.setLayoutManager(linearLayoutManager);
                BabyHealthAdapter babyHealthAdapter = new BabyHealthAdapter(BabyManagement.this, babyList);
                recyclerViewBaby.setAdapter(babyHealthAdapter);

                Log.i("fuiafuiauoifhoui", "onDataChange: " + babyList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("BabyManagement", "onCancelled: " + error.getMessage());
            }
        });
    }
}