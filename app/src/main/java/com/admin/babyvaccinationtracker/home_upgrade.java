package com.admin.babyvaccinationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class home_upgrade extends AppCompatActivity {
    BottomNavigationView navigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_upgrade);

        navigation = findViewById(R.id.navigation);
        loadFragment(new homeFragment());
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.navigation_home ){
                    Fragment fragment = new homeFragment();
                    loadFragment(fragment);
                    return true;
                }
                else if(id == R.id.navigation_posts){
                    Fragment fragment = new Fragment_manage_post();
                    loadFragment(fragment);
                    return true;
                }
                else if(id == R.id.navigation_user){
//                    Fragment fragment = new Fragment_manage_user_upgrade();
//                    loadFragment(fragment);
                    startActivity(new Intent(home_upgrade.this, Admin_management_of_user.class));
                    return true;
                }
                else if(id == R.id.navigation_setting){
                    return true;
                }
                return false;
            }
        });
    }
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }
    @Override
    public void onBackPressed() {
        // Xử lý sự kiện khi người dùng bấm nút Back
        // Ví dụ: Hiển thị hộp thoại xác nhận thoát chương trình, vv.
        moveTaskToBack(true);
    }
}