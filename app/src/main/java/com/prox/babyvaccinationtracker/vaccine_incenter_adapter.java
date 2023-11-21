package com.prox.babyvaccinationtracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prox.babyvaccinationtracker.model.Vaccines;
import com.squareup.picasso.Picasso;

import java.util.List;

public class vaccine_incenter_adapter extends RecyclerView.Adapter<vaccine_incenter_adapter.MyViewHolder>{
    private List<Vaccines> mlistvaccineitem;
    public vaccine_incenter_adapter(List<Vaccines> mlistvaccineitem) {
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
        Vaccines vaccineItem = mlistvaccineitem.get(position);
        holder.vaccineName.setText(vaccineItem.getVaccine_name());
        holder.vaccinePrice.setText(vaccineItem.getPrice());
        if (vaccineItem.getVaccine_image() != null && vaccineItem.getVaccine_image().size() > 0) {
            String url = vaccineItem.getVaccine_image().get(0);
            if (url != null && !url.isEmpty()) {
                Picasso.get().load(url).into(holder.imgchinh);
            }
        }
        holder.txtXuatXu.setText(vaccineItem.getOrigin());
    }

    @Override
    public int getItemCount() {
        return mlistvaccineitem.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView vaccineName;
        private TextView vaccinePrice, txtXuatXu;
        ImageView imgchinh;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            vaccineName = itemView.findViewById(R.id.txtten);
            vaccinePrice = itemView.findViewById(R.id.txtgia);
            imgchinh = itemView.findViewById(R.id.imgchinh);
            txtXuatXu = itemView.findViewById(R.id.txtXuatXu);
        }


    }

}
