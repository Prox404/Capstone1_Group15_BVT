package com.prox.babyvaccinationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {

    LinearLayout registionContainer;
    TextView textViewNumberOfRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        registionContainer = findViewById(R.id.registionContainer);
        textViewNumberOfRegistration = findViewById(R.id.textViewNumberOfRegistration);
        getNumberOfRegistration();

        registionContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, RegistrationRequestActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getNumberOfRegistration() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Vaccination_Registration");
        Query query = databaseReference.orderByChild("status").equalTo(0);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String numberOfRegistration = snapshot.getChildrenCount() + "";
                Log.i("Home", "onDataChange: " + numberOfRegistration);
                textViewNumberOfRegistration.setText(numberOfRegistration);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}