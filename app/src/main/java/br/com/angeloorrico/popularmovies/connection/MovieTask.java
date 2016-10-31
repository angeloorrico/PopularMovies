package br.com.angeloorrico.popularmovies.connection;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import br.com.angeloorrico.popularmovies.BuildConfig;
import br.com.angeloorrico.popularmovies.interfaces.MoviesConnector;
import br.com.angeloorrico.popularmovies.models.MovieResponseModel;
import br.com.angeloorrico.popularmovies.utils.Utils;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Angelo on 25/10/2016.
 */

public class MovieTask extends AsyncTask<String, Void, MovieResponseModel> {

    private static final String LOG_TAG           = MovieTask.class.getSimpleName();
    private static final String PARAM_API_KEY     = "api_key";
    public static final String PARAM_MOST_POPULAR = "popular";
    public static final String PARAM_TOP_RATED    = "top_rated";

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
        clientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                HttpUrl url = request.url().newBuilder().addQueryParameter(
                        MovieTask.PARAM_API_KEY, BuildConfig.API_KEY).build();
                request = request.newBuilder().url(url).build();
                return chain.proceed(request);
            }
        });
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
    protected MovieResponseModel doInBackground(String... params) {
        try {
            return mServicesEndpoints.fetchMoviesList(params[0],
                    Utils.getDeviceLocale()).execute().body();
        } catch(Exception ex) {
            Log.e(LOG_TAG, ex.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(MovieResponseModel movieResponseModel) {
        super.onPostExecute(movieResponseModel);
        mConnector.onConnectionResult(movieResponseModel);
    }

}