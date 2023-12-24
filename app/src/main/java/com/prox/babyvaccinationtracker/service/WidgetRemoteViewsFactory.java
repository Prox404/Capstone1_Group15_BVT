package com.prox.babyvaccinationtracker.service;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.prox.babyvaccinationtracker.R;
import com.prox.babyvaccinationtracker.model.Vaccination_Registration;

import java.util.ArrayList;
import java.util.List;

public class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    Context context;
    List<Vaccination_Registration> vaccination_registrations = new ArrayList<>();
    Gson gson = new Gson();
    public WidgetRemoteViewsFactory(Context applicationContext, Intent intent) {
        this.context = applicationContext;

        String data = intent.getStringExtra("data");
        vaccination_registrations = (List<Vaccination_Registration>) gson.fromJson(data, List.class);
        Log.i("Widget", "WidgetRemoteViewsFactory: " + data);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        Log.i("Widget", "onDataSetChanged: " + "onDataSetChanged");
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return vaccination_registrations.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.item_vaccine_registration_timeline);
//        remoteViews.setTextViewText(android.R.id.text1, "Hello");

        Object object = vaccination_registrations.get(i);
//        Log.i("Widget", "getViewAt: " + i + object.toString());
        Vaccination_Registration registration = gson.fromJson(gson.toJson(object), Vaccination_Registration.class);

//        Log.i("Widget", "getViewAt: " + registration.getVaccine().getVaccine_name());
        String Date = registration.getRegist_created_at().split("/")[0] + "/" + registration.getRegist_created_at().split("/")[1];
        remoteViews.setTextViewText(R.id.textViewDate, Date);
        remoteViews.setTextViewText(R.id.textViewVaccineName, registration.getVaccine().getVaccine_name());
        remoteViews.setTextViewText(R.id.textViewBabyName, registration.getBaby().getBaby_name());
        remoteViews.setTextViewText(R.id.textViewCenterName, registration.getCenter().getCenter_name());
        remoteViews.setTextViewText(R.id.textViewCenterAddress, registration.getCenter().getCenter_address());
        remoteViews.setTextViewText(R.id.textViewWorkingTime, registration.getCenter().getWork_time().split(",")[1].trim());
        remoteViews.setTextViewText(R.id.textViewPrice, registration.getVaccine().getPrice());

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return new RemoteViews(context.getPackageName(), R.layout.loading_widget);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
