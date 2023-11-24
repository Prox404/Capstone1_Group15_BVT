package com.vaccinecenter.babyvaccinationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.vaccinecenter.babyvaccinationtracker.Adapter.NotificationAdapter;
import com.vaccinecenter.babyvaccinationtracker.model.NotificationMessage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    RecyclerView recylerViewNotification;
    TextView textViewClearNotification;
    private String user_id;
    NotificationAdapter notificationAdapter;
    List<NotificationMessage> notificationList = new ArrayList<>();

    View empty_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        recylerViewNotification = findViewById(R.id.recylerViewNotification);
        textViewClearNotification = findViewById(R.id.textViewClearNotification);
        empty_layout = findViewById(R.id.emptyLayout);

        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        user_id = sharedPreferences.getString("center_id", "");

        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference("notifications");
        Query query = databaseReference.orderByChild("user_id").equalTo(user_id);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(NotificationActivity.this);
        recylerViewNotification.setLayoutManager(linearLayoutManager);
        query.addValueEventListener(new ValueEventListener() {
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
                if (notificationList.size() > 0) {
                    empty_layout.setVisibility(View.GONE);
                } else {
                    empty_layout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        textViewClearNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("notifications");
                Query query = databaseReference.orderByChild("user_id").equalTo(user_id);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue() != null) {
                            Log.i("NotificationActivity", "onDataChange: " + snapshot.getValue());
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                NotificationMessage notificationMessage = dataSnapshot.getValue(NotificationMessage.class);
                                notificationMessage.setNotification_id(dataSnapshot.getKey());
                                if (notificationMessage.getDate().before(new Date())) {
                                    dataSnapshot.getRef().removeValue();
                                }
                            }
                        } else {
                            Log.i("NotificationActivity", "onDataChange: " + snapshot.getValue());
                        }
                        notificationList.clear();
                        notificationAdapter.notifyDataSetChanged();
                        empty_layout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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