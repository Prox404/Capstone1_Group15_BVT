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
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prox.babyvaccinationtracker.adapter.TimeLineAdapter;
import com.prox.babyvaccinationtracker.model.Baby;
import com.prox.babyvaccinationtracker.model.Regimen;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DashboardFragment extends Fragment {

    Context context;
    RecyclerView recyclerViewTimeline;
    TimeLineAdapter timeLineAdapter;
    LinearLayout injectionHistory, familyContainer;
    List<Regimen> regimenList = new ArrayList<>();

    LinearLayout babyListContainer;
    ArrayList<Baby> babies = new ArrayList<>();

    String babyId = "";
    String firstBabyId = "";


    public DashboardFragment() {
        // Required empty public constructor
    }

    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
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
        context = container != null ? container.getContext() : null;
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        recyclerViewTimeline = view.findViewById(R.id.recyclerViewTimeline);
        injectionHistory = view.findViewById(R.id.injectionHistory);
        babyListContainer = view.findViewById(R.id.babyListContainer);
        familyContainer = view.findViewById(R.id.familyContainer);

        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String babiesList = sharedPreferences.getString("babiesList", "");
        String babyID = "";
        try {
            Gson gson = new Gson();
            babies = gson.fromJson(babiesList, new TypeToken<List<Baby>>() {}.getType());
            firstBabyId = babies.get(0).getBaby_id();
            setTimeLine(firstBabyId);
            Log.i("Aaaa", "onCreateView: " + babies.toString());
            for (Baby baby : babies) {
                addButtonForBaby(baby);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        injectionHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, InjectionHistoryActivity.class);
                startActivity(i);
            }
        });

        familyContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, FamilyActivity.class);
                startActivity(i);
            }
        });

        return view;
    }

    private void addButtonForBaby(final Baby baby) {
        Button button = new Button(context);
        button.setBackground(context.getResources().getDrawable(R.drawable.button_bg));
        button.setText(baby.getBaby_name());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                babyId = baby.getBaby_id();
                setTimeLine(babyId);
            }
        });

        babyListContainer.addView(button);
    }

    public void setTimeLine(String baby_id) {
        DatabaseReference vaccinationRegimenReference = FirebaseDatabase.getInstance().getReference("vaccination_regimen").child(baby_id);
        vaccinationRegimenReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                regimenList.clear();
                Date closestDate = null;
                for (DataSnapshot regimenSnapshot : snapshot.getChildren()) {
                    Regimen regimen = regimenSnapshot.getValue(Regimen.class);
                    regimenList.add(regimen);
                }
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                timeLineAdapter = new TimeLineAdapter(context, regimenList);
                linearLayoutManager.scrollToPositionWithOffset(timeLineAdapter.highlightedPosition, 0);
                recyclerViewTimeline.setAdapter(timeLineAdapter);
                recyclerViewTimeline.setLayoutManager(linearLayoutManager);
                Log.i("Home", "onDataChange: " + regimenList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}