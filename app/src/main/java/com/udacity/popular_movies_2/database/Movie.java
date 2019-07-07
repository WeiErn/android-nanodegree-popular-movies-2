package com.udacity.popular_movies_2.database;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity(tableName = "movie")
public class Movie implements Parcelable {

    @Ignore
    private static final String baseUrl = "http://image.tmdb.org/t/p/w500";

    @PrimaryKey
    private int id;
    private String title;
    @ColumnInfo(name = "release_date")
    private Date releaseDate;
    @ColumnInfo(name = "movie_poster")
    private String moviePoster; // url
    @ColumnInfo(name = "vote_average")
    private double voteAverage;
    @ColumnInfo(name = "plot_synopsis")
    private String plotSynopsis;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public Movie(int id, String title, Date releaseDate, String moviePoster, double voteAverage, String plotSynopsis) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.moviePoster = baseUrl + moviePoster;
        this.voteAverage = voteAverage;
        this.plotSynopsis = plotSynopsis;
    }

    private Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        releaseDate = new Date(in.readLong());
        moviePoster = in.readString();
        voteAverage = in.readDouble();
        plotSynopsis = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeLong(releaseDate.getTime());
        dest.writeString(moviePoster);
        dest.writeDouble(voteAverage);
        dest.writeString(plotSynopsis);
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }
    };
}
