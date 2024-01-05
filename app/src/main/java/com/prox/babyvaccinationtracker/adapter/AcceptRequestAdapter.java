package com.prox.babyvaccinationtracker.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prox.babyvaccinationtracker.R;
import com.prox.babyvaccinationtracker.model.VaccinationCertificate;
import com.prox.babyvaccinationtracker.model.Vaccination_Registration;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class AcceptRequestAdapter extends RecyclerView.Adapter<AcceptRequestAdapter.ViewHolder> {
    private Context context;
    private List<Vaccination_Registration> vaccinationRegistions;

    public AcceptRequestAdapter(Context context, List<Vaccination_Registration> vaccinationRegistions) {
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
        holder.babyBirthdayTextView.setText(registration.getRegist_created_at());
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

//            buttonAccept.setVisibility(View.GONE);

            buttonAccept.setText("Xác nhận đã tiêm chủng");

            buttonAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("Aloo", "onClick: " + getAdapterPosition());
                    // Update status & remove item from list
                    //dialog show
                    Vaccination_Registration vaccination_registration = vaccinationRegistions.get(getAdapterPosition());
                    Date currentDate = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    Date registDate = new Date();
                    try {
                        registDate = formatter.parse(vaccination_registration.getRegist_created_at());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (registDate.after(currentDate)){
                        Toast.makeText(context, "Chưa đến ngày tiêm chủng", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Xác nhận đã tiêm chủng");
                    builder.setMessage("Bạn có chắc chắn đã tiêm chủng cho bé chưa?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Có", (dialogInterface, i) -> {
                        //update status
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Vaccination_Registration");
                        databaseReference.child(vaccinationRegistions.get(getAdapterPosition()).getRegist_id()).child("status").setValue(2).addOnCompleteListener(task -> {
                            Toast.makeText(context, "Đã xác nhận", Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                        }).addOnFailureListener(e -> {
                            Toast.makeText(context, "Lỗi xác nhận", Toast.LENGTH_SHORT).show();
                        });
                        Log.i("Accept", "onClick: " + vaccinationRegistions.get(getAdapterPosition()).toString());
                    });
                    builder.setNegativeButton("Không", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });
            // Initialize other views in your item layout here.
        }
    }
}