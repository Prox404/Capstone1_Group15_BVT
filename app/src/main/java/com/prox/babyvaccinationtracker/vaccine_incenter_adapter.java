package com.prox.babyvaccinationtracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prox.babyvaccinationtracker.model.Vaccine_item;

import java.util.List;

public class vaccine_incenter_adapter extends RecyclerView.Adapter<vaccine_incenter_adapter.MyViewHolder>{
    private List<Vaccine_item> mlistvaccineitem;
    public vaccine_incenter_adapter(List<Vaccine_item> mlistvaccineitem) {
        this.mlistvaccineitem = mlistvaccineitem;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_vaccine, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Vaccine_item vaccineItem = mlistvaccineitem.get(position);
        holder.bindData(vaccineItem.getVaccineName(), vaccineItem.getVaccinePrice());
    }

    @Override
    public int getItemCount() {
        return mlistvaccineitem.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView vaccineName;
        private TextView vaccinePrice;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            vaccineName = itemView.findViewById(R.id.txtten);
            vaccinePrice = itemView.findViewById(R.id.txtgia);
        }

        public void bindData(String vaccineName, String vaccinePrice) {
            vaccineName.contains("Vaccine Name: " + vaccineName);
            vaccinePrice.contains("Vaccine Price: " + vaccinePrice);
        }
    }

}
