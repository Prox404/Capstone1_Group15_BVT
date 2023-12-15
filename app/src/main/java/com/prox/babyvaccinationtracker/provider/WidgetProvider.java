package com.prox.babyvaccinationtracker.provider;

import android.app.PendingIntent;
import android.appwidget.AppWidgetProvider;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.prox.babyvaccinationtracker.R;
import com.prox.babyvaccinationtracker.model.Vaccination_Registration;
import com.prox.babyvaccinationtracker.service.WidgetService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WidgetProvider extends AppWidgetProvider {
    List<Vaccination_Registration> vaccination_registrations = new ArrayList<>();

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String id_vaccine_center = sharedPreferences.getString("customer_id","");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Vaccination_Registration");
        Query query = databaseReference.orderByChild("cus/customer_id").equalTo(id_vaccine_center);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    vaccination_registrations.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Vaccination_Registration vaccination_registration = dataSnapshot.getValue(Vaccination_Registration.class);
                        if(vaccination_registration.getStatus() == 1){
                            vaccination_registration.setRegist_id(dataSnapshot.getKey());
                            vaccination_registrations.add(vaccination_registration);
                        }
                    }
                    updateWidget(context, appWidgetManager, appWidgetIds, vaccination_registrations);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private void updateWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, List<Vaccination_Registration> vaccination_registrations_) {
        final int N = appWidgetIds.length;

        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_vaccine_register_widget_layout);
            Gson gson = new Gson();
            String json = gson.toJson(vaccination_registrations_);
            Log.i("Widget", "updateWidget: " + vaccination_registrations_.size());
            Intent intent = new Intent(context, WidgetService.class);
            intent.putExtra("data", json);
            views.setRemoteAdapter(R.id.listViewTimeline, intent);
            views.setEmptyView(R.id.listViewTimeline, R.id.textViewEmpty);

            //intent to open schedule injection activity
            views.setOnClickPendingIntent(R.id.textViewEmpty, getPendingSelfIntent(context, "open_schedule_injection_activity"));
            appWidgetManager.updateAppWidget(appWidgetId, views);
//            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.listViewTimeline);
        }
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if(intent.getAction().equals("open_schedule_injection_activity")){
            Intent intent1 = new Intent(context, com.prox.babyvaccinationtracker.Schedule_an_injection.class);
            intent1.putExtra("open_schedule_injection_activity", true);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        }
    }
}
