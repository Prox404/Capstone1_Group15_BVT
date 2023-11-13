package com.admin.babyvaccinationtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class UserActivity extends AppCompatActivity {

    TextView textViewUserName, textViewUserEmail, textViewUserPhone;

    ImageView imageViewUser;

    Button buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        imageViewUser = findViewById(R.id.imageViewUser);
        textViewUserName = findViewById(R.id.textViewUserName);
        textViewUserEmail = findViewById(R.id.textViewUserEmail);
        textViewUserPhone = findViewById(R.id.textViewUserPhone);
        buttonLogout = findViewById(R.id.buttonLogout);

        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        String admin_name = sharedPreferences.getString("admin_name", "");
        String admin_email = sharedPreferences.getString("admin_email", "");
        String admin_phone = sharedPreferences.getString("admin_phone", "");
        String admin_avatar = sharedPreferences.getString("admin_avatar", "");

        textViewUserName.setText(admin_name);
        textViewUserEmail.setText(admin_email);
        textViewUserPhone.setText(admin_phone);

        String imgaeUrl = admin_avatar.contains("https") ? admin_avatar : admin_avatar.replace("http", "https");
        Picasso.get().load(imgaeUrl).into(imageViewUser);

        buttonLogout.setOnClickListener(view -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            Intent intent = new Intent(UserActivity.this, AuthActivity.class);
            startActivity(intent);
        });
    }
}