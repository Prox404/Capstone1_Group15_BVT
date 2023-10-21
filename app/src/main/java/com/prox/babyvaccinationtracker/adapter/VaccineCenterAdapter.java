package com.prox.babyvaccinationtracker.adapter;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.prox.babyvaccinationtracker.model.Vaccine_center;

import java.util.List;

public class VaccineCenterAdapter extends ArrayAdapter<Vaccine_center> {
    public VaccineCenterAdapter(Context context, List<Vaccine_center> vaccineList) {
        super(context, 0, vaccineList);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Vaccine_center vaccineCenter = getItem(position);
        Log.i("vaccine_center", vaccineCenter.toString());
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        if (vaccineCenter != null) {
            textView.setText(vaccineCenter.getCenter_name());
        }

        return convertView;
    }

}
