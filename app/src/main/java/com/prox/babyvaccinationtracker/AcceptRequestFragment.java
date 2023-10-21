package com.prox.babyvaccinationtracker;

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
import com.prox.babyvaccinationtracker.Adapter.AcceptRequestAdapter;
import com.prox.babyvaccinationtracker.model.Vaccination_Registration;

import java.util.ArrayList;
import java.util.List;

public class AcceptRequestFragment extends Fragment {

    RecyclerView recycleViewAcceptRequest;
    List<Vaccination_Registration> vaccination_registrations = new ArrayList<>();

    public AcceptRequestFragment() {
        // Required empty public constructor
    }


    public static AcceptRequestFragment newInstance(String param1, String param2) {
        AcceptRequestFragment fragment = new AcceptRequestFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
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
        View view = inflater.inflate(R.layout.fragment_accept_request, container, false);
        recycleViewAcceptRequest = view.findViewById(R.id.recycleViewAcceptRequest);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Vaccination_Registration");
        Query query = databaseReference.orderByChild("status").equalTo(2);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                vaccination_registrations.clear();
                Log.i("Canceled", "onDataChange: " + snapshot.getChildrenCount());
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Vaccination_Registration vaccination_registration = dataSnapshot.getValue(Vaccination_Registration.class);
                    vaccination_registration.setRegister_id(dataSnapshot.getKey());
                    vaccination_registrations.add(vaccination_registration);
                }
                Log.i("Canceled", "onDataChange: " + vaccination_registrations.size());
                AcceptRequestAdapter pendingRequestAdapter = new AcceptRequestAdapter( getContext() , vaccination_registrations);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                recycleViewAcceptRequest.setAdapter(pendingRequestAdapter);
                recycleViewAcceptRequest.setLayoutManager(linearLayoutManager);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
}