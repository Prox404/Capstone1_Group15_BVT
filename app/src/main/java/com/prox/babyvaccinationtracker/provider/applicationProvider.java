package com.prox.babyvaccinationtracker.provider;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class applicationProvider extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
