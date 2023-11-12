package com.prox.babyvaccinationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prox.babyvaccinationtracker.service.NotificationService;

import java.util.Objects;


public class HomeActivity extends AppCompatActivity {

    Toolbar toolbar;
    BottomNavigationView navigation;

    final int MY_FOREGROUND_SERVICE_PERMISSION_REQUEST_CODE = 1;
    final int MY_BOOT_COMPLETED_PERMISSION_REQUEST_CODE = 2;

    private void check(){
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("BlackList").child("Customers").child(id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    startActivity(new Intent(HomeActivity.this, DisplayBlockActivity.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        check();


        toolbar = (Toolbar) findViewById(R.id.homeToolBar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        navigation = findViewById(R.id.navigation);
        navigation.setOnItemSelectedListener(mOnNavigationItemSelectedListener);
//        navigation.setItemIconTintList(null);
        loadFragment(new DashboardFragment());

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.FOREGROUND_SERVICE) == PackageManager.PERMISSION_GRANTED) {
            Log.i("main", "FOREGROUND_SERVICE Permission granted");
        } else {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.FOREGROUND_SERVICE}, MY_FOREGROUND_SERVICE_PERMISSION_REQUEST_CODE);
        }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_BOOT_COMPLETED) == PackageManager.PERMISSION_GRANTED) {
            Log.i("main", "RECEIVE_BOOT_COMPLETED Permission granted");
        } else {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_BOOT_COMPLETED}, MY_FOREGROUND_SERVICE_PERMISSION_REQUEST_CODE);
        }

        Intent notificationService = new Intent(HomeActivity.this, NotificationService.class);
        startService(notificationService);
    }

    private final NavigationBarView.OnItemSelectedListener mOnNavigationItemSelectedListener
            = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Log.i("navigation", "call");
            Fragment fragment;
            int itemId = item.getItemId();
//            Log.i("navigation", toString(itemId));
            if (itemId == R.id.navigation_dashboard) {
                fragment = new DashboardFragment();
                loadFragment(fragment);
                return true;
            } else if (itemId == R.id.navigation_calendar) {
//                Intent i = new Intent(HomeActivity.this, Schedule_an_injection.class);
//                startActivity(i);
                fragment = new ScheduleFragment();
                loadFragment(fragment);
                return true;
            }else if (itemId == R.id.navigation_vaccine) {
                fragment = new VaccineFragment();
                loadFragment(fragment);
                return true;
            }else if (itemId == R.id.navigation_health) {
                fragment = new HealthFragment();
                loadFragment(fragment);
                return true;
            }else if (itemId == R.id.navigation_chat) {
                fragment = new ChatFragment();
                loadFragment(fragment);
                return true;
            } else {
                return false;
            }
        }
    };

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
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

    @Override
    public void onBackPressed() {
        // Xử lý sự kiện khi người dùng bấm nút Back
        // Ví dụ: Hiển thị hộp thoại xác nhận thoát chương trình, vv.
        Log.i("Home", "onBackPressed: ");
        moveTaskToBack(true);
    }
}