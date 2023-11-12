package com.prox.babyvaccinationtracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prox.babyvaccinationtracker.model.Vaccine_center;
import com.prox.babyvaccinationtracker.model.Vaccines;

import java.io.Serializable;
import java.util.List;

public class vaccine_center_adapter extends RecyclerView.Adapter<vaccine_center_adapter.list_vacine_center> {
    List<Vaccine_center> mlistvaccinecenter;
    private Context mcontext;

    public vaccine_center_adapter(Context context, List<Vaccine_center> mlistvaccinecenter) {
        this.mlistvaccinecenter = mlistvaccinecenter;
        this.mcontext = context;
    }

    @NonNull
    @Override
    public list_vacine_center onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_vaccine_center, parent, false);
        return new list_vacine_center(view);
    }

    @Override
    public void onBindViewHolder(@NonNull list_vacine_center holder, int position) {
        final Vaccine_center vaccineCenter = mlistvaccinecenter.get(position);
        if (vaccineCenter == null) {
            return;
        } else {
            Log.i("TAG", "onBindViewHolder: "+ vaccineCenter.toString());
            holder.txtten.setText(vaccineCenter.getCenter_name());
            holder.txtdiachi.setText(vaccineCenter.getCenter_address());
        }
        holder.vaccine_center_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickgotoInfor(vaccineCenter);
            }
        });
    }

    private void onClickgotoInfor(Vaccine_center vaccineCenter) {
        Intent intent = new Intent(mcontext, information_center.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("center_name", vaccineCenter);
        intent.putExtras(bundle);
        mcontext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        if (mlistvaccinecenter != null) {
            return mlistvaccinecenter.size();
        }
        return 0;
    }

    public class list_vacine_center extends RecyclerView.ViewHolder {
        private TextView txtten;
        private TextView txtdiachi;
        private LinearLayout vaccine_center_id;

        public list_vacine_center(@NonNull View itemView) {
            super(itemView);
            txtten = itemView.findViewById(R.id.txtten);
            txtdiachi = itemView.findViewById(R.id.txtdichi);
            vaccine_center_id = itemView.findViewById(R.id.vaccine_center_id);
        }
    }
}
