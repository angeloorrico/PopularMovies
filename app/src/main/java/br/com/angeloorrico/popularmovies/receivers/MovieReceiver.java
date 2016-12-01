package br.com.angeloorrico.popularmovies.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import br.com.angeloorrico.popularmovies.interfaces.MoviesConnector;
import br.com.angeloorrico.popularmovies.services.MovieService;

/**
 * Created by Angelo on 01/12/2016.
 */

public class MovieReceiver extends BroadcastReceiver {

    private MoviesConnector mConnector;

    public void setConnector(MoviesConnector connector) {
        this.mConnector = connector;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mConnector.onConnectionResult(
                intent.getParcelableExtra(MovieService.PARAM_BROADCAST_RESULT));
    }

}