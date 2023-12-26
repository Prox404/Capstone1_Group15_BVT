package com.vaccinecenter.babyvaccinationtracker.Adapter;

import android.content.Context;
import android.icu.util.Calendar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vaccinecenter.babyvaccinationtracker.R;
import com.vaccinecenter.babyvaccinationtracker.model.Health;

import java.util.ArrayList;

public class HealthBabyAdapter  extends RecyclerView.Adapter<HealthBabyAdapter.viewholer> {
    private ArrayList<Health> healthItems;


    public HealthBabyAdapter( ArrayList<Health> healthItems) {
        this.healthItems = healthItems;
    }

    @NonNull
    @Override
    public viewholer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_health_baby, parent, false);
        return new viewholer(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholer holder, int position) {
        Health health = healthItems.get(position);
        // todo kiểm tra tháng
        String[] m = health.getHealthCreated_at().split(" ");

        Calendar calendar = Calendar.getInstance();
        int current_month = calendar.get(Calendar.MONTH);
        int month_health = getMonthIndex(m[1]);

        if(current_month == month_health){
            holder.heallth_month.setText("Tháng này");
        }
        else{
            holder.heallth_month.setText((current_month-month_health)+" tháng trước");
        }
        holder.height.setText(""+health.getHeight());
        holder.sleep.setText(""+health.getSleep());
        holder.weight.setText(""+health.getWeight());
    }

    private int getMonthIndex(String monthAbbreviation) {
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        for (int i = 0; i < months.length; i++) {
            if (months[i].equals(monthAbbreviation)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return healthItems.size();
    }


    public class viewholer extends RecyclerView.ViewHolder {
        TextView height;
        TextView weight;
        TextView sleep;
        TextView heallth_month;
        public viewholer(@NonNull View itemView) {
            super(itemView);

            height = itemView.findViewById(R.id.health_height_item);
            weight = itemView.findViewById(R.id.health_weight_item);
            sleep = itemView.findViewById(R.id.health_sleep_item);
            heallth_month = itemView.findViewById(R.id.heallth_month);


        }
    }
}
