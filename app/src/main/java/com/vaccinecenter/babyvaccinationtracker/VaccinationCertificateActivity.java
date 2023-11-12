package com.vaccinecenter.babyvaccinationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.vaccinecenter.babyvaccinationtracker.R;
import com.vaccinecenter.babyvaccinationtracker.model.VaccinationCertificate;

public class VaccinationCertificateActivity extends AppCompatActivity {

    TextView textViewBabyName,
            textViewBabyBirthDate,
            textViewBabyGender,
            textViewBabyCongenitalDisease,
            textViewCenterName,
            textViewCenterAddress,
            textViewVaccineName,
            textViewVaccineEntryDate,
            textViewVaccineDosage,
            textViewSideEffects,
            textViewSideEffectsResponse,
            textViewTime;

    ImageView imageViewQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccination_certificate);

        Intent intent = getIntent();
        String certificate_id = intent.getStringExtra("certificate_id");

        textViewBabyName = findViewById(R.id.textViewBabyName);
        textViewBabyBirthDate = findViewById(R.id.textViewBabyBirthDate);
        textViewBabyGender = findViewById(R.id.textViewBabyGender);
        textViewBabyCongenitalDisease = findViewById(R.id.textViewBabyCongenitalDisease);
        textViewCenterName = findViewById(R.id.textViewCenterName);
        textViewCenterAddress = findViewById(R.id.textViewCenterAddress);
        textViewVaccineName = findViewById(R.id.textViewVaccineCenterName);
        textViewVaccineEntryDate = findViewById(R.id.textViewVaccineEntryDate);
        textViewVaccineDosage = findViewById(R.id.textViewVaccineDosage);
        textViewSideEffects = findViewById(R.id.textViewSideEffects);
        textViewSideEffectsResponse = findViewById(R.id.textViewSideEffectsResponse);
        textViewTime = findViewById(R.id.textViewTime);
        imageViewQR = findViewById(R.id.imageViewQR);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Vaccination_Certificate").child(certificate_id);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                VaccinationCertificate vaccinationCertificate = snapshot.getValue(VaccinationCertificate.class);
                vaccinationCertificate.setVaccineCertificate_id(snapshot.getKey());

                Log.i("Certificate", "onDataChange: " + vaccinationCertificate.toString());
                textViewBabyName.setText(vaccinationCertificate.getBaby().getBaby_name());
                textViewBabyBirthDate.setText(vaccinationCertificate.getBaby().getBaby_birthday());
                textViewBabyGender.setText(vaccinationCertificate.getBaby().getBaby_gender());
                textViewBabyCongenitalDisease.setText(vaccinationCertificate.getBaby().getBaby_congenital_disease());
                textViewCenterName.setText(vaccinationCertificate.getCenter().getCenter_name());
                textViewCenterAddress.setText(vaccinationCertificate.getCenter().getCenter_address());
                textViewVaccineName.setText(vaccinationCertificate.getVaccine().getVaccine_name());
                textViewVaccineEntryDate.setText(vaccinationCertificate.getVaccine().getDate_of_entry());
                String dosage = vaccinationCertificate.getVaccine().getDosage() + " " + vaccinationCertificate.getVaccine().getUnit();
                textViewVaccineDosage.setText(dosage);
                textViewTime.setText(vaccinationCertificate.getVaccineCertificate_Created_at());

                String Qr_url = vaccinationCertificate.getQr().replace("https", "http");
                Picasso.get().load(vaccinationCertificate.getQr()).into(imageViewQR);

                if (vaccinationCertificate.getSide_effects() != null) {
                    textViewSideEffects.setText(vaccinationCertificate.getSide_effects());
                    textViewSideEffects.setVisibility(View.VISIBLE);
                    if (vaccinationCertificate.getSide_effects_Response() != null) {
                        textViewSideEffectsResponse.setText(vaccinationCertificate.getSide_effects_Response());
                        textViewSideEffectsResponse.setVisibility(View.VISIBLE);
                    } else {
                        textViewSideEffectsResponse.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}