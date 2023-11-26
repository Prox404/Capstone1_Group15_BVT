package com.prox.babyvaccinationtracker.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prox.babyvaccinationtracker.R;
import com.prox.babyvaccinationtracker.model.Vaccination_Registration;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RegistrationTimelineAdapter extends RecyclerView.Adapter<RegistrationTimelineAdapter.ViewHolder> {
    private Context context;
    private List<Vaccination_Registration> vaccinationRegistions;

    public RegistrationTimelineAdapter(Context context, List<Vaccination_Registration> vaccinationRegistions) {
        this.context = context;
        this.vaccinationRegistions = vaccinationRegistions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vaccine_registration_timeline, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Vaccination_Registration registration = vaccinationRegistions.get(position);
        Log.i("Aloo", "onBindViewHolder: " + registration.getBaby().getBaby_avatar());
        String Date = registration.getRegist_created_at().split("/")[0] + "/" + registration.getRegist_created_at().split("/")[1];
        holder.textViewDate.setText(Date);
        holder.textViewVaccineName.setText(registration.getVaccine().getVaccine_name());
        holder.textViewBabyName.setText(registration.getBaby().getBaby_name());
        holder.textViewCenterName.setText(registration.getCenter().getCenter_name());
        holder.textViewCenterAddress.setText(registration.getCenter().getCenter_address());
        holder.textViewWorkingTime.setText(registration.getCenter().getWork_time().split(",")[1].trim());
        holder.textViewPrice.setText(registration.getVaccine().getPrice());

        String address2 = registration.getCenter().getCenter_address2();
        String address = registration.getCenter().getCenter_address();
        String fullAddress = address;
        if (address2 != null && !address2.isEmpty()) {
            fullAddress = address2 + ", " + address;
            holder.textViewCenterAddress.setText(fullAddress);
        }

        String finalFullAddress = fullAddress;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(finalFullAddress));

                // Tạo Intent để mở ứng dụng Google Maps
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

                // Chỉ định gói ứng dụng của Google Maps
                mapIntent.setPackage("com.google.android.apps.maps");

                // Nếu có, mở ứng dụng Google Maps
                if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(mapIntent);
                } else {
                    Toast.makeText(context, "Không tìm thấy ứng dụng Google Maps", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return vaccinationRegistions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDate,
                textViewVaccineName,
                textViewBabyName,
                textViewCenterName,
                textViewCenterAddress,
                textViewWorkingTime,
                textViewPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewVaccineName = itemView.findViewById(R.id.textViewVaccineName);
            textViewBabyName = itemView.findViewById(R.id.textViewBabyName);
            textViewCenterName = itemView.findViewById(R.id.textViewCenterName);
            textViewCenterAddress = itemView.findViewById(R.id.textViewCenterAddress);
            textViewWorkingTime = itemView.findViewById(R.id.textViewWorkingTime);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
        }
    }
}