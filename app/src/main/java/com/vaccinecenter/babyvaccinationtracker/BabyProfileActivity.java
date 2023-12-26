package com.vaccinecenter.babyvaccinationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.vaccinecenter.babyvaccinationtracker.Adapter.CompletedRequestAdapter;
import com.vaccinecenter.babyvaccinationtracker.model.Baby;
import com.vaccinecenter.babyvaccinationtracker.model.Vaccination_Registration;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BabyProfileActivity extends AppCompatActivity {

    TextView textViewBabyName, textViewBirthday, textViewGender, textViewCongenitalDisease;
    RecyclerView recyclerViewCertificate;
    ImageView imageViewQR, imageViewAvatar;
    View emptyLayout;

    List<Vaccination_Registration> vaccination_registrations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baby_profile);

        Intent intent = getIntent();
        String baby_id = intent.getStringExtra("baby_id");
        String cus_id = intent.getStringExtra("cus_id");

        if (baby_id == null || cus_id == null) {
            Toast.makeText(this, "Đã xảy ra lỗi, hãy thử lại", Toast.LENGTH_SHORT).show();
            finish();
        }

        textViewBabyName = findViewById(R.id.textViewBabyName);
        textViewBirthday = findViewById(R.id.textViewBirthday);
        textViewGender = findViewById(R.id.textViewGender);
        textViewCongenitalDisease = findViewById(R.id.textViewCongenitalDisease);
        imageViewQR = findViewById(R.id.imageViewQR);
        recyclerViewCertificate = findViewById(R.id.recyclerViewCertificate);
        imageViewAvatar = findViewById(R.id.imageViewAvatar);
        emptyLayout = findViewById(R.id.emptyLayout);

        Log.i("Profile", "onCreate: " + baby_id + " - " + cus_id);

        DatabaseReference babyReference = FirebaseDatabase.getInstance().getReference("users").child("customers").child(cus_id).child("babies").child(baby_id);
        DatabaseReference completeRequestReference = FirebaseDatabase.getInstance().getReference("Vaccination_Registration");
        Query query = completeRequestReference.orderByChild("cus/customer_id").equalTo(cus_id);

        babyReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Baby baby = snapshot.getValue(Baby.class);
                if (baby == null) {
                    Toast.makeText(BabyProfileActivity.this, "Đã xảy ra lỗi, hãy thử lại", Toast.LENGTH_SHORT).show();
//                    finish();
                } else {
                    textViewBabyName.setText(baby.getBaby_name());
                    textViewBirthday.setText(baby.getBaby_birthday());
                    textViewGender.setText(baby.getBaby_gender());
                    textViewCongenitalDisease.setText(baby.getBaby_congenital_disease());
                    String qr_url = snapshot.child("qr").getValue(String.class);
                    Log.i("Image", "onDataChange: " + qr_url);
                    if (qr_url != null) {
                        String imageURL = qr_url.contains("https") ? qr_url : qr_url.replace("http", "https");
                        Picasso.get().load(imageURL).into(imageViewQR, new Callback() {
                            @Override
                            public void onSuccess() {
                                Log.i("Image", "onSuccess: " + imageURL);
                            }

                            @Override
                            public void onError(Exception e) {
                                Log.i("Image", "onError: " + imageURL);
                                imageViewQR.setImageResource(R.drawable.ic_nothing);
                            }
                        });
                    }
                    String imgaeUrl = baby.getBaby_avatar().contains("https") ? baby.getBaby_avatar() : baby.getBaby_avatar().replace("http", "https");
                    Picasso.get().load(imgaeUrl).into(imageViewAvatar);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                vaccination_registrations.clear();
                Log.i("Completed", "onDataChange: " + snapshot.getChildrenCount());
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Vaccination_Registration vaccination_registration = dataSnapshot.getValue(Vaccination_Registration.class);
                    assert vaccination_registration != null;
                    vaccination_registration.setRegister_id(dataSnapshot.getKey());
                    if (vaccination_registration.getStatus() == 3 && vaccination_registration.getBaby().getBaby_id().equals(baby_id)){
                        vaccination_registrations.add(vaccination_registration);
                    }

                }
                Log.i("Completed", "onDataChange: " + vaccination_registrations.size());
                if (vaccination_registrations.size() == 0){
                    emptyLayout.setVisibility(View.VISIBLE);
                }
                CompletedRequestAdapter pendingRequestAdapter = new CompletedRequestAdapter( BabyProfileActivity.this , vaccination_registrations);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BabyProfileActivity.this);
                recyclerViewCertificate.setAdapter(pendingRequestAdapter);
                recyclerViewCertificate.setLayoutManager(linearLayoutManager);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}