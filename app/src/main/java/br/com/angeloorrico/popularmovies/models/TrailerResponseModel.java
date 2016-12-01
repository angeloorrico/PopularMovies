package br.com.angeloorrico.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Angelo on 15/11/2016.
 */

public class TrailerResponseModel implements Parcelable {

    private List<TrailerModel> results;

    protected TrailerResponseModel(Parcel in) {
        results = in.createTypedArrayList(TrailerModel.CREATOR);
    }

    public static final Creator<TrailerResponseModel> CREATOR = new Creator<TrailerResponseModel>() {
        @Override
        public TrailerResponseModel createFromParcel(Parcel in) {
            return new TrailerResponseModel(in);
        }

        @Override
        public TrailerResponseModel[] newArray(int size) {
            return new TrailerResponseModel[size];
        }
    };

    public List<TrailerModel> getResults() {
        return this.results;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(results);
    }
}