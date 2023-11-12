package com.prox.babyvaccinationtracker;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prox.babyvaccinationtracker.model.Vaccination_Registration;

import java.util.ArrayList;
import java.util.List;


public class WaitingForCertificationFragment extends Fragment {

    List<Vaccination_Registration> vaccination_registrations = new ArrayList<>();
    Context context;

    public WaitingForCertificationFragment() {
        // Required empty public constructor
    }

    public static WaitingForCertificationFragment newInstance(String param1, String param2) {
        WaitingForCertificationFragment fragment = new WaitingForCertificationFragment();
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
        View view = inflater.inflate(R.layout.fragment_waiting_for_certification, container, false);

        return view;
    }
}