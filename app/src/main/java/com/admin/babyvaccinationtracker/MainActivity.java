package com.admin.babyvaccinationtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.cloudinary.android.MediaManager;
import com.google.firebase.FirebaseApp;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Map config = new HashMap();
    private void configCloudinary() {
        config.put("cloud_name", "daahr9bmg");
        config.put("api_key", "166289647394352");
        config.put("api_secret", "g_WMi7qAWJV3HipXdZ7CKWgcjjc");
        MediaManager.init(this, config);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        configCloudinary();
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}