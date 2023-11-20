package com.vaccinecenter.babyvaccinationtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class UserProfileActivity extends AppCompatActivity {

    TextView textViewUserName, textViewUserEmail, textViewUserPhone, textViewUserAddress, textViewUserWorkingTime;
    ImageView imageViewUser;
    Button buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        textViewUserName = findViewById(R.id.textViewUserName);
        textViewUserEmail = findViewById(R.id.textViewUserEmail);
        textViewUserPhone = findViewById(R.id.textViewUserPhone);
        textViewUserAddress = findViewById(R.id.textViewUserAddress);
        textViewUserWorkingTime = findViewById(R.id.textViewUserWorkingTime);
        imageViewUser = findViewById(R.id.imageViewUser);
        buttonLogout = findViewById(R.id.buttonLogout);

        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        String center_name = sharedPreferences.getString("center_name", "Trần Công Trí");
        String center_address = sharedPreferences.getString("center_address", "Tiên Phước, Quảng Nam");
        String center_email = sharedPreferences.getString("center_email", "prox@example.com");
        String center_image = sharedPreferences.getString("center_image", "");
        String hotline = sharedPreferences.getString("hotline", "012345678");
        String work_time = sharedPreferences.getString("work_time", "Cả Ngày");

        textViewUserName.setText(center_name);
        textViewUserEmail.setText(center_email);
        textViewUserPhone.setText(hotline);
        textViewUserAddress.setText(center_address);
        textViewUserWorkingTime.setText(work_time);
        if (!center_image.equals("")) {
            String imgaeUrl = center_image.contains("https") ? center_image : center_image.replace("http", "https");
            Picasso.get().load(imgaeUrl).into(imageViewUser);
        }

        buttonLogout.setOnClickListener(view -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            Intent intent = new Intent(UserProfileActivity.this, login_for_vaccine_center.class);
            startActivity(intent);
        });
    }
}