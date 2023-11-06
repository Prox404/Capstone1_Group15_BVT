package com.admin.babyvaccinationtracker.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.admin.babyvaccinationtracker.BlackListFragment;
import com.admin.babyvaccinationtracker.ManageUserFragment;
import com.admin.babyvaccinationtracker.ManageVaccineRegistrationFragment;

public class ManagerUserViewPageAdapter extends FragmentPagerAdapter {

    public ManagerUserViewPageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new ManageUserFragment();
            case 1:
                return new BlackListFragment();
            case 2:
                return new ManageVaccineRegistrationFragment();
            default:
                return new ManageUserFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Quản lý người dùng";
            case 1:
                return "Danh sách người dùng bị chặn";
            case 2:
                return "Quản lý đơn đăng ký trung tâm vắc-xin";
        }
        return super.getPageTitle(position);
    }
}
