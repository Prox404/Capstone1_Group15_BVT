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
                    Toast.makeText(heatlth_input_for_babies.this,"Phải nhập chiều cao",Toast.LENGTH_LONG).show();
                    return;
                }
                String weight = health_input_weight.getText().toString();
                if(weight.isEmpty()){
                    Toast.makeText(heatlth_input_for_babies.this,"Phải nhập cân nặng",Toast.LENGTH_LONG).show();
                    return;
                }
                String sleep = health_input_sleep.getText().toString();
                if(sleep.isEmpty()){
                    Toast.makeText(heatlth_input_for_babies.this,"Phải nhập giờ ngủ",Toast.LENGTH_LONG).show();
                    return;
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

                databaseReference.child(babychoose.getBaby_id()).setValue(health); // hiện tại

                DatabaseReference reference = firebaseDatabase.getReference("health_history");

                int monthIndex = getMonthIndex(sl[1]);
                String referenceKey = sl[sl.length-1];
                reference.child(babychoose.getBaby_id()) // lưu vào lịch sử
                        .child(referenceKey)
                        .child(String.valueOf(monthIndex))
                        .setValue(health);


                //                databaseReference.child(babychoose.getBaby_id()).child("Jan").setValue(health);
//                databaseReference.child(babychoose.getBaby_id()).child("Feb").setValue(health);
//                databaseReference.child(babychoose.getBaby_id()).child("Mar").setValue(health);
//                databaseReference.child(babychoose.getBaby_id()).child("Apr").setValue(health);
//                databaseReference.child(babychoose.getBaby_id()).child("May").setValue(health);
//                databaseReference.child(babychoose.getBaby_id()).child("Jun").setValue(health);
//                databaseReference.child(babychoose.getBaby_id()).child("Jul").setValue(health);
//                databaseReference.child(babychoose.getBaby_id()).child("Aug").setValue(health);
//                databaseReference.child(babychoose.getBaby_id()).child("Sep").setValue(health);
//                databaseReference.child(babychoose.getBaby_id()).child("Oct").setValue(health);
//                databaseReference.child(babychoose.getBaby_id()).child("Nov").setValue(health);
//                databaseReference.child(babychoose.getBaby_id()).child("Dec").setValue(health);
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
        Button button = new Button(heatlth_input_for_babies.this);
        button.setText(baby.getBaby_name());
        health_input_button.addView(button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                babychoose = baby;
            }
        });
    }
}