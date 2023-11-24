package com.vaccinecenter.babyvaccinationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
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
import com.squareup.picasso.Picasso;
import com.vaccinecenter.babyvaccinationtracker.Adapter.CompletedRequestAdapter;
import com.vaccinecenter.babyvaccinationtracker.Adapter.RecyclerAdapter;
import com.vaccinecenter.babyvaccinationtracker.model.Baby;
import com.vaccinecenter.babyvaccinationtracker.model.Customer;
import com.vaccinecenter.babyvaccinationtracker.model.VaccinationCertificate;
import com.vaccinecenter.babyvaccinationtracker.model.Vaccination_Registration;
import com.vaccinecenter.babyvaccinationtracker.model.Vaccines;

import java.util.ArrayList;
import java.util.Calendar;

public class view_child_information extends AppCompatActivity {

    ImageView imageView_Baby_Avatar;
    TextView textView_Name, textView_Date_Of_Birth, textView_Congenital_Disease, textView_Gender;
    RecyclerView recyclerViewCertificate;
    Baby baby;
    View emptyLayout;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_child_information);
        imageView_Baby_Avatar = findViewById(R.id.imageView_Baby_Avatar);
        textView_Name = findViewById(R.id.textView_Name);
        textView_Date_Of_Birth = findViewById(R.id.textView_Date_Of_Birth);
        textView_Congenital_Disease = findViewById(R.id.textView_Congenital_Disease);
        textView_Gender = findViewById(R.id.textView_Gender);
        recyclerViewCertificate = findViewById(R.id.recyclerViewCertificate);
        emptyLayout = findViewById(R.id.emptyLayout);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            // Lấy đối tượng baby từ Bundle
            baby = (Baby) bundle.getSerializable("baby");
            if (baby != null) {
                Picasso.get().load(baby.getBaby_avatar()).into(imageView_Baby_Avatar);
                textView_Name.setText(baby.getBaby_name());
                textView_Date_Of_Birth.setText(baby.getBaby_birthday());
                textView_Gender.setText(baby.getBaby_gender());
                textView_Congenital_Disease.setText(baby.getBaby_congenital_disease());

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Vaccination_Registration");
                Query query = databaseReference.orderByChild("baby/baby_id").equalTo(baby.getBaby_id());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        Object object = snapshot.getValue();
//                        Log.i("object", "onDataChange: " + object);
                        ArrayList<Vaccination_Registration> vaccination_registrations = new ArrayList<>();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Vaccination_Registration vaccination_registration = dataSnapshot.getValue(Vaccination_Registration.class);
                            vaccination_registration.setRegister_id(dataSnapshot.getKey());
                            if (vaccination_registration.getStatus() == 3)
                                vaccination_registrations.add(vaccination_registration);
                        }
                        CompletedRequestAdapter pendingRequestAdapter = new CompletedRequestAdapter( view_child_information.this , vaccination_registrations);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view_child_information.this);
                        recyclerViewCertificate.setAdapter(pendingRequestAdapter);
                        recyclerViewCertificate.setLayoutManager(linearLayoutManager);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }

    }
//    public void getbaby(){
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Customers");
//        Query query = databaseReference.orderByChild("Babies/baby_id");
//
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    if (dataSnapshot.exists()) {
//                        Customer customer = dataSnapshot.getValue(Customer.class);
//                        customer.getBaby().setBaby_id(dataSnapshot.getKey());
//                        if (removeDiacritics(vaccinationCertificate.getBaby().getBaby_name().toLowerCase()).contains(removeDiacritics(searchTerm.toLowerCase()))){
//                            mlistchild.add(vaccinationCertificate.getBaby());
//                            Log.i("siuuuu", "onDataChange: " + vaccinationCertificate.getBaby());
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
}