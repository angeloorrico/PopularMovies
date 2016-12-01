package br.com.angeloorrico.popularmovies.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;

import br.com.angeloorrico.popularmovies.BuildConfig;
import br.com.angeloorrico.popularmovies.connection.ServicesEndpoints;
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
 * Created by Angelo on 30/11/2016.
 */

public class MovieService extends IntentService {

    private static final String LOG_TAG                  = MovieService.class.getSimpleName();
    private static final String PARAM_API_KEY            = "api_key";
    public static final String PARAM_SELECTED_FILTER     = "selected_filter";
    public static final String PARAM_MOST_POPULAR        = "popular";
    public static final String PARAM_TOP_RATED           = "top_rated";
    public static final String PARAM_TRAILERS            = "trailers";
    public static final String PARAM_REVIEWS             = "reviews";
    public static final String PARAM_MOVIE_ID            = "movie_id";
    public static final String BROADCAST_MOVIES_ACTION   = "broadcast_movies";
    public static final String BROADCAST_TRAILERS_ACTION = "broadcast_trailers";
    public static final String BROADCAST_REVIEWS_ACTION  = "broadcast_reviews";
    public static final String PARAM_BROADCAST_RESULT    = "result";

    private ServicesEndpoints mServicesEndpoints;

    private OkHttpClient mOkHttpClient;

    public MovieService() {
        super(MovieService.class.getName());
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
                        PARAM_API_KEY, BuildConfig.API_KEY).build();
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
    protected void onHandleIntent(Intent intent) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .client(getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            mServicesEndpoints = retrofit.create(ServicesEndpoints.class);

            Parcelable responseModel;
            Intent resultIntent;
            if (intent.getStringExtra(PARAM_SELECTED_FILTER).equals(PARAM_MOST_POPULAR) ||
                    intent.getStringExtra(PARAM_SELECTED_FILTER).equals(PARAM_TOP_RATED)) {
                responseModel =
                        mServicesEndpoints.fetchMoviesList(intent.getStringExtra(PARAM_SELECTED_FILTER),
                                Utils.getDeviceLocale()).execute().body();
                resultIntent = new Intent(BROADCAST_MOVIES_ACTION);
            } else if (intent.getStringExtra(PARAM_SELECTED_FILTER).equals(PARAM_TRAILERS)) {
                responseModel = mServicesEndpoints.fetchTrailers(
                        intent.getStringExtra(PARAM_MOVIE_ID),
                        BuildConfig.API_KEY,
                        Utils.getDeviceLocale()).execute().body();
                resultIntent = new Intent(BROADCAST_TRAILERS_ACTION);
            } else {
                responseModel = mServicesEndpoints.fetchReviews(
                        intent.getStringExtra(PARAM_MOVIE_ID),
                        BuildConfig.API_KEY).execute().body();
                resultIntent = new Intent(BROADCAST_REVIEWS_ACTION);
            }

            resultIntent.putExtra(PARAM_BROADCAST_RESULT, responseModel);
            LocalBroadcastManager.getInstance(this).sendBroadcast(resultIntent);
        } catch(Exception ex) {
            Log.e(LOG_TAG, ex.getMessage());
        }
    }

}