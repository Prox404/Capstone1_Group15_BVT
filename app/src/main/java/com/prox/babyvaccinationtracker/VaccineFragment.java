package com.prox.babyvaccinationtracker;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class VaccineFragment extends Fragment {

    LinearLayout searchVaccine, searchVaccineCenter;

    public VaccineFragment() {
        // Required empty public constructor
    }

    public static VaccineFragment newInstance(String param1, String param2) {
        VaccineFragment fragment = new VaccineFragment();
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
        View view = inflater.inflate(R.layout.fragment_vaccine, container, false);
        searchVaccine = view.findViewById(R.id.searchVaccine);
        searchVaccineCenter = view.findViewById(R.id.searchVaccineCenter);

        searchVaccine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), search_vaccination.class);
                startActivity(intent);
            }
        });

        searchVaccineCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), search_vaccination_center.class);
//                startActivity(intent);
            }
        });
        return  view;
    }
}