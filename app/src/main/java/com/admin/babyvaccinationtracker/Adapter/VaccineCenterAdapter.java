package com.admin.babyvaccinationtracker.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.admin.babyvaccinationtracker.R;
import com.admin.babyvaccinationtracker.SendNotificationActivity;
import com.admin.babyvaccinationtracker.model.Vaccine_center;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class VaccineCenterAdapter extends RecyclerView.Adapter<VaccineCenterAdapter.VaccineCenterViewHolder> {
    private List<Vaccine_center> vaccineCenterList;
    private List<String> selectedVaccineCenter = new ArrayList<>();

    public VaccineCenterAdapter(List<Vaccine_center> vaccineCenterList) {
        this.vaccineCenterList = vaccineCenterList;
        this.selectedVaccineCenter = SendNotificationActivity.selectedCustomers;
    }

    @NonNull
    @Override
    public VaccineCenterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vaccine_center_item, parent, false);
        return new VaccineCenterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VaccineCenterViewHolder holder, int position) {
        Vaccine_center vaccineCenter = vaccineCenterList.get(position);
        holder.bind(vaccineCenter);
    }

    @Override
    public int getItemCount() {
        return vaccineCenterList.size();
    }

    public class VaccineCenterViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewCenterName;
        private TextView textViewHotline;
        private TextView textViewAddress;
        ImageView imageViewVaccineCenter;

        public VaccineCenterViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCenterName = itemView.findViewById(R.id.textViewCenterName);
            textViewHotline = itemView.findViewById(R.id.textViewHotline);
            textViewAddress = itemView.findViewById(R.id.textViewAddress);
            imageViewVaccineCenter = itemView.findViewById(R.id.imageViewVaccineCenter);
        }

        public void bind(Vaccine_center vaccineCenter) {
            Log.i("Adaper", "bind: " + vaccineCenter.toString());
            textViewCenterName.setText(vaccineCenter.getCenter_name());
            textViewHotline.setText(vaccineCenter.getHotline());
            textViewAddress.setText(vaccineCenter.getCenter_address());

            if (selectedVaccineCenter.contains(vaccineCenter.getCenter_id())) {
                // Thay đổi giao diện người dùng (ví dụ: thay đổi màu nền)
                itemView.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.primarySelectedColor));
            } else {
                // Reset giao diện người dùng (ví dụ: trả về màu nền mặc định)
                itemView.setBackgroundColor(itemView.getContext().getResources().getColor(android.R.color.white));
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedVaccineCenter.contains(vaccineCenter.getCenter_id())) {
                        selectedVaccineCenter.remove(vaccineCenter.getCenter_id());
                    } else {
                        selectedVaccineCenter.add(vaccineCenter.getCenter_id());
                    }
                    notifyItemChanged(getAdapterPosition());
                }
            });

            String center_image = vaccineCenter.getCenter_image();
            center_image = center_image.contains("https") ? center_image : center_image.replace("http", "https");
            Picasso.get().load(center_image).into(imageViewVaccineCenter, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    imageViewVaccineCenter.setImageResource(R.drawable.user_default_avatar);
                }
            });
        }
    }
}

