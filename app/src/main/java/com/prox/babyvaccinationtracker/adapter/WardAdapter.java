package com.prox.babyvaccinationtracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.prox.babyvaccinationtracker.geo.province;
import com.prox.babyvaccinationtracker.geo.ward;

import java.util.List;

public class WardAdapter extends ArrayAdapter<ward> {
    public WardAdapter(Context context, List<ward> wards) {
        super(context, 0, wards);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ward ward = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView textViewProvinceName = convertView.findViewById(android.R.id.text1);

        textViewProvinceName.setText(ward.getName());

        return convertView;
    }
}
