package com.prox.babyvaccinationtracker.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ContentView;
import androidx.annotation.NonNull;

import com.prox.babyvaccinationtracker.R;
import com.prox.babyvaccinationtracker.model.Vaccine_center;
import com.prox.babyvaccinationtracker.model.Vaccines;
import com.squareup.picasso.Picasso;

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
            convertView = View.inflate(getContext(), R.layout.vaccine_horizontal_item, null);
        }

        TextView txtten = convertView.findViewById(R.id.txtten);
        TextView txtgia = convertView.findViewById(R.id.txtgia);
        ImageView imgchinh = convertView.findViewById(R.id.imgchinh);
        TextView txtXuatXu = convertView.findViewById(R.id.txtXuatXu);

        if (vaccineCenter != null) {
            txtten.setText(vaccineCenter.getVaccine_name());
            txtgia.setText(vaccineCenter.getPrice());
            txtXuatXu.setText(vaccineCenter.getOrigin());
            String imageUrl = vaccineCenter.getVaccine_image().get(0);
            Log.i("image url", "getView: " + imageUrl);
            imageUrl = imageUrl.replace("https", "http");
            Picasso.get().load(imageUrl).into(imgchinh);
        }

        return convertView;
    }
}
