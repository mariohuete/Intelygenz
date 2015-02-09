package com.mariohuete.rss_reader.utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.mariohuete.rss_reader.R;

/**
 * Created by mariobama on 09/02/15.
 */
public class Connect {

    //METHODS:
    public static boolean isOnline(Context context) {
        // Check if there's an available connection
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static void showToast(Context context) {
        // Show toast when try to access web browser but ther'es no internet connection
        Toast.makeText(context, context.getResources().getString(R.string.no_conn), Toast.LENGTH_LONG).show();
    }

}
