package com.prox.babyvaccinationtracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.prox.babyvaccinationtracker.model.Vaccine_item;
import com.prox.babyvaccinationtracker.model.Vaccines;
import com.squareup.picasso.Picasso;

import java.util.List;

public class vaccineadapter extends ArrayAdapter<Vaccines> {
    private final Context context;
    private final List<Vaccines> mlistvaccine;

    public vaccineadapter(Context context, List<Vaccines> mlistvaccine) {
        super(context, R.layout.list_vaccine, mlistvaccine);
        this.context = context;
        this.mlistvaccine = mlistvaccine;
    }
  
    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        final Vaccines vaccines = mlistvaccine.get(position);
        View view = View.inflate(context, R.layout.list_vaccine, null);

        TextView txtten = view.findViewById(R.id.txtten);
        TextView txtgia = view.findViewById(R.id.txtgia);
        ImageView imgchinh = view.findViewById(R.id.imgchinh);
        LinearLayout vaccine_id = view.findViewById(R.id.vaccine_id);
        TextView textViewAddress = view.findViewById(R.id.textViewAddress);
        TextView textViewVaccineCenterName = view.findViewById(R.id.textViewVaccineCenterName);

        txtten.setText(vaccines.getVaccine_name());
        txtgia.setText(vaccines.getPrice());
        textViewAddress.setText(vaccines.getAdditionInformation().get("center_address"));
        textViewVaccineCenterName.setText(vaccines.getAdditionInformation().get("center_name"));
        String imageUrl = vaccines.getVaccine_image().get(0);
        Log.i("image url", "getView: " + imageUrl);
        imageUrl = imageUrl.replace("https", "http");
        Picasso.get().load(imageUrl).into(imgchinh, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                //do smth when picture is loaded successfully
            }

            @Override
            public void onError(Exception e) {
                //do smth when there is picture loading error
            }
        });

        vaccine_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickGotoInfo(vaccines);
            }
        });

        return view;
    }

    private void onClickGotoInfo(Vaccines vaccines) {
        Intent intent = new Intent(context, information_vaccine.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("vaccine", vaccines);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
