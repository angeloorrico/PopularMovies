package br.com.angeloorrico.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Angelo on 18/11/2016.
 */

public class TrailerModel implements Parcelable {

    public static String TRAILER_PARCELABLE_PARAM = "TrailerExtra";

    private String id;

    private String key;

    private String name;

    public TrailerModel() {

    }

    protected TrailerModel(Parcel in) {
        id = in.readString();
        key = in.readString();
        name = in.readString();
    }

    public static final Creator<TrailerModel> CREATOR = new Creator<TrailerModel>() {
        @Override
        public TrailerModel createFromParcel(Parcel in) {
            return new TrailerModel(in);
        }

        @Override
        public TrailerModel[] newArray(int size) {
            return new TrailerModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(key);
        parcel.writeString(name);
    }
}
