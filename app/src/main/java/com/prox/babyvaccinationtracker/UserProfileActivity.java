package com.prox.babyvaccinationtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class UserProfileActivity extends AppCompatActivity {

    TextView textViewUserName, textViewUserEmail, textViewUserPhone, textViewUserAddress, textViewUserEthnicity, textViewUserBirthday;
    Button buttonLogout;
    ImageView imageViewUser, imageViewGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        textViewUserName = findViewById(R.id.textViewUserName);
        textViewUserEmail = findViewById(R.id.textViewUserEmail);
        textViewUserPhone = findViewById(R.id.textViewUserPhone);
        textViewUserAddress = findViewById(R.id.textViewUserAddress);
        textViewUserEthnicity = findViewById(R.id.textViewUserEthnicity);
        textViewUserBirthday = findViewById(R.id.textViewUserBirthday);
        imageViewGender = findViewById(R.id.imageViewGender);
        imageViewUser = findViewById(R.id.imageViewUser);
        buttonLogout = findViewById(R.id.buttonLogout);

        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        //String cus_avatar = sharedPreferences.getString("cus_avatar", "");
        String cus_name = sharedPreferences.getString("cus_name", "Trần Công Trí");
        String cus_birthday = sharedPreferences.getString("cus_birthday", "03/11/2002");
        String cus_address = sharedPreferences.getString("cus_address", "Tiên Phước, Quảng Nam");
        String cus_phone = sharedPreferences.getString("cus_phone", "012345678");
        String cus_email = sharedPreferences.getString("cus_email", "prox@example.com");
        String cus_gender = sharedPreferences.getString("cus_gender", "Nam");
        String cus_ethnicity = sharedPreferences.getString("cus_ethnicity", "Kinh");

        textViewUserName.setText(cus_name);
        textViewUserEmail.setText(cus_email);
        textViewUserPhone.setText(cus_phone);
        textViewUserAddress.setText(cus_address);
        textViewUserEthnicity.setText(cus_ethnicity);
        textViewUserBirthday.setText(cus_birthday);

        //String imgaeUrl = cus_avatar.contains("https") ? cus_avatar : cus_avatar.replace("http", "https");
        //Picasso.get().load(imgaeUrl).into(imageViewUser);
        if (cus_gender.equals("Nữ")){
            imageViewGender.setImageResource(R.drawable.ic_female);
        }

        buttonLogout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            Intent intent = new Intent(UserProfileActivity.this, AuthActivity.class);
            startActivity(intent);
        });
    }
}