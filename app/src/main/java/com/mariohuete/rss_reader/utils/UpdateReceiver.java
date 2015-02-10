package com.mariohuete.rss_reader.utils;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.mariohuete.rss_reader.DetailActivity;
import com.mariohuete.rss_reader.MainActivity;
import com.mariohuete.rss_reader.R;
import com.mariohuete.rss_reader.adapters.ModelAdapter;
import com.mariohuete.rss_reader.models.Model;

import java.util.Collections;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by mariobama on 10/02/15.
 */
public class UpdateReceiver extends BroadcastReceiver {

    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        Common.connected = getConnectivityStatus(context) == 1 || getConnectivityStatus(context) == 2;
        Log.i("CONNECTED", ""+Common.connected);
    }

}