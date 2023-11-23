package com.admin.babyvaccinationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;


public class AuthActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        String admin_id = sharedPreferences.getString("admin_id", "");
        Log.i("Auth", "onCreate: " + admin_id);
        if (admin_id.equals("")) {
            loadFragment(new LoginFragment());
        } else {
            Intent intent = new Intent(AuthActivity.this, HomeActivity.class);
            startActivity(intent);
        }
    }

    public void loadFragment(@NonNull Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null); // Để có thể quay lại Fragment trước đó
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
            default:
                loadFragment(new LoginFragment());
                break;
        }
    }
}