package com.prox.babyvaccinationtracker;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.provider.CalendarContract;
import android.util.Log;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.prox.babyvaccinationtracker.model.NotificationMessage;
import com.prox.babyvaccinationtracker.model.Vaccination_Registration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CalendarSync {

    static ArrayList<Vaccination_Registration> vaccination_registrations = new ArrayList<>();
    static ArrayList<NotificationMessage> notificationMessages = new ArrayList<>();

    public static void syncVaccineRegisterToCalendar(Context context) {

        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
            String id_vaccine_center = sharedPreferences.getString("customer_id","");

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Vaccination_Registration");
            Query query = databaseReference.orderByChild("cus/customer_id").equalTo(id_vaccine_center);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    vaccination_registrations.clear();
//                    Log.i("Pending", "onDataChange: " + snapshot.getChildrenCount());
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Vaccination_Registration vaccination_registration = dataSnapshot.getValue(Vaccination_Registration.class);
                        if(vaccination_registration.getStatus() == 1){
                            vaccination_registration.setRegist_id(dataSnapshot.getKey());
                            vaccination_registrations.add(vaccination_registration);
                        }
                    }
                    Log.i("Calendar Sync", "onDataChange: " + vaccination_registrations.size());
                    for (Vaccination_Registration vaccination_registration : vaccination_registrations) {
                        addEventToCalendar(context, vaccination_registration);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addEventToCalendar(Context context, Vaccination_Registration vaccination_registration) {
        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();

        try {
            // Parse thông tin từ JSON
            String title = "Tiêm vaccine" + vaccination_registration.getVaccine().getVaccine_name();
            String vaccine_date = vaccination_registration.getRegist_created_at() + " 07:00";
            String location = vaccination_registration.getCenter().getCenter_address();
            if (vaccination_registration.getCenter().getCenter_address2() != null) {
                location = vaccination_registration.getCenter().getCenter_address2() + " " + location;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date vaccine_date__ = sdf.parse(vaccine_date);
            long startTime = vaccine_date__.getTime();
            // Các thông tin khác nếu cần

            // Thêm thông tin vào ContentValues
            values.put(CalendarContract.Events.DTSTART, startTime);
            values.put(CalendarContract.Events.DTEND, startTime + (12 * 60 * 60 * 1000));
            values.put(CalendarContract.Events.TITLE, title);
            values.put(CalendarContract.Events.CALENDAR_ID, 1);
            values.put(CalendarContract.Events.EVENT_LOCATION, location);
            values.put(CalendarContract.Events.EVENT_TIMEZONE, "Asia/Ho_Chi_Minh");

            // Thêm sự kiện vào CalendarProvider
            cr.insert(CalendarContract.Events.CONTENT_URI, values);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static void removeAllVaccineRegisterCalendar(Context context) {
        ContentResolver cr = context.getContentResolver();

        String selection = CalendarContract.Events.CALENDAR_ID + " = ?";

        String[] selectionArgs = new String[]{"1"};

        cr.delete(CalendarContract.Events.CONTENT_URI, selection, selectionArgs);
    }

    public static void syncReminderToCalendar(Context context){
        try{
            SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
            String user_id = sharedPreferences.getString("customer_id","");

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("notifications");
            Query query = databaseReference.orderByChild("user_id").equalTo(user_id);

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    notificationMessages.clear();
                    Log.i("NotificationActivity", "onDataChange: " + snapshot.getValue());
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        NotificationMessage notificationMessage = dataSnapshot.getValue(NotificationMessage.class);
                        notificationMessage.setNotification_id(dataSnapshot.getKey());
                        if (notificationMessage.getDate().after(new Date()) && ( notificationMessage.getTitle().contains("Nhắc nhở") || notificationMessage.getTitle().contains("Nhắc lại"))) {
                            notificationMessages.add(notificationMessage);
                        }
                    }
                    Log.i("NotificationActivity", "onDataChange: " + notificationMessages.size());
                    for (NotificationMessage notificationMessage : notificationMessages) {
                        addReminderEventToCalendar(context, notificationMessage);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }catch (Exception e){e.printStackTrace();}
    }

    private static void addReminderEventToCalendar(Context context, NotificationMessage notificationMessage) {
        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();

        try {
            // Parse thông tin từ JSON
            String title = notificationMessage.getTitle();
            Date vaccine_date__ = notificationMessage.getDate();
            long startTime = vaccine_date__.getTime();
            // Các thông tin khác nếu cần

            // Thêm thông tin vào ContentValues
            values.put(CalendarContract.Events.DTSTART, startTime);
            values.put(CalendarContract.Events.DTEND, startTime + (3 * 60 * 60 * 1000));
            values.put(CalendarContract.Events.TITLE, title);
            values.put(CalendarContract.Events.CALENDAR_ID, 1);
            values.put(CalendarContract.Events.EVENT_TIMEZONE, "Asia/Ho_Chi_Minh");

            // Thêm sự kiện vào CalendarProvider
            cr.insert(CalendarContract.Events.CONTENT_URI, values);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void removeAllReminderCalendar(Context context) {
        ContentResolver cr = context.getContentResolver();

        String selection = CalendarContract.Events.CALENDAR_ID + " = ?";

        String[] selectionArgs = new String[]{"1"};

        cr.delete(CalendarContract.Events.CONTENT_URI, selection, selectionArgs);
    }
}
