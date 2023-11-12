package com.vaccinecenter.babyvaccinationtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.vaccinecenter.babyvaccinationtracker.Adapter.TabPagerAdapter;

public class RegistrationRequestActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration_request);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        TabPagerAdapter adapter = new TabPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new PendingRequestFragment(), "Đang chờ");
        adapter.addFragment(new AcceptRequestFragment(), "Xác nhận tiêm chủng");
        adapter.addFragment(new CompletedRequestFragment(), "Đã hoàn thành");
        adapter.addFragment(new CanceledRequestFragment(), "Đã hủy");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}