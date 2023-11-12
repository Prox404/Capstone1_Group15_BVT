package com.prox.babyvaccinationtracker.adapter;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.prox.babyvaccinationtracker.R;
import com.prox.babyvaccinationtracker.model.Vaccine_center;
import com.squareup.picasso.Picasso;

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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.vaccine_center_item, parent, false);
        }

        TextView textViewVaccineCenterName = convertView.findViewById(R.id.textViewVaccineCenterName);
        TextView textViewAddress = convertView.findViewById(R.id.textViewAddress);
        TextView textViewHotLine = convertView.findViewById(R.id.textViewHotLine);
        TextView textViewWorkingTime = convertView.findViewById(R.id.textViewWorkingTime);
        ImageView imageViewVaccineCenter = convertView.findViewById(R.id.imgchinh);
        if (vaccineCenter != null) {
            textViewVaccineCenterName.setText(vaccineCenter.getCenter_name());
            textViewAddress.setText(vaccineCenter.getCenter_address());
            textViewHotLine.setText(vaccineCenter.getHotline());
            textViewWorkingTime.setText(vaccineCenter.getWork_time());
            String imageUrl = vaccineCenter.getCenter_image();
            imageUrl = imageUrl.replace("https", "http");
            Picasso.get().load(imageUrl).into(imageViewVaccineCenter);
        }

        return convertView;
    }

}
