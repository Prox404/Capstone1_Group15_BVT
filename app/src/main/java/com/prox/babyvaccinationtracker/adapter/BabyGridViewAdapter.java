package com.prox.babyvaccinationtracker.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.prox.babyvaccinationtracker.BabyProfileActivity;
import com.prox.babyvaccinationtracker.R;
import com.prox.babyvaccinationtracker.model.Baby;

import java.util.List;

public class BabyGridViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<Baby> mBabies;

    public BabyGridViewAdapter(Context context, List<Baby> babies) {
        mContext = context;
        mBabies = babies;
    }

    @Override
    public int getCount() {
        return mBabies.size();
    }

    @Override
    public Object getItem(int position) {
        return mBabies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridViewItem = convertView;
        if(gridViewItem == null) {
            gridViewItem = LayoutInflater.from(mContext).inflate(R.layout.baby_card_item, parent, false);
        }

        Baby baby = mBabies.get(position);

        TextView txtName = gridViewItem.findViewById(R.id.textViewBabyName);
        TextView txtBirthday = gridViewItem.findViewById(R.id.textViewBirthday);

        // Set baby data
        txtName.setText(baby.getBaby_name());
        txtBirthday.setText(baby.getBaby_birthday());

        SharedPreferences sharedPreferences = mContext.getSharedPreferences("user", Context.MODE_PRIVATE);
        String UserID = sharedPreferences.getString("customer_id", "");

        gridViewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, BabyProfileActivity.class);
                intent.putExtra("baby_id", baby.getBaby_id());
                intent.putExtra("cus_id", UserID);
                mContext.startActivity(intent);
            }
        });

        return gridViewItem;
    }

}