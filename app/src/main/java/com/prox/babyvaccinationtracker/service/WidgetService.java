package com.prox.babyvaccinationtracker.service;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.i("Widget", "onGetViewFactory");
        return new WidgetRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
