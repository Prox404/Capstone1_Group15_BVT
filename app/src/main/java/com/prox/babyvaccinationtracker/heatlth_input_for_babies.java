package com.prox.babyvaccinationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prox.babyvaccinationtracker.model.Baby;
import com.prox.babyvaccinationtracker.model.Health;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class heatlth_input_for_babies extends AppCompatActivity {
    LinearLayout health_input_button;
    EditText health_input_height,health_input_weight,health_input_sleep;
    Button health_input_complete;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Baby babychoose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heatlth_input_for_babies);
        // ánh xạ
        health_input_button = findViewById(R.id.health_input_button);
        health_input_height = findViewById(R.id.health_input_height);
        health_input_weight = findViewById(R.id.health_input_weight);
        health_input_sleep = findViewById(R.id.health_input_sleep);
        health_input_complete = findViewById(R.id.health_input_complete);
        // Lấy dữ liệu baby
        SharedPreferences sharedPreferences = heatlth_input_for_babies.this.getSharedPreferences("user", Context.MODE_PRIVATE);
        String babiesJson = sharedPreferences.getString("babiesList", "");
        Gson gson = new Gson();
        Type type = new TypeToken<List<Baby>>() {}.getType();
        List<Baby> babiesList = gson.fromJson(babiesJson, type);
        babychoose = babiesList.get(0);
        for (Baby baby : babiesList) {
            addButtonForBaby(baby);
            Log.i("Baby",""+baby);
        }
        // firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("health");


        health_input_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String height = health_input_height.getText().toString();
                if(height.isEmpty()){
                    health_input_height.setError("Phải nhập chiều cao");
                    return;
                }else {
                    health_input_height.setError(null);
                }
                String weight = health_input_weight.getText().toString();
                if(weight.isEmpty()){
                    health_input_weight.setError("Phải nhập cân nặng");
                    return;
                }else {
                    health_input_weight.setError(null);
                }
                String sleep = health_input_sleep.getText().toString();
                if(sleep.isEmpty()){
                    Toast.makeText(heatlth_input_for_babies.this,"Phải nhập giờ ngủ",Toast.LENGTH_LONG).show();
                    return;
                }else {
                    health_input_sleep.setError(null);
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                Date now = new Date();
                String formattedDate = dateFormat.format(now);
                String[] sl = formattedDate.split(" ");
                Log.i("Dateeeeeeeeeeeeeeee",""+formattedDate+" "+sl[1]);
                Health health = new Health();
                health.setHealthCreated_at(formattedDate);
                health.setHeight(Double.parseDouble(height));
                health.setWeight(Double.parseDouble(weight));
                health.setSleep(Double.parseDouble(sleep));

                if (health.getHeight() < 0 || health.getHeight() > 200) {
                    Toast.makeText(heatlth_input_for_babies.this, "Chiều cao không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (health.getWeight() < 0 || health.getWeight() > 200) {
                    Toast.makeText(heatlth_input_for_babies.this, "Cân nặng không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (health.getSleep() < 0 || health.getSleep() > 24) {
                    Toast.makeText(heatlth_input_for_babies.this, "Giờ ngủ không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }

                int monthIndex = getMonthIndex(sl[1]);
                String referenceKey = sl[sl.length-1];
                databaseReference.child(babychoose.getBaby_id()) // lưu vào lịch sử
                        .child(""+referenceKey)
                        .child(String.valueOf(monthIndex))
                        .setValue(health);
                finish();
            }
        });
    }
    private int getMonthIndex(String monthAbbreviation) {
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        for (int i = 0; i < months.length; i++) {
            if (months[i].equals(monthAbbreviation)) {
                return i;
            }
        }
        return -1; // Return -1 for an unknown month
    }
    private void addButtonForBaby(final Baby baby){
        Button button = new Button(this);
        button.setText(baby.getBaby_name());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        params.setMargins(0, 0, 15, 0);
        button.setLayoutParams(params);
        button.setElevation(0);
        button.setPadding(20, 5, 20, 5);
        button.setHeight(30);
        button.setMinimumHeight(130);
        button.setMinHeight(0);
        button.setStateListAnimator(null);

        // Nếu babyListContainer chưa có Button nào, hoặc Button đầu tiên được thêm vào
        if (health_input_button.getChildCount() == 0) {
            // Thiết lập background cho Button đầu tiên là color/primaryColor
            button.setBackground(getResources().getDrawable(R.drawable.rounded_primary_button_bg));
            button.setTextColor(getResources().getColor(R.color.white));

            babychoose = baby;
        } else {
            // Thiết lập background mặc định cho tất cả các Button khác
            button.setBackground(getResources().getDrawable(R.drawable.rounded_white_button_bg));
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                babychoose = baby;

                resetButtonBackgrounds();

                button.setBackground(getResources().getDrawable(R.drawable.rounded_primary_button_bg));
                button.setTextColor(getResources().getColor(R.color.white));
            }
        });

        health_input_button.addView(button);
    }
    private void resetButtonBackgrounds() {
        // Lặp qua tất cả các Button trong babyListContainer và đặt background về màu trắng
        for (int i = 0; i < health_input_button.getChildCount(); i++) {
            View child = health_input_button.getChildAt(i);
            if (child instanceof Button) {
                ((Button) child).setBackground(getResources().getDrawable(R.drawable.rounded_white_button_bg));
                ((Button) child).setTextColor(getResources().getColor(R.color.textColor));
            }
        }
    }
}