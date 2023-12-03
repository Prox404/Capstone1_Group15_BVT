package com.prox.babyvaccinationtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.Manifest;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class UserProfileActivity extends AppCompatActivity {

    TextView textViewUserName, textViewUserEmail, textViewUserPhone, textViewUserAddress, textViewUserEthnicity, textViewUserBirthday;
    Button buttonLogout;
    ImageView imageViewUser, imageViewGender;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch switchSyncVaccinationCalendar, switchSyncReminderCalendar;

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
        switchSyncVaccinationCalendar = findViewById(R.id.switchSyncVaccinationCalendar);
        switchSyncReminderCalendar = findViewById(R.id.switchSyncReminderCalendar);

        SharedPreferences settingsPreferences = getSharedPreferences("settings", MODE_PRIVATE);

        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        String cus_avatar = sharedPreferences.getString("cus_avatar", "");
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

        String imgaeUrl = cus_avatar.contains("https") ? cus_avatar : cus_avatar.replace("http", "https");
        Picasso.get().load(imgaeUrl).into(imageViewUser);
        if (cus_gender.equals("Nữ")){
            imageViewGender.setImageResource(R.drawable.ic_female);
        }

        buttonLogout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            Intent intent = new Intent(UserProfileActivity.this, AuthActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        requestPermission();
        Boolean syncVaccinationCalendar = settingsPreferences.getBoolean("syncVaccinationCalendar", false);
        switchSyncVaccinationCalendar.setChecked(syncVaccinationCalendar);
        switchSyncVaccinationCalendar.setOnCheckedChangeListener((compoundButton, b) -> {
            SharedPreferences.Editor editor = settingsPreferences.edit();
            editor.putBoolean("syncVaccinationCalendar", b);
            editor.apply();

            if (b){
                CalendarSync.syncVaccineRegisterToCalendar(UserProfileActivity.this);
            }else {
                CalendarSync.removeAllVaccineRegisterCalendar(UserProfileActivity.this);
            }
        });

        Boolean syncReminderCalendar = settingsPreferences.getBoolean("syncReminderCalendar", false);
        switchSyncReminderCalendar.setChecked(syncReminderCalendar);
        switchSyncReminderCalendar.setOnCheckedChangeListener((compoundButton, b) -> {
            SharedPreferences.Editor editor = settingsPreferences.edit();
            editor.putBoolean("syncReminderCalendar", b);
            editor.apply();

            if (b){
                CalendarSync.syncReminderToCalendar(UserProfileActivity.this);
            }else {
                CalendarSync.removeAllReminderCalendar(UserProfileActivity.this);
            }
        });
    }

    private void requestPermission() {
        if(ContextCompat.checkSelfPermission( this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.READ_CALENDAR}, 1);
        }else{
            Log.i("Profile Activity", "requestPermission: " + "READ_CALENDAR PERMISSION GRANTED");
        }
        if(ContextCompat.checkSelfPermission( this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.WRITE_CALENDAR}, 2);
        }else{
            Log.i("Profile Activity", "requestPermission: " + "WRITE_CALENDAR PERMISSION GRANTED");
        }
    }
}