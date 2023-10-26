package com.prox.babyvaccinationtracker.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.prox.babyvaccinationtracker.R;
import com.prox.babyvaccinationtracker.model.Health;

import java.util.ArrayList;

public class HealthBabyAdapter extends ArrayAdapter<Health> {
    private Context context;
    private ArrayList<Health> healthItems;


    public HealthBabyAdapter(Context context, ArrayList<Health> healthItems) {
        super(context, R.layout.list_health_baby);
        this.context = context;
        this.healthItems = healthItems;
    }
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        final Health health = healthItems.get(position);
        View view = View.inflate(context, R.layout.list_health_baby, null);

        TextView height = view.findViewById(R.id.health_height_item);
        TextView weight = view.findViewById(R.id.health_weight_item);
        TextView sleep = view.findViewById(R.id.health_sleep_item);


        height.setText(health.getHeight()+"");
        weight.setText(health.getWeight()+"");
        sleep.setText(health.getSleep()+"");

        return view;
    }
}
