package com.admin.babyvaccinationtracker;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class activity_report extends AppCompatActivity {
    private TabLayout mTablayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
    }
}