package com.prox.babyvaccinationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prox.babyvaccinationtracker.model.Vaccine_center;
import com.prox.babyvaccinationtracker.model.Vaccine_item;
import com.prox.babyvaccinationtracker.model.Vaccines;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class information_center extends AppCompatActivity {
    TextView txt_tentrungtam, txt_chungnhan, txt_diachi, txt_sdt, txt_thoigianlamviec;
    RecyclerView vaccineRecycleview;
    Vaccine_center vaccineCenter;
    vaccine_incenter_adapter vaccineIncenterAdapter;
    Vaccines vaccines;;
    HashMap<String,Vaccines> vaccinesHashMap = new HashMap<>();
//    List<vaccineadapter> vaccineItems = new ArrayList<>();
//    List<Vaccine_center> mlistcenter = new ArrayList<>();
    List<Vaccine_item> mlistvaccineitem = new ArrayList<>();
    String id = "-NgUp4yfKYmSSzwe46Nl";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_center);
        txt_tentrungtam = findViewById(R.id.txt_tentrungtam);
        txt_chungnhan = findViewById(R.id.txt_chungnhan);
        txt_diachi = findViewById(R.id.txt_diachi);
        txt_sdt = findViewById(R.id.txt_sdt);
        txt_thoigianlamviec = findViewById(R.id.txt_thoigianlamviec);
        vaccineRecycleview = findViewById(R.id.vaccineRecyclerView);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            vaccineCenter = (Vaccine_center) bundle.getSerializable("center_name");
            Log.i("NAM", "onCreate: bundle" + vaccineCenter.getCenter_id());
        }
        txt_tentrungtam.setText(vaccineCenter.getCenter_name());
        txt_chungnhan.setText(vaccineCenter.getActivity_certificate());
        txt_diachi.setText(vaccineCenter.getCenter_address());
        txt_sdt.setText(vaccineCenter.getHotline());
        txt_thoigianlamviec.setText(vaccineCenter.getWork_time());

        String address2 = vaccineCenter.getCenter_address2();
        String address = vaccineCenter.getCenter_address();
        String fullAddress = address;
        if (address2 != null && !address2.isEmpty()) {
            fullAddress = address2 + ", " + address;
            txt_diachi.setText(fullAddress);
        }

        String finalFullAddress = fullAddress;
        txt_diachi.setOnClickListener(view -> {

            // Tạo URI với địa chỉ
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(finalFullAddress));

            // Tạo Intent để mở ứng dụng Google Maps
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

            // Chỉ định gói ứng dụng của Google Maps
            mapIntent.setPackage("com.google.android.apps.maps");

            // Nếu có, mở ứng dụng Google Maps
            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                Toast.makeText(this, "Không tìm thấy ứng dụng Google Maps", Toast.LENGTH_SHORT).show();
            }
        });


        vaccineRecycleview.setLayoutManager(new  LinearLayoutManager(this));
//        vaccinesHashMap = vaccineCenter.getVaccines();


        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference vaccineCenterRef = databaseRef.child("vaccine_centers");
        vaccineCenterRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot vaccineCenterSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot vaccineSnapshot : vaccineCenterSnapshot.child("vaccines").getChildren()) {
                        String vaccineName = vaccineSnapshot.child("vaccine_name").getValue(String.class);
                        String vaccinePrice = vaccineSnapshot.child("price").getValue(String.class);
                        mlistvaccineitem.add(new Vaccine_item(vaccineName, vaccinePrice));
                    }
                }
                vaccineIncenterAdapter = new vaccine_incenter_adapter(mlistvaccineitem);
                vaccineRecycleview.setAdapter(vaccineIncenterAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase Error", "Error fetching data", databaseError.toException());
            }
        });
    }
}