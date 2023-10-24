package com.prox.babyvaccinationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.prox.babyvaccinationtracker.adapter.CompletedRequestAdapter;
import com.prox.babyvaccinationtracker.model.Vaccination_Registration;

import java.util.ArrayList;
import java.util.List;

public class InjectionHistoryActivity extends AppCompatActivity {

    RecyclerView recycleViewCompletedRequest;
    View emptyLayout;

    List<Vaccination_Registration> vaccination_registrations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_injection_history);

        recycleViewCompletedRequest = findViewById(R.id.recycleViewCompletedRequest);
        emptyLayout = findViewById(R.id.emptyLayout);

        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        String cus_id = sharedPreferences.getString("customer_id", "");
        Log.i("User", "onCreate: " + cus_id);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Vaccination_Registration");
        Query query = databaseReference.orderByChild("cus/customer_id").equalTo(cus_id);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                vaccination_registrations.clear();
                Log.i("Completed", "onDataChange: " + snapshot.getChildrenCount());
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Vaccination_Registration vaccination_registration = dataSnapshot.getValue(Vaccination_Registration.class);
                    assert vaccination_registration != null;
                    vaccination_registration.setRegist_id(dataSnapshot.getKey());
                    if (vaccination_registration.getStatus() == 3){
                        vaccination_registrations.add(vaccination_registration);
                    }

                }
                Log.i("Completed", "onDataChange: " + vaccination_registrations.size());
                if (vaccination_registrations.size() == 0){
                    emptyLayout.setVisibility(View.VISIBLE);
                }
                CompletedRequestAdapter pendingRequestAdapter = new CompletedRequestAdapter( InjectionHistoryActivity.this , vaccination_registrations);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(InjectionHistoryActivity.this);
                recycleViewCompletedRequest.setAdapter(pendingRequestAdapter);
                recycleViewCompletedRequest.setLayoutManager(linearLayoutManager);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}