package com.udacity.popular_movies_2.utils;

import com.udacity.popular_movies_2.database.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public final class JsonUtils {

    public static List getMoviesFromJson(String responseJsonStr)
            throws JSONException, ParseException {
        final String id = "id";
        final String title = "title";
        final String releaseDate = "release_date";
        final String moviePoster = "poster_path"; // url
        final String voteAverage = "vote_average";
        final String plotSynopsis = "overview";
        final SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        List movieList = new ArrayList<Movie>();
        JSONObject responseJsonObj = new JSONObject(responseJsonStr);

        // handle error?

        JSONArray moviesJsonArr = responseJsonObj.getJSONArray("results");

        if (moviesJsonArr.length() > 0) {
            for (int i = 0; i < moviesJsonArr.length(); i++) {
                JSONObject movieJsonObj = moviesJsonArr.getJSONObject(i);
                Movie movie = new Movie(
                        movieJsonObj.getInt(id),
                        movieJsonObj.getString(title),
                        inputDateFormat.parse(movieJsonObj.getString(releaseDate)),
                        movieJsonObj.getString(moviePoster),
                        movieJsonObj.getDouble(voteAverage),
                        movieJsonObj.getString(plotSynopsis)
                );
                movieList.add(movie);
            }
        }
        return movieList;
    }
}
