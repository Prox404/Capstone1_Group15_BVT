package com.prox.babyvaccinationtracker;

import android.content.Context;
import android.content.Intent;
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
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.prox.babyvaccinationtracker.adapter.PendingRequestAdapter;
import com.prox.babyvaccinationtracker.adapter.RegistrationTimelineAdapter;
import com.prox.babyvaccinationtracker.model.Vaccination_Registration;

import java.util.ArrayList;
import java.util.List;

public class ScheduleFragment extends Fragment {

    LinearLayout pendingInjection, scheduleInjection,timelineContainer;
    RecyclerView recyclerViewTimeline;
    Context context;
    List<Vaccination_Registration> vaccination_registrations = new ArrayList<>();


    public ScheduleFragment() {
        // Required empty public constructor
    }


    public static ScheduleFragment newInstance(String param1, String param2) {
        ScheduleFragment fragment = new ScheduleFragment();
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
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        context = container != null ? container.getContext() : null;
        scheduleInjection = view.findViewById(R.id.scheduleInjection);
        pendingInjection = view.findViewById(R.id.pendingInjection);
        recyclerViewTimeline = view.findViewById(R.id.recyclerViewTimeline);
        timelineContainer = view.findViewById(R.id.timelineContainer);

        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String id_vaccine_center = sharedPreferences.getString("customer_id","");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Vaccination_Registration");
        Query query = databaseReference.orderByChild("cus/customer_id").equalTo(id_vaccine_center);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    vaccination_registrations.clear();
                    Log.i("Pending", "onDataChange: " + snapshot.getChildrenCount());
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Vaccination_Registration vaccination_registration = dataSnapshot.getValue(Vaccination_Registration.class);
                        if(vaccination_registration.getStatus() == 0){
                            vaccination_registration.setRegist_id(dataSnapshot.getKey());
                            vaccination_registrations.add(vaccination_registration);
                        }
                    }
                    Log.i("Pending", "onDataChange: " + vaccination_registrations.size());
                    if(vaccination_registrations.size() == 0){
                        timelineContainer.setVisibility(View.GONE);
                    }else {
                        timelineContainer.setVisibility(View.VISIBLE);
                    }
                    RegistrationTimelineAdapter pendingRequestAdapter = new RegistrationTimelineAdapter( getContext() , vaccination_registrations);
                    recyclerViewTimeline.setAdapter(pendingRequestAdapter);
                    recyclerViewTimeline.setLayoutManager(new LinearLayoutManager(context));
                }else {
                    timelineContainer.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        scheduleInjection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Schedule_an_injection.class);
                startActivity(intent);
            }
        });

        pendingInjection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InjectionActivity.class);
                startActivity(intent);
            }
        });

        return  view;
    }
}