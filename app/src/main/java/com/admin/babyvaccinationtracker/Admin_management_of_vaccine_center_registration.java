package com.admin.babyvaccinationtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.admin.babyvaccinationtracker.Adapter.ManagerUserViewPageAdapter;
import com.google.android.material.tabs.TabLayout;

public class Admin_management_of_vaccine_center_registration extends AppCompatActivity {
    ImageView imageView_back;
    private TabLayout mTablayout;
    private ViewPager mViewPager;

    ManagerUserViewPageAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_management_of_vaccine_center_registration);

        imageView_back = findViewById(R.id.imageView_back);
        mTablayout = findViewById(R.id.ManagerTabLayout);
        mViewPager = findViewById(R.id.ManageviewPager);

        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        adapter = new ManagerUserViewPageAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(adapter);

        mTablayout.setupWithViewPager(mViewPager);


    }
}