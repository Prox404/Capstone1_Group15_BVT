package com.prox.babyvaccinationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prox.babyvaccinationtracker.adapter.NotificationAdapter;
import com.prox.babyvaccinationtracker.model.NotificationMessage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    RecyclerView recylerViewNotification;
    private String user_id;
    NotificationAdapter notificationAdapter;
    List<NotificationMessage> notificationList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        recylerViewNotification = findViewById(R.id.recylerViewNotification);

        SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        user_id = sharedPreferences.getString("customer_id", "");
        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference("notifications").child(user_id);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(NotificationActivity.this);
        recylerViewNotification.setLayoutManager(linearLayoutManager);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.getValue() != null) {
                    Log.i("NotificationActivity", "onDataChange: " + snapshot.getValue());
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        NotificationMessage notificationMessage = dataSnapshot.getValue(NotificationMessage.class);
                        notificationMessage.setNotification_id(dataSnapshot.getKey());
                        if (notificationMessage.getDate().before(new Date())) {
                            notificationList.add(notificationMessage);
                        }
                    }

                } else {
                    Log.i("NotificationActivity", "onDataChange: " + snapshot.getValue());
                }
                notificationList = reverseArrayList((ArrayList<NotificationMessage>) notificationList);
                notificationAdapter = new NotificationAdapter(notificationList);
                recylerViewNotification.setAdapter(notificationAdapter);
                notificationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public ArrayList<NotificationMessage> reverseArrayList(ArrayList<NotificationMessage> notificationList) {
        ArrayList<NotificationMessage> reversedList = new ArrayList<>();
        for (int i = notificationList.size() - 1; i >= 0; i--) {
            reversedList.add(notificationList.get(i));
        }
        return reversedList;
    }
}