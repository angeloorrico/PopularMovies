package br.com.angeloorrico.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Angelo on 15/11/2016.
 */

public class ReviewResponseModel implements Parcelable {

    private List<ReviewModel> results;

    protected ReviewResponseModel(Parcel in) {
        results = in.createTypedArrayList(ReviewModel.CREATOR);
    }

    public static final Creator<ReviewResponseModel> CREATOR = new Creator<ReviewResponseModel>() {
        @Override
        public ReviewResponseModel createFromParcel(Parcel in) {
            return new ReviewResponseModel(in);
        }

        @Override
        public ReviewResponseModel[] newArray(int size) {
            return new ReviewResponseModel[size];
        }
    };

    public List<ReviewModel> getResults() {
        return results;
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
