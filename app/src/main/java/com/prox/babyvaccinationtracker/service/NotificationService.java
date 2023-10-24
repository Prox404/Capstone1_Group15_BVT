package com.prox.babyvaccinationtracker.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prox.babyvaccinationtracker.NotificationActivity;
import com.prox.babyvaccinationtracker.R;
import com.prox.babyvaccinationtracker.model.NotificationMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NotificationService extends Service {
    public NotificationService() {
    }

    private DatabaseReference databaseReference;
    private String user_id;
    private String notificationChannelId = "VaccineNotification";
    private NotificationManager notificationManager;

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        user_id = sharedPreferences.getString("customer_id", "");
        Log.i("Notification_service", "Run Service");

        // Khởi tạo Firebase Database Reference
        databaseReference = FirebaseDatabase.getInstance().getReference("notifications").child(user_id);

        // Khởi tạo NotificationManager
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Tạo Notification Channel (chỉ cần thực hiện trên Android 8.0+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(notificationChannelId, "VaccineNotification", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Lắng nghe sự thay đổi trong cơ sở dữ liệu
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // Xử lý khi có lịch hẹn mới được thêm vào
                NotificationMessage notificationMessage = dataSnapshot.getValue(NotificationMessage.class);
                if (notificationMessage != null) {
                    scheduleNotification(notificationMessage); // Lên lịch hiển thị thông báo
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // Xử lý khi có sự thay đổi trong lịch hẹn (nếu cần)
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                // Xử lý khi có lịch hẹn bị xóa (nếu cần)
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // Xử lý khi có sự thay đổi vị trí của lịch hẹn (nếu cần)
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra trong cơ sở dữ liệu (nếu cần)
            }
        });

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void scheduleNotification(NotificationMessage notification) {
        Log.i("Notification_service", "schedule notification");
        // Định dạng thời gian lấy từ cơ sở dữ liệu
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        Date notificationTime = notification.getDate();

        // Kiểm tra nếu thời gian lịch hẹn đã qua thì không hiển thị thông báo
        if (notificationTime != null && notificationTime.after(new Date())) {
            // Lên lịch hiển thị thông báo
            Handler handler = new Handler();
            handler.postDelayed(() -> showNotification(notification), notificationTime.getTime() - System.currentTimeMillis());
        }
    }

    // Phương thức để hiển thị thông báo
    private void showNotification(NotificationMessage message) {
        Intent intent = new Intent(this, NotificationActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this, notificationChannelId)
                .setContentTitle(message.getTitle())
                .setContentText(message.getMessage())
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

//        notificationManager.notify(0, notification);
        startForeground((int)(Math.random() * 10000) + 1, notification);
    }

}