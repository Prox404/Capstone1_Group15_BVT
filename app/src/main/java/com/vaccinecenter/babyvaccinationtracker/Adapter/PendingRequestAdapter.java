package com.vaccinecenter.babyvaccinationtracker.Adapter;

import android.content.Context;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vaccinecenter.babyvaccinationtracker.R;
import com.vaccinecenter.babyvaccinationtracker.model.NotificationMessage;
import com.vaccinecenter.babyvaccinationtracker.model.Vaccination_Registration;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

            DatabaseReference notificationReference = FirebaseDatabase.getInstance().getReference("notifications");


            buttonCancel.setVisibility(View.VISIBLE);

            buttonAccept.setText("Chấp nhận");
            buttonCancel.setText("Từ chối");

            buttonAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("Aloo", "onClick: " + getAdapterPosition());
                    String user_id = vaccinationRegistions.get(getAdapterPosition()).getCus().getCustomer_id();
                    String baby_id = vaccinationRegistions.get(getAdapterPosition()).getBaby().getBaby_id();
                    String baby_name = vaccinationRegistions.get(getAdapterPosition()).getBaby().getBaby_name();
                    String vaccine_name = vaccinationRegistions.get(getAdapterPosition()).getVaccine().getVaccine_name();
                    String register_date = vaccinationRegistions.get(getAdapterPosition()).getRegist_created_at();
                    // Update status & remove item from list
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Vaccination_Registration");
                    databaseReference.child(vaccinationRegistions.get(getAdapterPosition()).getRegister_id()).child("status").setValue(1).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            String notificationCompletedId = notificationReference.push().getKey();
                            String notificationScheduleId = notificationReference.push().getKey();

                            NotificationMessage scheduleNotificationMessage = new NotificationMessage();
                            scheduleNotificationMessage.setUser_id(user_id);
                            scheduleNotificationMessage.setBaby_id(baby_id);
                            scheduleNotificationMessage.setTitle("Hôm nay bạn có lịch tiêm chủng");
                            scheduleNotificationMessage.setMessage("Hôm nay bạn có lịch tiêm chủng cho bé " + baby_name + " vaccine " + vaccine_name);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                            Date date = new Date();
                            try {
                                date = simpleDateFormat.parse(register_date + " 07:00");
                            } catch (ParseException e) {
                                Log.i("Aloo", "onComplete: " + e.getMessage());
                            }
                            scheduleNotificationMessage.setDate(date);

                            notificationReference.child(notificationScheduleId).setValue(scheduleNotificationMessage).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    NotificationMessage notificationMessage = new NotificationMessage();
                                    notificationMessage.setUser_id(user_id);
                                    notificationMessage.setTitle("Đăng ký tiêm chủng");
                                    notificationMessage.setMessage("Đơn đăng ký tiêm chủng của bé " + baby_name + " đã được chấp nhận");
                                    Date currentDate = new Date();
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(currentDate);
                                    calendar.add(Calendar.MINUTE, 1);
                                    notificationMessage.setDate(calendar.getTime());

                                    notificationReference.child(notificationCompletedId).setValue(notificationMessage).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(view.getContext(), "Thành công!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                        }
                    });
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