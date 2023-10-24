package com.prox.babyvaccinationtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.prox.babyvaccinationtracker.adapter.DistrictAdapter;
import com.prox.babyvaccinationtracker.api.GeoApi;
import com.prox.babyvaccinationtracker.geo.district;
import com.prox.babyvaccinationtracker.geo.province;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class schedule_an_injection_districts extends AppCompatActivity {
    ImageView schedule_btn_back_districts;
    ListView schedule_list_view_districts;
    DistrictAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_an_injection_districts);

        schedule_btn_back_districts = findViewById(R.id.schedule_btn_back_districts);
        schedule_list_view_districts = findViewById(R.id.schedule_list_view_districts);
        schedule_btn_back_districts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Intent i = getIntent();
        int code = i.getIntExtra("provinceCODE",0);

        GeoApi.geoapi.all_districts(code).enqueue(new Callback<province>() {
            @Override
            public void onResponse(Call<province> call, Response<province> response) {
                if(response.isSuccessful()){
                    province p = response.body();
                    ArrayList<district> d = p.getDistricts();
                    adapter = new DistrictAdapter(schedule_an_injection_districts.this,d);
                    schedule_list_view_districts.setAdapter(adapter);

                    schedule_list_view_districts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            district d = (district) adapterView.getItemAtPosition(i);
                            Intent intent = new Intent();
                            intent.putExtra("districtNAME", d.getName());
                            intent.putExtra("districtCODE", d.getCode());
                            setResult(RESULT_OK,intent);
                            finish();
                        }
                    });
                }

            }

            @Override
            public void onFailure(Call<province> call, Throwable t) {
                Toast.makeText(schedule_an_injection_districts.this, "onFailure", Toast.LENGTH_LONG).show();
            }
        });
    }
}