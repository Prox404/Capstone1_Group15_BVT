package com.prox.babyvaccinationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prox.babyvaccinationtracker.adapter.AddChatAdapter;
import com.prox.babyvaccinationtracker.adapter.VaccineCenterAdapter;
import com.prox.babyvaccinationtracker.model.Conversation;
import com.prox.babyvaccinationtracker.model.Vaccine_center;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class AddChatConversition extends AppCompatActivity {

    TextView edt_chat_search;
    TextView tv_chat_center_address;
    TextView tv_chat_display_all;
    RecyclerView rv_chat_list_vaccine_center;
    AddChatAdapter adapter;
    ArrayList<Vaccine_center> vaccine_centers_origin = new ArrayList<>();
    String address = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_chat_conversition);

        edt_chat_search = findViewById(R.id.edt_chat_search);
        tv_chat_center_address = findViewById(R.id.tv_chat_center_address);
        tv_chat_display_all = findViewById(R.id.tv_chat_display_all);
        rv_chat_list_vaccine_center = findViewById(R.id.rv_chat_list_vaccine_center);

        Intent intent = getIntent();
        if(intent!= null){
            address = intent.getStringExtra("cus_address");
            tv_chat_center_address.setText(address);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AddChatConversition.this);
        rv_chat_list_vaccine_center.setLayoutManager(linearLayoutManager);
        adapter = new AddChatAdapter(AddChatConversition.this, vaccine_centers_origin);
        rv_chat_list_vaccine_center.setAdapter(adapter);

        GetDataOnFireBase_vaccine();


        tv_chat_display_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.setCenters(vaccine_centers_origin);
            }
        });

        edt_chat_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String name = editable.toString();
                search_vaccines(name);
            }
        });
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child("Vaccine_center");;
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                vaccine_centers.clear();
//                if(snapshot!=null){
//                    for(DataSnapshot a : snapshot.getChildren()){
//                        Vaccine_center center = a.getValue(Vaccine_center.class);
//                        center.setCenter_id(a.getKey());
//                        vaccine_centers.add(center);
//                    }
//                    adapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

    }

    public static String removeDiacritics(String input) {
        String nfdNormalizedString = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }
    private void search_vaccines(String searchText){
        ArrayList<Vaccine_center> matchingCenters = new ArrayList<>(); // tìm trung tâm vắc-xin
        if(!searchText.isEmpty()){
            matchingCenters.clear();
            for(Vaccine_center center : vaccine_centers_origin){
                if(removeDiacritics(center.getCenter_name().toLowerCase()).contains(removeDiacritics(searchText.toLowerCase()))){
                    matchingCenters.add(center);
                }
            }
            adapter.setCenters(matchingCenters);
        }
        else{
            adapter.setCenters(vaccine_centers_origin);
        }
    }
    private void GetDataOnFireBase_vaccine(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child("Vaccine_center");;
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String customerAddress = address;
                vaccine_centers_origin.clear();
                for (DataSnapshot vaccineSnapshot : snapshot.getChildren()) {
                    Vaccine_center center = vaccineSnapshot.getValue(Vaccine_center.class);
                    center.setCenter_id(vaccineSnapshot.getKey());
                    vaccine_centers_origin.add(center);
                }
                vaccine_centers_origin = sortCentersByRelativeDistance(customerAddress, vaccine_centers_origin);
                adapter.setCenters(vaccine_centers_origin);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private ArrayList<Vaccine_center> sortCentersByRelativeDistance(String customerAddress, ArrayList<Vaccine_center> vaccineCenters) {
        String[] customerAddressParts = customerAddress.split(", ");
        Map<Vaccine_center, Integer> centerMatchCountMap = new HashMap<>();

        for (Vaccine_center center : vaccineCenters) {
            String centerAddress = center.getCenter_address();
            String[] centerAddressParts = centerAddress.split(", ");

            int matchCount = 0;

            for (int i = 0; i < customerAddressParts.length; i++) {
                for (int j = 0; j < centerAddressParts.length; j++) {
                    if (customerAddressParts[i].equalsIgnoreCase(centerAddressParts[j])) {
                        matchCount++;
                        break;
                    }
                }
            }

            centerMatchCountMap.put(center, matchCount);
        }

        ArrayList<Vaccine_center> sortedCenters = new ArrayList<>(centerMatchCountMap.keySet());
        Collections.sort(sortedCenters, (center1, center2) -> Integer.compare(centerMatchCountMap.get(center2), centerMatchCountMap.get(center1)));

        return sortedCenters;
    }

}