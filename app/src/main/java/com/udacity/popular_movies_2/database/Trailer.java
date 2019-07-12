package com.udacity.popular_movies_2.database;

import android.os.Parcel;
import android.os.Parcelable;

public class Trailer implements Parcelable {

    private String name;
    private String url;

    public String getName() { return name; }

    public String getUrl() { return url; }

    public Trailer(String name, String url) {
        this.name = name;
        this.url = url;
    }

    private Trailer(Parcel in) {
        name = in.readString();
        url = in.readString();
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(url);
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel parcel) {
            return new Trailer(parcel);
        }

        @Override
        public Trailer[] newArray(int i) { return new Trailer[i]; }
    };
}
