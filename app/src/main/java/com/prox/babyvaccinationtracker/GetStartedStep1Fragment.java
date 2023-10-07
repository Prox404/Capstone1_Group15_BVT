package com.prox.babyvaccinationtracker;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GetStartedStep1Fragment extends Fragment {

    Context context;
    public GetStartedStep1Fragment() {
        // Required empty public constructor
    }

    public static GetStartedStep1Fragment newInstance(String param1, String param2) {
        GetStartedStep1Fragment fragment = new GetStartedStep1Fragment();
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
        this.context = container != null ? container.getContext() : null;

        View view =  inflater.inflate(R.layout.fragment_get_started_step1, container, false);


        return view;
    }
}