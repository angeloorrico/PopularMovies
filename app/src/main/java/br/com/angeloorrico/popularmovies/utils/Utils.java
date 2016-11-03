package br.com.angeloorrico.popularmovies.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.Locale;

import br.com.angeloorrico.popularmovies.BuildConfig;

/**
 * Created by Angelo on 25/10/2016.
 */

public class Utils {

    private  static final String POSTER_SIZE   = "w342";
    private  static final String BACKDROP_SIZE = "w780";

    public static boolean hasInternetConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static String getDeviceLocale() {
        return Locale.getDefault().toString().equalsIgnoreCase(
                "pt_BR") ? "pt-BR" : Locale.getDefault().toString();
    }

    public static String getImageURL(boolean isPoster) {
        return BuildConfig.POSTER_BASE_URL + (isPoster ? POSTER_SIZE : BACKDROP_SIZE);
    }

}