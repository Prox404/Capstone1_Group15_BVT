package com.prox.babyvaccinationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prox.babyvaccinationtracker.adapter.BabyGridViewAdapter;
import com.prox.babyvaccinationtracker.model.Baby;
import com.prox.babyvaccinationtracker.model.Customer;

import java.util.ArrayList;
import java.util.List;

public class FamilyActivity extends AppCompatActivity {

    GridView gridViewBaby;
    ArrayList<Baby> babies = new ArrayList<>();

    BabyGridViewAdapter babyGridViewAdapter;

    LinearLayout addBabyContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family2);

        gridViewBaby = findViewById(R.id.gridViewBaby);
        addBabyContainer = findViewById(R.id.addBabyContainer);

        showBabyList();
        addBabyContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FamilyActivity.this, GetStartedActivity.class);
                startActivity(intent);
            }
        });
    }
    public void showBabyList(){
        Log.i("FamilyActivity", "showBabyList: " + "showBabyList called ");
        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        String UserID = sharedPreferences.getString("customer_id", "");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child("customers").child(UserID).child("babies");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                babies.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Baby baby = dataSnapshot.getValue(Baby.class);
                    baby.setBaby_id(dataSnapshot.getKey());
                    babies.add(baby);
                }

                babyGridViewAdapter = new BabyGridViewAdapter(FamilyActivity.this, babies);
                gridViewBaby.setAdapter(babyGridViewAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        showBabyList();
    }
}