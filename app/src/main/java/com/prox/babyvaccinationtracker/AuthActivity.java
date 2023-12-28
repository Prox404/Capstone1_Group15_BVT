package com.prox.babyvaccinationtracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;


public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        String customer_id = sharedPreferences.getString("customer_id", "");
        String babies = sharedPreferences.getString("babiesList", "");
        Log.i("AuthActivity", "Babies onCreate: " + babies);
        if(!customer_id.equals("null") && !customer_id.equals("")){
            if (!babies.equals("null") && !babies.equals("") && !babies.equals("[]")) {
                Intent intent = new Intent(AuthActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Log.i("AuthActivity", "changeFragment: home");
            }else{
                Log.i("AuthActivity", "onCreate: " + "get started");
                Intent intent = new Intent(AuthActivity.this, GetStartedActivity.class);
                startActivity(intent);
            }
        }else{
            Log.i("AuthActivity", "onCreate: " + "login");
            loadFragment(new LoginFragment());
        }
    }

    public void loadFragment(@NonNull Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void changeFragment(String fragmentName) {
        Log.i("Auth activity", "changeFragment: " + fragmentName);
        switch (fragmentName){
            case "login":
                loadFragment(new LoginFragment());
                break;
            case "register":
                loadFragment(new RegisterFragment());
                break;
            case "forgotPassword":
                loadFragment(new ResetPasswordFragment());
                break;
            case "home":
                SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                String customer_id = sharedPreferences.getString("customer_id", null);
                String babies = sharedPreferences.getString("babiesList", null);
                if(customer_id != null){
//
                    if (babies != null) {
                        Intent intent = new Intent(AuthActivity.this, HomeActivity.class);
                        startActivity(intent);
                        Log.i("", "changeFragment: home");
                    }else{
                         Intent intent = new Intent(AuthActivity.this, GetStartedActivity.class);
                         startActivity(intent);
                    }
                }else{
                    loadFragment(new LoginFragment());
                }
            case "getStarted":
                Intent i = new Intent(AuthActivity.this, GetStartedActivity.class);
                startActivity(i);
                break;
            default:
                loadFragment(new LoginFragment());
                break;
        }
    }
}