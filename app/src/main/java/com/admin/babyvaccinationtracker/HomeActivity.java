package com.admin.babyvaccinationtracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    ImageView imageView_Menu, imageView_Search,imageView_Notification, imageView_Account, imageView_article_management,
            imageView_user_management, imageView_report, imageView_health_monitoring, imageView_edit, imageView_home,
            imageView_article, imageView_user, imageView_setting, imageView_send_notification;

    EditText editText_Search;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        imageView_Menu = findViewById(R.id.imageView_Menu);
        imageView_Search = findViewById(R.id.imageView_Search);
        imageView_Notification = findViewById(R.id.imageView_Notification);
        imageView_Account = findViewById(R.id.imageView_Account);
        imageView_article_management = findViewById(R.id.imageView_article_management);
        imageView_user_management = findViewById(R.id.imageView_user_management);
        imageView_report = findViewById(R.id.imageView_report);
        imageView_health_monitoring = findViewById(R.id.imageView_health_monitoring);
        imageView_edit = findViewById(R.id.imageView_edit);
        imageView_home = findViewById(R.id.imageView_home);
        imageView_article = findViewById(R.id.imageView_article);
        imageView_user = findViewById(R.id.imageView_user);
        imageView_setting = findViewById(R.id.imageView_setting);
        editText_Search = findViewById(R.id.editText_Search);
        imageView_send_notification = findViewById(R.id.imageView_send_notification);

       imageView_send_notification.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(HomeActivity.this, SendNotificationActivity.class);
               startActivity(intent);
           }
       });

       imageView_user_management.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(HomeActivity.this, Admin_management_of_vaccine_center_registration.class);
               startActivity(intent);
           }
       });

        imageView_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, activity_report.class));
            }
        });
       
    }
}
