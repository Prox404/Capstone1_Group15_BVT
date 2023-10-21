package com.prox.babyvaccinationtracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.prox.babyvaccinationtracker.geo.district;
import com.prox.babyvaccinationtracker.geo.province;

import java.util.List;

public class DistrictAdapter extends ArrayAdapter<district> {
    public DistrictAdapter(Context context, List<district> districts) {
        super(context, 0, districts);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        district district = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView textViewProvinceName = convertView.findViewById(android.R.id.text1);

        textViewProvinceName.setText(district.getName());


        return convertView;
    }

}
