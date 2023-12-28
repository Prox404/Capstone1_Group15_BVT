package com.vaccinecenter.babyvaccinationtracker.Adapter;

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
import com.vaccinecenter.babyvaccinationtracker.R;
import com.vaccinecenter.babyvaccinationtracker.model.NotificationMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    private List<NotificationMessage> notificationList;
    Context context;

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
        holder.actionContainer.setVisibility(View.GONE);
        holder.imageViewNotiType.setImageResource(R.drawable.ic_notifications);
        holder.iconContainer.setBackgroundResource(R.drawable.tool_item_bg);
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


