package com.prox.babyvaccinationtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prox.babyvaccinationtracker.adapter.BabyGridViewAdapter;
import com.prox.babyvaccinationtracker.model.Baby;

import java.util.ArrayList;
import java.util.List;

public class FamilyActivity extends AppCompatActivity {

    GridView gridViewBaby;
    ArrayList<Baby> babies = new ArrayList<>();

    BabyGridViewAdapter babyGridViewAdapter;

    LinearLayout addBabyContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family2);

        gridViewBaby = findViewById(R.id.gridViewBaby);
        addBabyContainer = findViewById(R.id.addBabyContainer);

        showBabyList();
        addBabyContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FamilyActivity.this, GetStartedActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        showBabyList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showBabyList();
    }

    public void showBabyList(){
        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        String babiesList = sharedPreferences.getString("babiesList", "");
        try {
            Gson gson = new Gson();
            babies = gson.fromJson(babiesList, new TypeToken<List<Baby>>() {}.getType());
//            Log.i("Aaaa", "onCreateView: " + babies.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        babyGridViewAdapter = new BabyGridViewAdapter(this, babies);
        gridViewBaby.setAdapter(babyGridViewAdapter);
    }
}