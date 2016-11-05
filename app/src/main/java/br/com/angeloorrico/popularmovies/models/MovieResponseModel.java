package br.com.angeloorrico.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Angelo on 25/10/2016.
 */

public class MovieResponseModel implements Parcelable {

    private ArrayList<MovieModel> results;

    protected MovieResponseModel(Parcel in) {
        results = in.createTypedArrayList(MovieModel.CREATOR);
    }

    public ArrayList<MovieModel> getResults() {
        return results;
    }

    public void setResults(ArrayList<MovieModel> results) {
        this.results = results;
    }

    public static final Creator<MovieResponseModel> CREATOR = new Creator<MovieResponseModel>() {
        @Override
        public MovieResponseModel createFromParcel(Parcel in) {
            return new MovieResponseModel(in);
        }

        @Override
        public MovieResponseModel[] newArray(int size) {
            return new MovieResponseModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(results);
    }

}