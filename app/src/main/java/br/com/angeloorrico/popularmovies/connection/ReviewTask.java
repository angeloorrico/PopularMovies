package br.com.angeloorrico.popularmovies.connection;

import android.os.AsyncTask;
import android.util.Log;

import br.com.angeloorrico.popularmovies.BuildConfig;
import br.com.angeloorrico.popularmovies.interfaces.MoviesConnector;
import br.com.angeloorrico.popularmovies.models.ReviewResponseModel;
import br.com.angeloorrico.popularmovies.utils.Utils;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Angelo on 25/10/2016.
 */

public class ReviewTask extends AsyncTask<String, Void, ReviewResponseModel> {

    private static final String LOG_TAG           = ReviewTask.class.getSimpleName();

    private ServicesEndpoints mServicesEndpoints;

    private OkHttpClient mOkHttpClient;

    private MoviesConnector mConnector;

    public void setConnector(MoviesConnector connector) {
        this.mConnector = connector;
    }

    public MoviesConnector getConnector() {
        return mConnector;
    }

    private OkHttpClient getOkHttpClient() {
        if (mOkHttpClient != null)
            return mOkHttpClient;

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.addInterceptor(new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY));

        mOkHttpClient = clientBuilder.build();
        return mOkHttpClient;
    }

    @Override
    protected void onPreExecute() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mServicesEndpoints = retrofit.create(ServicesEndpoints.class);
    }

    @Override
    protected ReviewResponseModel doInBackground(String... params) {
        try {
            return mServicesEndpoints.fetchReviews(params[0],
                    BuildConfig.API_KEY).execute().body();
        } catch(Exception ex) {
            Log.e(LOG_TAG, ex.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(ReviewResponseModel responseModel) {
        super.onPostExecute(responseModel);
        mConnector.onConnectionResult(responseModel);
    }

}