package com.udacity.popular_movies_2.database;

import android.os.Parcel;
import android.os.Parcelable;

public class Review implements Parcelable {

    private String author;
    private String content;
    private String url;


    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() { return url; }

    public Review(String author, String content, String url) {
        this.author = author;
        this.content = content;
        this.url = url;
    }

    private Review(Parcel in) {
        author = in.readString();
        content = in.readString();
        url = in.readString();
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(url);
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel parcel) { return new Review(parcel); }

        @Override
        public Review[] newArray(int i) { return new Review[i]; }
    };
}
