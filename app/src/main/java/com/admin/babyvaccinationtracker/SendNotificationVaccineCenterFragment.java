package com.admin.babyvaccinationtracker;

import android.content.Context;
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
import com.google.firebase.database.ValueEventListener;
import com.admin.babyvaccinationtracker.Adapter.VaccineCenterAdapter;
import com.admin.babyvaccinationtracker.model.Vaccine_center;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SendNotificationVaccineCenterFragment extends Fragment {

    Context context;
    private RecyclerView recyclerViewVaccineCenter;
    List<Vaccine_center> vaccineCenterList = new ArrayList<>();
    List<Vaccine_center> vaccinefilterList = new ArrayList<>(vaccineCenterList);
    VaccineCenterAdapter adapter;

    public SendNotificationVaccineCenterFragment() {
        // Required empty public constructor
    }

    public static SendNotificationVaccineCenterFragment newInstance(String param1, String param2) {
        SendNotificationVaccineCenterFragment fragment = new SendNotificationVaccineCenterFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("FRAGMENT_2", "onResume");
        Log.i("Resume", "onResume: " + vaccineCenterList.toString());
        String vaccineCenterNameSearch = SendNotificationActivity.searchQuery;
        if (!vaccineCenterNameSearch.isEmpty()){
            vaccinefilterList.clear();
            for (Vaccine_center vaccine_center : vaccineCenterList){
                if (removeDiacritics(vaccine_center.getCenter_name().toLowerCase()).contains(removeDiacritics(vaccineCenterNameSearch.toLowerCase()))){
                    Log.i("Filter", "onResume: " + vaccine_center.getCenter_name() + " " + vaccineCenterNameSearch);
                    vaccinefilterList.add(vaccine_center);
                }
            }
            updateUI();
        }else {
            Log.i("SendNoti", "onResume: Empty");
            vaccinefilterList = new ArrayList<>(vaccineCenterList);
            updateUI();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("FRAGMENT_2", "onCreate ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.context = container.getContext() != null ? container.getContext() : null;
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_send_notification_vaccine_center, container, false);

        recyclerViewVaccineCenter = view.findViewById(R.id.recyclerViewVaccineCenter);
        recyclerViewVaccineCenter.setLayoutManager(new LinearLayoutManager(context));

        DatabaseReference vaccineCenterRef = FirebaseDatabase.getInstance().getReference().child("Vaccine_centers");
        vaccineCenterRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                vaccineCenterList.clear();
                for (DataSnapshot vaccineCenterSnapshot : dataSnapshot.getChildren()) {
//                    Log.i("VaccineCenter", "onDataChange" + vaccineCenterSnapshot.toString());
                    if (vaccineCenterSnapshot.child("center_name").exists()) {
                        Log.i("VaccineCenter", "onDataChange: " + vaccineCenterSnapshot.toString());
                        Vaccine_center vaccine_center = vaccineCenterSnapshot.getValue(Vaccine_center.class);
                        vaccine_center.setCenter_id(vaccineCenterSnapshot.getKey());
                        vaccineCenterList.add(vaccine_center);
                    }
                }
                vaccinefilterList = new ArrayList<>(vaccineCenterList);
                Log.i("VaccineCenter", "onDataChange: " + vaccineCenterList.toString());
                updateUI(); // Thông báo rằng dữ liệu đã thay đổi
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi (nếu có)
            }
        });

        return  view;
    }

    private void updateUI() {
        if (getActivity() != null) {
            adapter = new VaccineCenterAdapter(vaccinefilterList);
            recyclerViewVaccineCenter.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    public void updateFilter(String searchQuery) {
        if (!searchQuery.isEmpty()) {
            vaccinefilterList.clear();
            Log.i("Filter", "updateFilter: " + vaccineCenterList.toString());
            for (Vaccine_center vaccine_center : vaccineCenterList) {
                Log.i("Filter", "updateFilter: " + vaccine_center.getCenter_name().toLowerCase() + " " + searchQuery.toLowerCase());
                if (removeDiacritics(vaccine_center.getCenter_name().toLowerCase()).contains(removeDiacritics(searchQuery.toLowerCase()))) {
                    vaccinefilterList.add(vaccine_center);
                }
            }
            updateUI();
        } else {
            vaccinefilterList.clear(); // Đảm bảo rằng vaccinefilterList trống khi không có tìm kiếm
            vaccinefilterList = new ArrayList<>(vaccineCenterList);
            updateUI();
        }
    }

    public static String removeDiacritics(String input) {
        String nfdNormalizedString = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }
}