package com.udacity.popular_movies_2.utils;

import com.udacity.popular_movies_2.database.Movie;
import com.udacity.popular_movies_2.database.Review;
import com.udacity.popular_movies_2.database.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public final class JsonUtils {

    public static final String baseUrl = "http://image.tmdb.org/t/p/w500";
    public static final String youtubeBaseUrl = "https://www.youtube.com/watch?v=";

    public static List getMoviesFromJson(String responseJsonStr)
            throws JSONException, ParseException {

        final SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        List movieList = new ArrayList<Movie>();
        JSONObject responseJsonObj = new JSONObject(responseJsonStr);

        // handle error?

        JSONArray moviesJsonArr = responseJsonObj.getJSONArray("results");

        if (moviesJsonArr.length() > 0) {
            for (int i = 0; i < moviesJsonArr.length(); i++) {
                JSONObject movieJsonObj = moviesJsonArr.getJSONObject(i);
                Movie movie = new Movie(
                        movieJsonObj.getInt("id"),
                        movieJsonObj.getString("title"),
                        inputDateFormat.parse(movieJsonObj.getString("release_date")),
                        baseUrl + movieJsonObj.getString("poster_path"),
                        movieJsonObj.getDouble("vote_average"),
                        movieJsonObj.getString("overview")
                );
                movieList.add(movie);
            }
        }
        return movieList;
    }

    public static List getTrailersFromJson(String responseJsonStr) throws JSONException {
        List trailerList = new ArrayList<>();
        JSONObject responseJsonObj = new JSONObject(responseJsonStr);
        JSONArray trailersJsonArr = responseJsonObj.getJSONArray("results");

        if (trailersJsonArr.length() > 0) {
            for (int i = 0; i < trailersJsonArr.length(); i++) {
                JSONObject trailerJsonObj = trailersJsonArr.getJSONObject(i);
                Trailer trailer = new Trailer(
                        trailerJsonObj.getString("name"),
                        youtubeBaseUrl + trailerJsonObj.getString("key")
                );
                trailerList.add(trailer);
            }
        }
        return trailerList;
    }

    public static List getReviewsFromJson(String responseJsonStr) throws JSONException {
        List reviewList = new ArrayList<>();
        JSONObject responseJsonObj = new JSONObject(responseJsonStr);
        JSONArray reviewsJsonArr = responseJsonObj.getJSONArray("results");

        if (reviewsJsonArr.length() > 0) {
            for (int i = 0; i < reviewsJsonArr.length(); i++) {
                JSONObject reviewJsonObj = reviewsJsonArr.getJSONObject(i);
                Review review = new Review(
                        reviewJsonObj.getString("author"),
                        reviewJsonObj.getString("content"),
                        reviewJsonObj.getString("url")
                );
                reviewList.add(review);
            }
        }
        return reviewList;
    }
}
