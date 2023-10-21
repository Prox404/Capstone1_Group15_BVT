package com.prox.babyvaccinationtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.cloudinary.android.MediaManager;
import com.google.firebase.FirebaseApp;
import com.prox.babyvaccinationtracker.service.NotificationService;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    Map config = new HashMap();
    final int MY_FOREGROUND_SERVICE_PERMISSION_REQUEST_CODE = 1;
    final int MY_BOOT_COMPLETED_PERMISSION_REQUEST_CODE = 2;
    private void configCloudinary() {
        config.put("cloud_name", "du42cexqi");
        config.put("api_key", "346965553513552");
        config.put("api_secret", "SguEwSEbwQNgOgHRTkyxeuG-478");
        MediaManager.init(this, config);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        configCloudinary();

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.FOREGROUND_SERVICE) == PackageManager.PERMISSION_GRANTED) {
            Log.i("main", "FOREGROUND_SERVICE Permission granted");
        } else {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.FOREGROUND_SERVICE}, MY_FOREGROUND_SERVICE_PERMISSION_REQUEST_CODE);
        }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_BOOT_COMPLETED) == PackageManager.PERMISSION_GRANTED) {
            Log.i("main", "RECEIVE_BOOT_COMPLETED Permission granted");
        } else {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_BOOT_COMPLETED}, MY_FOREGROUND_SERVICE_PERMISSION_REQUEST_CODE);
        }

        Intent i = new Intent(MainActivity.this, AuthActivity.class);
        startActivity(i);
    }
}