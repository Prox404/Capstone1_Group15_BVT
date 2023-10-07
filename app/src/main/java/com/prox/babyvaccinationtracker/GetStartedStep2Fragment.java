package com.prox.babyvaccinationtracker;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GetStartedStep2Fragment extends Fragment {

    public GetStartedStep2Fragment() {
        // Required empty public constructor
    }

    public static GetStartedStep2Fragment newInstance(String param1, String param2) {
        GetStartedStep2Fragment fragment = new GetStartedStep2Fragment();
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
        View view = inflater.inflate(R.layout.fragment_get_started_step2, container, false);
        return view;
    }
}