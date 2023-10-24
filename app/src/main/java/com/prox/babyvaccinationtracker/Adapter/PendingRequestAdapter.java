package com.prox.babyvaccinationtracker.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prox.babyvaccinationtracker.R;
import com.prox.babyvaccinationtracker.model.Vaccination_Registration;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PendingRequestAdapter extends RecyclerView.Adapter<PendingRequestAdapter.ViewHolder> {
    private Context context;
    private List<Vaccination_Registration> vaccinationRegistions;

    public PendingRequestAdapter(Context context, List<Vaccination_Registration> vaccinationRegistions) {
        this.context = context;
        this.vaccinationRegistions = vaccinationRegistions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vaccination_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Vaccination_Registration registration = vaccinationRegistions.get(position);
        Log.i("Aloo", "onBindViewHolder: " + registration.getBaby().getBaby_avatar());

        holder.babyNameTextView.setText(registration.getBaby().getBaby_name());
        holder.babyBirthdayTextView.setText(registration.getBaby().getBaby_birthday());
        holder.congenitalDiseaseTextView.setText(registration.getBaby().getBaby_congenital_disease());
        holder.vaccineNameTextView.setText(registration.getVaccine().getVaccine_name());
        holder.vaccineCenterNameTextView.setText(registration.getCenter().getCenter_name());
        String imageUrl = registration.getBaby().getBaby_avatar();
        //replace space with http to https
        imageUrl = imageUrl.replace("https", "http");
        Picasso.get().load(imageUrl)
                .error(R.drawable.ic_launcher_background)
                .into(holder.babyAvatarImageView);
    }

    @Override
    public int getItemCount() {
        return vaccinationRegistions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView babyNameTextView, babyBirthdayTextView, congenitalDiseaseTextView, vaccineNameTextView, vaccineCenterNameTextView;
        ImageView babyAvatarImageView;

        Button buttonAccept;
        Button buttonCancel;

        public ViewHolder(View itemView) {
            super(itemView);
            babyNameTextView = itemView.findViewById(R.id.babyNameTextView);
            babyAvatarImageView = itemView.findViewById(R.id.babyAvatarImageView);
            buttonAccept = itemView.findViewById(R.id.buttonAction);
            buttonCancel = itemView.findViewById(R.id.buttonAction2);
            babyBirthdayTextView = itemView.findViewById(R.id.babyBirthdayTextView);
            congenitalDiseaseTextView = itemView.findViewById(R.id.congenitalDiseaseTextView);
            vaccineNameTextView = itemView.findViewById(R.id.vaccineNameTextView);
            vaccineCenterNameTextView = itemView.findViewById(R.id.vaccineCenterNameTextView);

            buttonCancel.setVisibility(View.VISIBLE);

            buttonAccept.setText("Chấp nhận");
            buttonCancel.setText("Từ chối");

            buttonAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("Aloo", "onClick: " + getAdapterPosition());
                    // Update status & remove item from list
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Vaccination_Registration");
                    databaseReference.child(vaccinationRegistions.get(getAdapterPosition()).getRegister_id()).child("status").setValue(1);
                    Log.i("Accept", "onClick: " + vaccinationRegistions.get(getAdapterPosition()).toString());
                    vaccinationRegistions.remove(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });

            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("Aloo", "onClick: " + getAdapterPosition());
                    // Update status & remove item from list
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Vaccination_Registration");
                    databaseReference.child(vaccinationRegistions.get(getAdapterPosition()).getRegister_id()).child("status").setValue(-1);
                    Log.i("Accept", "onClick: " + vaccinationRegistions.get(getAdapterPosition()).toString());
                    vaccinationRegistions.remove(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
            // Initialize other views in your item layout here.
        }
    }
}