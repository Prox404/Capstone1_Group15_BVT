package com.prox.babyvaccinationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prox.babyvaccinationtracker.adapter.TimeLineAdapter;
import com.prox.babyvaccinationtracker.model.Regimen;
import com.prox.babyvaccinationtracker.service.NotificationService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerViewTimeline;
    TimeLineAdapter timeLineAdapter;
    List<Regimen> regimenList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.homeToolBar);
        recyclerViewTimeline = findViewById(R.id.recyclerViewTimeline);
        setSupportActionBar(toolbar);

        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        String babiesList = sharedPreferences.getString("babiesList", "");
        String babyID = "";
        try {
            JSONArray jsonArray = new JSONArray(babiesList);
            if (jsonArray.length() > 0) {
                JSONObject firstBaby = jsonArray.getJSONObject(0);
                 babyID = firstBaby.getString("baby_id");
                Log.i("Home", "onCreate: " + babyID);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        DatabaseReference vaccinationRegimenReference = FirebaseDatabase.getInstance().getReference("vaccination_regimen").child(babyID);
        vaccinationRegimenReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                regimenList.clear();
                for (DataSnapshot regimenSnapshot : snapshot.getChildren()) {
                    Regimen regimen = regimenSnapshot.getValue(Regimen.class);
                    regimenList.add(regimen);
                }
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HomeActivity.this);
                timeLineAdapter = new TimeLineAdapter(HomeActivity.this, regimenList);
                linearLayoutManager.scrollToPositionWithOffset(timeLineAdapter.highlightedPosition, 0);
                recyclerViewTimeline.setAdapter(timeLineAdapter);
                recyclerViewTimeline.setLayoutManager(linearLayoutManager);
                Log.i("Home", "onDataChange: " + regimenList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Intent notificationService = new Intent(HomeActivity.this, NotificationService.class);
        startService(notificationService);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_notification) {
            // Xử lý sự kiện khi người dùng nhấn vào biểu tượng thông báo ở đây
            // Ví dụ: mở màn hình thông báo, hiển thị danh sách thông báo, vv.
            Log.i("Home", "onOptionsItemSelected: notification" );
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}