package com.prox.babyvaccinationtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class login_for_vaccine_center extends AppCompatActivity {
    EditText login_for_edt_email_vaccine_center,login_for_edt_password_vaccine_center;
    Button login_btn_login_for_vaccine_center;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_for_vaccine_center);
        login_for_edt_password_vaccine_center = findViewById(R.id.login_for_edt_password_vaccine_center);
        login_for_edt_email_vaccine_center = findViewById(R.id.login_for_edt_email_vaccine_center);
        login_btn_login_for_vaccine_center = findViewById(R.id.login_btn_login_for_vaccine_center);


    }
}