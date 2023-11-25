package com.prox.babyvaccinationtracker.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prox.babyvaccinationtracker.R;
import com.prox.babyvaccinationtracker.RemindLaterActivity;
import com.prox.babyvaccinationtracker.Schedule_an_injection;
import com.prox.babyvaccinationtracker.VaccineRegimen;
import com.prox.babyvaccinationtracker.model.NotificationMessage;
import com.prox.babyvaccinationtracker.model.Regimen;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    private List<NotificationMessage> notificationList;
    Context context;
    public static ArrayList<String> notificationIdsCanDelete = new ArrayList<>();
    private List<NotificationMessage> reminderList = new ArrayList<>();
    private HashMap<String, String> firstReminderMap = new HashMap<>();

    public NotificationAdapter(List<NotificationMessage> notificationList) {
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        context = parent.getContext();

        SharedPreferences sharedPreferences = context.getSharedPreferences("RemindLaterPrefs", Context.MODE_PRIVATE);
        String notificationsJson = sharedPreferences.getString("notifications", null);
        if (notificationsJson != null) {
            // Có dữ liệu trong SharedPreferences, giải mã danh sách từ JSON
            List<NotificationMessage> notificationList_ = new Gson().fromJson(notificationsJson, new TypeToken<List<NotificationMessage>>() {
            }.getType());
            reminderList.clear();
            reminderList.addAll(notificationList_);
        }
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        NotificationMessage notification = notificationList.get(position);

        holder.titleTextView.setText(notification.getTitle());
        holder.messageTextView.setText(notification.getMessage());
        if (notification.getDate() != null) {
            holder.dateTextView.setText(formatDate(notification.getDate()));
        }
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("vaccination_regimen");
        holder.actionContainer.setVisibility(View.GONE);
        String vaccineType = getEnglishVaccineType(notification.getMessage());
        if (notification.getTitle().contains("Nhắc nhở")) {
            holder.imageViewNotiType.setImageResource(R.drawable.ic_remind);
            holder.iconContainer.setBackgroundResource(R.drawable.tool_item_red_bg);

            if (notification.getMessage().contains("Nhắc lại")) {
                if (!isFirstReminder(notification.getBaby_id(), vaccineType)) {
                    notificationIdsCanDelete.add(notification.getNotification_id());
                    return;
                }
                if (!isReminder(notification.getNotification_id())){
                    holder.actionContainer.setVisibility(View.VISIBLE);
                    holder.action1.setText("Đăng ký ngay");
                    holder.action2.setVisibility(View.VISIBLE);
                    holder.action2.setText("Nhắc lại sau");
                    holder.action1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(context, Schedule_an_injection.class);
                            context.startActivity(i);
                        }
                    });

                    holder.action2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(context, RemindLaterActivity.class);
                            notification.setType(vaccineType);
                            i.putExtra("notification", notification);
                            context.startActivity(i);
                        }
                    });
                }else{
                    return;
                }

            } else {
                if (isVaccineType(vaccineType , notification.getBaby_id())){
                    return;
                }
                Date queryDate = notification.getDate();
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date formatDate = new Date();
                try {
                    formatDate = outputFormat.parse(outputFormat.format(queryDate));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                Log.i("Date__", "onBindViewHolder: " + formatDate);

                Log.i("VaccineType", "onBindViewHolder: " + vaccineType);
                if (vaccineType.equals("")) {
                    Log.i("VaccineType", "Message: " + notification.getMessage());
                }

                Query regimenRef = databaseReference.child(notification.getBaby_id()).orderByChild("vaccination_type").equalTo(vaccineType);
                Date finalFormatDate = formatDate;
                regimenRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Regimen regimen = dataSnapshot.getValue(Regimen.class);
                                if (regimen.getDate().equals(finalFormatDate) && !regimen.isVaccinated()) {
                                    holder.actionContainer.setVisibility(View.VISIBLE);
                                    holder.action1.setText("Đăng ký ngay");
                                    holder.action2.setVisibility(View.VISIBLE);
                                    holder.action2.setText("Nhắc lại sau");
                                    holder.action1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent i = new Intent(context, Schedule_an_injection.class);
                                            context.startActivity(i);
                                        }
                                    });

                                    holder.action2.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent i = new Intent(context, RemindLaterActivity.class);
                                            notification.setType(vaccineType);
                                            i.putExtra("notification", notification);
                                            context.startActivity(i);
                                        }
                                    });
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Xử lý lỗi nếu cần
                    }
                });
            }


        } else {
            notificationIdsCanDelete.add(notification.getNotification_id());
            holder.imageViewNotiType.setImageResource(R.drawable.ic_notifications);
            holder.iconContainer.setBackgroundResource(R.drawable.tool_item_bg);
        }
    }

    private boolean isFirstReminder(String baby_id, String vaccineType) {
        if (firstReminderMap.containsKey(baby_id) && firstReminderMap.get(baby_id).equals(vaccineType)) {
            return false;
        } else {
            firstReminderMap.put(baby_id, vaccineType);
            return true;
        }
    }

    public static ArrayList<String> getNotificationIdsCanDelete(){
        return notificationIdsCanDelete;
    }

    private Boolean isReminder(String notification_id){
        for (NotificationMessage reminder : reminderList) {
            if (reminder.getNotification_id().equals(notification_id)) {
                return true;
            }
        }
        return false;
    }

    private Boolean isVaccineType(String vaccineType, String baby_id){
        for (NotificationMessage reminder : reminderList) {
            if (reminder.getType().equals(vaccineType) && reminder.getBaby_id().equals(baby_id)) {
                return true;
            }
        }
        return false;
    }

    private String getEnglishVaccineType(String notificationTitle) {
        if (notificationTitle.contains("Viêm gan B"))
            return "hepatitis_b";
        if (notificationTitle.contains("Bạch hầu, ho gà, uốn ván"))
            return "diphtheria_whooping_cough_poliomyelitis";
        if (notificationTitle.contains("Bại liệt"))
            return "paralysis";
        if (notificationTitle.contains("Viêm màng não mủ do Hib"))
            return "pneumonia_hib_meningitis";
        if (notificationTitle.contains("Tiêu chảy do Rota Virus"))
            return "rotavirus_diarrhea";
        if (notificationTitle.contains("Viêm phổi, viêm màng não, viêm tai giữa"))
            return "pneumonia_meningitis_otitis_media_caused_by_streptococcus";
        if (notificationTitle.contains("Viêm màng não, nhiễm khuẩn huyết, viêm phổi do não mô cầu khuẩn B,C"))
            return "meningitis_sepsis_pneumonia_caused_by_neisseria_meningitidis_b_c";
        if (notificationTitle.contains("Cúm"))
            return "influenza";
        if (notificationTitle.contains("Sởi "))
            return "measles";
        if (notificationTitle.contains("Viêm màng não, nhiễm khuẩn huyết, viêm phổi do não mô cầu khuẩn A,C,W,Y"))
            return "meningitis_sepsis_pneumonia_caused_by_neisseria_meningitidis_a_c_w_y";
        if (notificationTitle.contains("Viêm não Nhật Bản"))
            return "japanese_encephalitis";
        if (notificationTitle.contains("Sởi, Quai bị, Rubella"))
            return "measles_mumps_rubella";
        if (notificationTitle.contains("Viêm gan A c"))
            return "hepatitis_a";
        if (notificationTitle.contains("Viêm gan A + B"))
            return "hepatitis_a_b";
        if (notificationTitle.contains("Thương hàn"))
            return "tetanus";
        if (notificationTitle.contains("Bệnh tả"))
            return "anthrax";
        if (notificationTitle.contains("Lao"))
            return "tuberculosis";

        return "";
    }


    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    private String formatDate(Date date) {
        // Format the date as needed
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        return dateFormat.format(date);
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView messageTextView;
        public TextView dateTextView;

        public LinearLayout iconContainer, actionContainer;
        public Button action1, action2, action3;
        public ImageView imageViewNotiType;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            iconContainer = itemView.findViewById(R.id.iconContainer);
            imageViewNotiType = itemView.findViewById(R.id.imageViewNotiType);
            actionContainer = itemView.findViewById(R.id.actionContainer);
            action1 = itemView.findViewById(R.id.action1);
            action2 = itemView.findViewById(R.id.action2);
            action3 = itemView.findViewById(R.id.action3);
        }
    }

}


