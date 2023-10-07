package com.prox.babyvaccinationtracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prox.babyvaccinationtracker.model.Vaccines;

import java.util.List;

public class vaccineadapter extends RecyclerView.Adapter<vaccineadapter.listvacine>{
    List<Vaccines> mlistvaccine;

    public vaccineadapter(List<Vaccines> mlistvaccine) {
        this.mlistvaccine = mlistvaccine;
    }

    @NonNull
    @Override
    public listvacine onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_vaccine, parent, false);
        return new listvacine(view);
    }

    @Override
    public void onBindViewHolder(@NonNull listvacine holder, int position) {
        Vaccines vaccines = mlistvaccine.get(position);
        if (vaccines == null){
            return;
        }else{
            holder.txtten.setText(vaccines.getVaccine_name());
            holder.txthienthi.setText(vaccines.getVaccination_target_group());
        }
    }

    @Override
    public int getItemCount() {
        if (mlistvaccine != null){
            return mlistvaccine.size();
        }
        return 0;
    }

    public class listvacine extends RecyclerView.ViewHolder{
        private TextView txtten;
        private TextView txthienthi;

        public listvacine(@NonNull View itemView) {
            super(itemView);
            txtten = itemView.findViewById(R.id.txtten);
            txthienthi = itemView.findViewById(R.id.txthienthi);
        }
    }
}
