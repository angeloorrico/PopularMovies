package br.com.angeloorrico.popularmovies.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.Locale;

/**
 * Created by Angelo on 25/10/2016.
 */

public class Utils {

    public static boolean hasInternetConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static String getDeviceLocale() {
        return Locale.getDefault().toString().equalsIgnoreCase(
                "pt_BR") ? "pt-BR" : Locale.getDefault().toString();
    }

}