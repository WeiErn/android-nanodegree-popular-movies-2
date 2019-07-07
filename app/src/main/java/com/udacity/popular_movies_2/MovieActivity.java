package com.udacity.popular_movies_2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;

import com.squareup.picasso.Picasso;
import com.udacity.popular_movies_2.database.AppDatabase;
import com.udacity.popular_movies_2.database.Movie;
import com.udacity.popular_movies_2.databinding.ActivityMovieBinding;

import java.text.SimpleDateFormat;

public class MovieActivity extends AppCompatActivity {
    ActivityMovieBinding mBinding;

    // Extra for the task ID to be received after rotation
    public static final String INSTANCE_MOVIE_ID = "instanceMovieId";

    private AppDatabase mDb;

    private static final SimpleDateFormat outputDateFormat = new SimpleDateFormat("d MMMM yyyy");
    // Constant for default task id to be used when not in update mode
    private static final int DEFAULT_MOVIE_ID = -1;

    private int mMovieId = DEFAULT_MOVIE_ID;

    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie);

        mDb = AppDatabase.getInstance(getApplicationContext());

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_MOVIE_ID)) {
            mMovieId = savedInstanceState.getInt(INSTANCE_MOVIE_ID, DEFAULT_MOVIE_ID);
        }

        Intent intent = getIntent();

        if (intent.hasExtra("movie")) {
            mMovie = intent.getExtras().getParcelable("movie");
            displayMovieDetails(mMovie);
        }
    }

    private void displayMovieDetails(Movie movie) {
        Picasso.get().load(movie.getMoviePoster()).into(mBinding.movieDetailPoster);
        getSupportActionBar().setTitle(movie.getTitle());
        mBinding.movieDetailReleaseDate.setText(outputDateFormat.format(movie.getReleaseDate()));
        mBinding.movieDetailVoteAverage.setText(movie.getVoteAverage() + "/10");
        mBinding.movieDetailPlotSynopsis.setText(movie.getPlotSynopsis());

        mBinding.buttonMarkAsFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMarkAsFavoriteButtonClicked();
            }
        });

        mMovieId = movie.getId();
        if (existInDb(mMovieId)) {
            mBinding.buttonMarkAsFavorite.setBackgroundResource(R.color.colorFavoriteButtonMarked);
        }
    }

    private boolean existInDb(final int movieId) {
        LiveData<Movie> movie = mDb.movieDao().loadMovieById(movieId);
        if (movie != null) { return true; }
        return false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(INSTANCE_MOVIE_ID, mMovieId);
        super.onSaveInstanceState(outState);
    }


    private void onMarkAsFavoriteButtonClicked() {

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.movieDao().insertMovie(mMovie);
            }
        });
    }
}
