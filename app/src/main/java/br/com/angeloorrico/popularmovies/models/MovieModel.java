package br.com.angeloorrico.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.angeloorrico.popularmovies.utils.Utils;

/**
 * Created by Angelo on 25/10/2016.
 */

public class MovieModel implements Parcelable {

    public static String MOVIE_PARCELABLE_PARAM          = "MovieExtra";
    public static String SELECTED_MOVIE_PARCELABLE_PARAM = "IndexExtra";

    private int id;

    private String title;

    private Date release_date;

    private String poster_path;

    private String backdrop_path;

    private String vote_average;

    private String overview;

    private boolean isFavorite;

    public MovieModel() {
    }

    protected MovieModel(Parcel in) {
        id = in.readInt();
        title = in.readString();
        release_date = (Date) in.readSerializable();
        poster_path = in.readString();
        backdrop_path = in.readString();
        vote_average = in.readString();
        overview = in.readString();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        SimpleDateFormat sdf;
        if (Utils.getDeviceLocale().equals("pt-BR"))
            sdf = new SimpleDateFormat("dd/MM/yyyy");
        else
            sdf = new SimpleDateFormat("MM/dd/yyyy");
        return sdf.format(release_date);
    }

    public void setReleaseDate(Date releaseDate) {
        this.release_date = releaseDate;
    }

    public String getPosterPath() {
        return poster_path;
    }

    public void setPosterPath(String poster) {
        poster_path = poster;
    }

    public String getVoteAverage() {
        return vote_average;
    }

    public void setVoteAverage(String voteAverage) {
        this.vote_average = voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getBackdropPath() {
        return backdrop_path;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdrop_path = backdropPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static final Creator<MovieModel> CREATOR = new Creator<MovieModel>() {
        @Override
        public MovieModel createFromParcel(Parcel in) {
            return new MovieModel(in);
        }

        @Override
        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeSerializable(release_date);
        parcel.writeString(poster_path);
        parcel.writeString(backdrop_path);
        parcel.writeString(vote_average);
        parcel.writeString(overview);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}