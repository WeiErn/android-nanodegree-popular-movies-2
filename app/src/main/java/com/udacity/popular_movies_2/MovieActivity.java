package com.udacity.popular_movies_2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;
import com.udacity.popular_movies_2.database.Movie;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class MovieActivity extends AppCompatActivity {
    private ImageView mMoviePosterImageView;
    private TextView mMovieTitleTextView;
    private TextView mMovieReleaseDateTextView;
    private TextView mMovieVoteAverageTextView;
    private TextView mMoviePlotSynopsisTextView;
    private static final SimpleDateFormat outputDateFormat = new SimpleDateFormat("d MMMM yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        mMoviePosterImageView = findViewById(R.id.movie_detail_poster);
        mMovieTitleTextView = findViewById(R.id.movie_detail_title);
        mMovieReleaseDateTextView = findViewById(R.id.movie_detail_release_date);
        mMovieVoteAverageTextView = findViewById(R.id.movie_detail_vote_average);
        mMoviePlotSynopsisTextView = findViewById(R.id.movie_detail_plot_synopsis);

        Intent intent = getIntent();

        if (intent.hasExtra("movie")) {
            Movie movie = intent.getExtras().getParcelable("movie");
            Picasso.get().load(movie.getMoviePoster()).into(mMoviePosterImageView);
            getSupportActionBar().setTitle(movie.getTitle());
            mMovieTitleTextView.setText(movie.getTitle());
            mMovieReleaseDateTextView.setText(outputDateFormat.format(movie.getReleaseDate()));
            mMovieVoteAverageTextView.setText(movie.getVoteAverage() + "/10");
            mMoviePlotSynopsisTextView.setText(movie.getPlotSynopsis());
        }
    }
}
