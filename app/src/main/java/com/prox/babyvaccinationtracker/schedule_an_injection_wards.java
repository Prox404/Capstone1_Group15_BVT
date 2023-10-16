package com.prox.babyvaccinationtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.prox.babyvaccinationtracker.adapter.WardAdapter;
import com.prox.babyvaccinationtracker.api.GeoApi;
import com.prox.babyvaccinationtracker.geo.district;
import com.prox.babyvaccinationtracker.geo.ward;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class schedule_an_injection_wards extends AppCompatActivity {
    ImageView scheule_image_ward_back;
    ListView schedule_list_view_ward;

    WardAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_an_injection_wards);
        scheule_image_ward_back = findViewById(R.id.scheule_image_ward_back);
        scheule_image_ward_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        schedule_list_view_ward = findViewById(R.id.schedule_list_view_ward);
        Intent i = getIntent();
        int code = i.getIntExtra("districtCODE",0);
        GeoApi.geoapi.all_wards(code).enqueue(new Callback<district>() {
            @Override
            public void onResponse(Call<district> call, Response<district> response) {
                if(response.isSuccessful()){
                    district d = response.body();
                    ArrayList<ward> wards = d.getWards();
                    adapter = new WardAdapter(schedule_an_injection_wards.this, wards);
                    schedule_list_view_ward.setAdapter(adapter);

                    schedule_list_view_ward.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            ward w = (ward) adapterView.getItemAtPosition(i);
                            Intent intent = new Intent();
                            intent.putExtra("wardNAME", w.getName());
                            setResult(RESULT_OK,intent);
                            finish();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<district> call, Throwable t) {

            }
        });
    }
}