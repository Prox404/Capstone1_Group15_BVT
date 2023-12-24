package com.prox.babyvaccinationtracker.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.prox.babyvaccinationtracker.util.NetworkUtils;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (isInitialStickyBroadcast()) {
            return;
        }
        Log.i("Network change", "onReceive: call" );

        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connMgr.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            Log.i("Network change", "Connected: " + isConnected);
        } else {
            Log.i("Network change", "Not Connected: " + isConnected);
            Intent networkChangeIntent = new Intent("network_change");
            LocalBroadcastManager.getInstance(context).sendBroadcast(networkChangeIntent);
        }
    }
}