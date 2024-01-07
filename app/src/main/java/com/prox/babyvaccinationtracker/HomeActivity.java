package com.prox.babyvaccinationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;


import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prox.babyvaccinationtracker.service.NotificationService;
import com.prox.babyvaccinationtracker.util.NetworkUtils;

import java.util.Objects;


public class HomeActivity extends AppCompatActivity {

    Toolbar toolbar;
    BottomNavigationView navigation;

    public static boolean NETWORK_STATE = true;

    final int MY_FOREGROUND_SERVICE_PERMISSION_REQUEST_CODE = 1;
    final int MY_BOOT_COMPLETED_PERMISSION_REQUEST_CODE = 2;

    public static DatabaseReference referenceCheck;
    public static ValueEventListener valueEventCheckListener;

    private void check(){
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        String id = sharedPreferences.getString("customer_id", "Trần Công Trí");
//        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        referenceCheck = FirebaseDatabase.getInstance().getReference("BlackList").child("customers").child(id);
        valueEventCheckListener = referenceCheck.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    startActivity(new Intent(HomeActivity.this, DisplayBlockActivity.class));
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

        boolean isNetworkAvailable = NetworkUtils.isNetworkAvailable(this);
        NETWORK_STATE = isNetworkAvailable;

        if (!isNetworkAvailable) {
            showNetworkAlertDialog(this);
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {

                        showNetworkAlertDialog(context);

                        Log.i("Network", "Network: " + isNetworkAvailable);
                    }
                },
                new IntentFilter("network_change")

        );

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

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.BIND_REMOTEVIEWS) == PackageManager.PERMISSION_GRANTED) {
            Log.i("main", "BIND_REMOTEVIEWS Permission granted");
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.BIND_REMOTEVIEWS}, 3);
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
            Log.i("Home", "onOptionsItemSelected: notification" );
            Intent i = new Intent(HomeActivity.this, NotificationActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(i);
            return true;
        }
        else if(id == R.id.action_vaccine_care){
            Intent i = new Intent(HomeActivity.this, search_care_vaccines.class);
            startActivity(i);
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

    public static void showNetworkAlertDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Mất kết nối mạng");
        builder.setMessage("Vui lòng kiểm tra kết nối mạng của bạn.");

        builder.setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Đóng dialog
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();

        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        wlp.gravity = Gravity.BOTTOM;
        window.setAttributes(wlp);

        alertDialog.show();
    }
}