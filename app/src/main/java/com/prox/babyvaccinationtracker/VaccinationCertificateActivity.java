package com.prox.babyvaccinationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prox.babyvaccinationtracker.model.VaccinationCertificate;
import com.prox.babyvaccinationtracker.response.SideEffectResponse;
import com.prox.babyvaccinationtracker.service.api.ApiService;
import com.squareup.picasso.Picasso;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Call;

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
            textViewSideEffectsResponse;

    LinearLayout sideEffectContainer;
    Button buttonSend;

    ImageView imageViewQR;

    EditText editTextSideEffects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccination_certificate);

        Intent intent = getIntent();
        String certificate_id = intent.getStringExtra("certificate_id");

        if (certificate_id == null) {
            Toast.makeText(this, "Chứng nhận không tồn tại !", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Log.i("Certificate", "onCreate: " + certificate_id);

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
            imageViewQR = findViewById(R.id.imageViewQR);
            editTextSideEffects = findViewById(R.id.editTextSideEffects);
            sideEffectContainer = findViewById(R.id.sideEffectContainer);
            buttonSend = findViewById(R.id.buttonSend);

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

                    String Qr_url = vaccinationCertificate.getQr().replace("https", "http");
                    Picasso.get().load(vaccinationCertificate.getQr()).into(imageViewQR);

                    if (vaccinationCertificate.getSide_effects() != null) {
                        textViewSideEffects.setText(vaccinationCertificate.getSide_effects());
                        textViewSideEffects.setVisibility(View.VISIBLE);
                    } else {
                        sideEffectContainer.setVisibility(View.VISIBLE);
                        buttonSend.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String sideEffects = editTextSideEffects.getText().toString();
                                if (sideEffects.isEmpty()) {
                                    Toast.makeText(VaccinationCertificateActivity.this, "Vui lòng nhập phản ứng phụ !", Toast.LENGTH_SHORT).show();
                                } else {
                                    DatabaseReference certificateReference = FirebaseDatabase.getInstance().getReference("Vaccination_Certificate").child(certificate_id);
                                    certificateReference.child("side_effects").setValue(sideEffects);
                                    textViewSideEffects.setText(sideEffects);
                                    textViewSideEffects.setVisibility(View.VISIBLE);
                                    ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
                                    Call<SideEffectResponse> call = apiService.sendSideEffect(sideEffects, vaccinationCertificate.getVaccine().getPost_vaccination_reactions());
                                    call.enqueue(new Callback<SideEffectResponse>() {
                                        @Override
                                        public void onResponse(Call<SideEffectResponse> call, Response<SideEffectResponse> response) {
                                            if (response.isSuccessful()) {
                                                SideEffectResponse sideEffectResponse = response.body();
                                                if (sideEffectResponse != null) {
                                                    Log.i("Side Effect Response", "onResponse: " + sideEffectResponse.getMessage());
                                                    String result = sideEffectResponse.getMessage();
                                                    certificateReference.child("side_effects_response").setValue(result);
                                                    textViewSideEffectsResponse.setText(result);
                                                    textViewSideEffectsResponse.setVisibility(View.VISIBLE);
//                                                    textViewSideEffectsResponse.setVisibility(View.VISIBLE);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<SideEffectResponse> call, Throwable t) {
                                            Log.i("SideEffect", "onFailure: " + t.getMessage());
                                        }
                                    });
                                    sideEffectContainer.setVisibility(View.GONE);
                                    Toast.makeText(VaccinationCertificateActivity.this, "Cảm ơn bạn đã gửi phản ứng phụ !", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}