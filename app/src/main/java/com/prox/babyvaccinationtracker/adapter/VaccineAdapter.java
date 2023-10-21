package com.prox.babyvaccinationtracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.prox.babyvaccinationtracker.model.Vaccine_center;
import com.prox.babyvaccinationtracker.model.Vaccines;

import java.util.List;

public class VaccineAdapter extends ArrayAdapter<Vaccines> {
    public VaccineAdapter(Context context, List<Vaccines> vaccineList) {
        super(context, 0, vaccineList);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Vaccines vaccineCenter = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        if (vaccineCenter != null) {
            textView.setText(vaccineCenter.getVaccine_name());
        }

        return convertView;
    }
}
