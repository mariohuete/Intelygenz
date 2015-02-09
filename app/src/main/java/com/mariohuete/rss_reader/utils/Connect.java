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

    //ATTRIBUTES:
    private static ConnectivityManager cm;
    private static NetworkInfo netInfo;

    //METHODS:
    public static boolean isOnline(Context context) {
        // Check if there's an available connection
        cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public static void showToast(Context context) {
        // Show toast when try to access web browser but ther'es no internet connection
        Toast.makeText(context, context.getResources().getString(R.string.no_conn), Toast.LENGTH_LONG).show();
    }
}
