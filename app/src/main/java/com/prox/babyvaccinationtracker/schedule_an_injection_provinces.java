package com.prox.babyvaccinationtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.prox.babyvaccinationtracker.adapter.ProvinceAdapter;
import com.prox.babyvaccinationtracker.api.GeoApi;
import com.prox.babyvaccinationtracker.geo.province;
import com.prox.babyvaccinationtracker.model.Vaccine_center;
import com.prox.babyvaccinationtracker.model.Vaccines;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class schedule_an_injection_provinces extends AppCompatActivity {

    ImageView schedule_img_back_provinces;
    EditText schedule_edt_province;
    ListView schedule_listview_provinces;

    ArrayList<province> provinces = new ArrayList<>();
    ProvinceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_an_injection_provinces);

        schedule_img_back_provinces = findViewById(R.id.schedule_img_back_provinces);
        schedule_listview_provinces = findViewById(R.id.schedule_listview_provinces);
        schedule_img_back_provinces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        GeoApi.geoapi.all_provinces().enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    JsonArray jsonArray = response.body();
                    if (jsonArray != null) {

                        for (JsonElement element : jsonArray) {
                            provinces.add(new Gson().fromJson(element, province.class));
                        }

                        adapter = new ProvinceAdapter(schedule_an_injection_provinces.this, provinces);
                        schedule_listview_provinces.setAdapter(adapter);

                    }
                    schedule_listview_provinces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            province p = (province) adapterView.getItemAtPosition(i);

                            Intent intent = new Intent();
                            intent.putExtra("provinceCODE", p.getCode());
                            intent.putExtra("provinceNAME",p.getName());
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(schedule_an_injection_provinces.this, "onFailure", Toast.LENGTH_LONG).show();
            }
        });

    }
}