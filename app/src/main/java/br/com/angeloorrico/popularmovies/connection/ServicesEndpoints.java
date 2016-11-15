package br.com.angeloorrico.popularmovies.connection;

import br.com.angeloorrico.popularmovies.models.MovieResponseModel;
import br.com.angeloorrico.popularmovies.models.ReviewResponseModel;
import br.com.angeloorrico.popularmovies.models.TrailerResponseModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Angelo on 25/10/2016.
 */

public interface ServicesEndpoints {

    @GET("movie/{sortBy}")
    Call<MovieResponseModel> fetchMoviesList(@Path("sortBy") String sortBy,
                                            @Query("language") String language);

    @GET("movie/{id}/videos")
    Call<TrailerResponseModel> fetchTrailers(@Path("id") String movieId,
                                             @Query("api_key") String apiKey,
                                             @Query("language") String language);

    @GET("movie/{id}/reviews")
    Call<ReviewResponseModel> fetchReviews(@Path("id") String movieId,
                                           @Query("api_key") String apiKey,
                                           @Query("language") String language);

}
