package com.vaccinecenter.babyvaccinationtracker.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vaccinecenter.babyvaccinationtracker.R;
import com.vaccinecenter.babyvaccinationtracker.See_detailed_vaccine_information;
import com.vaccinecenter.babyvaccinationtracker.model.Vaccines;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VaccineAdapter extends RecyclerView.Adapter<VaccineAdapter.listvacine>{
    List<Vaccines> mlistvaccine;
    private Context mcontext;

    public VaccineAdapter(Context context, List<Vaccines> mlistvaccine) {
        this.mlistvaccine = mlistvaccine;
        this.mcontext = context;
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
        final Vaccines vaccines = mlistvaccine.get(position);
        if (vaccines == null){
            return;
        }else{
            holder.txtten.setText(vaccines.getVaccine_name());
            holder.txtgia.setText(vaccines.getPrice());
            Picasso.get().load(vaccines.getVaccine_image().get(0)).error(R.drawable.ic_launcher_foreground).into(holder.image_view_search_vaccine);
        }
        holder.vaccine_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickgotoInfor(vaccines);
            }
        });
    }
    private void onClickgotoInfor(Vaccines vaccines){
        Intent intent = new Intent(mcontext, See_detailed_vaccine_information.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("vaccine_name", vaccines);
        intent.putExtras(bundle);
        mcontext.startActivity(intent);
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
        private TextView txtgia;
        private ImageView image_view_search_vaccine;
        private LinearLayout vaccine_id;

        public listvacine(@NonNull View itemView) {
            super(itemView);
            txtten = itemView.findViewById(R.id.txtten);
            txtgia = itemView.findViewById(R.id.txtgia);
            image_view_search_vaccine = itemView.findViewById(R.id.image_view_search_vaccine);
            vaccine_id = itemView.findViewById(R.id.vaccine_id);
        }
    }
}

