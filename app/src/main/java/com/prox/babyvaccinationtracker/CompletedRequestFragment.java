package com.prox.babyvaccinationtracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.prox.babyvaccinationtracker.Adapter.CompletedRequestAdapter;
import com.prox.babyvaccinationtracker.Adapter.PendingRequestAdapter;
import com.prox.babyvaccinationtracker.model.Vaccination_Registration;

import java.util.ArrayList;
import java.util.List;


public class CompletedRequestFragment extends Fragment {

    RecyclerView recycleViewCompletedRequest;

    List<Vaccination_Registration> vaccination_registrations = new ArrayList<>();

    Context context;


    public CompletedRequestFragment() {
        // Required empty public constructor
    }

    public static CompletedRequestFragment newInstance(String param1, String param2) {
        CompletedRequestFragment fragment = new CompletedRequestFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_completed_request, container, false);
        recycleViewCompletedRequest = view.findViewById(R.id.recycleViewCompletedRequest);

        context = container != null ? container.getContext() : null;
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String id_vaccine_center = sharedPreferences.getString("center_id","");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Vaccination_Registration");
        Query query = databaseReference.orderByChild("center/center_id").equalTo(id_vaccine_center);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                vaccination_registrations.clear();
                Log.i("Pending", "onDataChange: " + snapshot.getChildrenCount());
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Vaccination_Registration vaccination_registration = dataSnapshot.getValue(Vaccination_Registration.class);
                    if(vaccination_registration.getStatus() == 3){
                        vaccination_registration.setRegister_id(dataSnapshot.getKey());
                        vaccination_registrations.add(vaccination_registration);
                    }
                }
                Log.i("Pending", "onDataChange: " + vaccination_registrations.size());
                CompletedRequestAdapter pendingRequestAdapter = new CompletedRequestAdapter( getContext() , vaccination_registrations);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                recycleViewCompletedRequest.setAdapter(pendingRequestAdapter);
                recycleViewCompletedRequest.setLayoutManager(linearLayoutManager);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return  view;
    }
}