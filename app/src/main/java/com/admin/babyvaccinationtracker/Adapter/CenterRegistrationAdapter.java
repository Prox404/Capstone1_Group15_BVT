package com.admin.babyvaccinationtracker.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.admin.babyvaccinationtracker.R;
import com.admin.babyvaccinationtracker.VaccineCenterRegistration;
import com.admin.babyvaccinationtracker.model.Vaccine_center_registration;

import java.util.ArrayList;

public class CenterRegistrationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    ArrayList<Vaccine_center_registration> Vaccine_center_registration;
    Context context;
    VaccineCenterRegistration vaccineCenterRegistration;
    public CenterRegistrationAdapter(Context context,ArrayList<Vaccine_center_registration> vaccineCenterRegistration){
        this.context = context;
        this.Vaccine_center_registration = vaccineCenterRegistration;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vaccine_center_registration, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Vaccine_center_registration re = Vaccine_center_registration.get(position);
        Log.i("HOLLDER", ""+holder);
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.tv_name.setText(re.getCenter().getCenter_name());
            viewHolder.tv_email.setText(re.getCenter().getCenter_email());
            viewHolder.btn_confirm_vaccine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(context == null){
                        Log.i("Contentttttttt",""+context);
                        return;
                    }
                    if(re.getCenter() == null){
                        Log.i("Centerttttttt",""+re.getCenter());
                        return;
                    }
                    vaccineCenterRegistration = new VaccineCenterRegistration();
                    vaccineCenterRegistration.registerCenter(context,re.getCenter(),re.getCenter_registration_id());

                    Vaccine_center_registration.remove(position);
                    notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(Vaccine_center_registration != null)
            return Vaccine_center_registration.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name, tv_email;
        Button btn_confirm_vaccine;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.tv_name);
            tv_email = itemView.findViewById(R.id.tv_email);
            btn_confirm_vaccine = itemView.findViewById(R.id.btn_confirm_vaccine);
        }
    }
}
