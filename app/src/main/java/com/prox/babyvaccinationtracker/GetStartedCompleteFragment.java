package com.prox.babyvaccinationtracker;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GetStartedCompleteFragment extends Fragment {


    public GetStartedCompleteFragment() {
        // Required empty public constructor
    }

    public static GetStartedCompleteFragment newInstance() {
        GetStartedCompleteFragment fragment = new GetStartedCompleteFragment();
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
        return inflater.inflate(R.layout.fragment_get_started_complete, container, false);
    }
}