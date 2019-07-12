package com.udacity.popular_movies_2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.loader.app.LoaderManager.LoaderCallbacks;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.udacity.popular_movies_2.adapters.TrailerAdapter;
import com.udacity.popular_movies_2.database.AppDatabase;
import com.udacity.popular_movies_2.database.Movie;
import com.udacity.popular_movies_2.database.Trailer;
import com.udacity.popular_movies_2.databinding.ActivityMovieBinding;
import com.udacity.popular_movies_2.utils.Constants;
import com.udacity.popular_movies_2.utils.JsonUtils;
import com.udacity.popular_movies_2.utils.NetworkUtils;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

public class MovieActivity extends AppCompatActivity implements
        TrailerAdapter.TrailerAdapterOnClickHandler {
    ActivityMovieBinding mBinding;

    // Extra for the task ID to be received after rotation
    public static final String INSTANCE_MOVIE_ID = "instanceMovieId";
    private static final String THEMOVIEDB_URL = "https://api.themoviedb.org/3/movie/";

    private AppDatabase mDb;

    private static final SimpleDateFormat outputDateFormat = new SimpleDateFormat("d MMMM yyyy");

    private int mMovieId;
    private Movie mMovie;
    private boolean mExistsInDb;

    private static final int TRAILER_LOADER_ID = 1;
    final static String API_PARAM = "api_key";
    private static final String API_KEY = Constants.API_KEY;

    private RecyclerView mTrailerRecyclerView;
    private TrailerAdapter mTrailerAdapter;

    private LoaderCallbacks<List<Trailer>> trailerResultLoaderListener =
            new LoaderCallbacks<List<Trailer>>() {
                @NonNull
                @Override
                public Loader<List<Trailer>> onCreateLoader(int id, @Nullable Bundle args) {
                    return new AsyncTaskLoader<List<Trailer>>(MovieActivity.this) {
                        List<Trailer> mTrailersData = null;

                        @Override
                        protected void onStartLoading() {
                            if (mTrailersData != null) {
                                deliverResult(mTrailersData);
                            } else {
//                    mLoadingIndicator.setVisibility(View.VISIBLE);
                                forceLoad();
                            }
                        }

                        @Nullable
                        @Override
                        public List<Trailer> loadInBackground() {
                            HashMap<String, String> apiKeyValuePair = new HashMap<>();
                            apiKeyValuePair.put(API_PARAM, API_KEY);

                            URL trailersRequestUrl = NetworkUtils.buildUrl(
                                    THEMOVIEDB_URL + mMovieId + "/videos",
                                    apiKeyValuePair);

                            try {
                                String jsonTrailersResponse = NetworkUtils
                                        .getResponseFromHttpUrl(trailersRequestUrl);

                                List<Trailer> listTrailersData = JsonUtils
                                        .getTrailersFromJson(jsonTrailersResponse);

                                return listTrailersData;
                            } catch (Exception e) {
                                e.printStackTrace();
                                return null;
                            }
                        }

                        public void deliverResult(List<Trailer> data) {
                            mTrailersData = data;
                            super.deliverResult(data);
                        }
                    };
                }

                @Override
                public void onLoadFinished(@NonNull Loader<List<Trailer>> loader, List<Trailer> trailers) {
//        mLoadingIndicator.setVisibility(View.INVISIBLE);
//                    mTrailerAdapter.setTrailerData(trailers);
//                    getSupportLoaderManager().destroyLoader(TRAILER_LOADER_ID);
                    for (Trailer trailer : trailers) {
                        TrailerFragment trailerFragment = new TrailerFragment();
                        Bundle trailerBundle = new Bundle();
                        trailerBundle.putString("trailerTitle", trailer.getName());
                        trailerBundle.putString("trailerUrl", "");
                        trailerFragment.setArguments(trailerBundle);

                        FragmentManager fragmentManager = getSupportFragmentManager();

                        fragmentManager.beginTransaction()
                                .add(R.id.movie_trailers, trailerFragment)
                                .commit();
                    }
                }

                @Override
                public void onLoaderReset(@NonNull Loader<List<Trailer>> loader) {

                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie);

//        setContentView(R.layout.trailers);
        mTrailerRecyclerView = mBinding.recyclerviewTrailers;
        GridLayoutManager layoutManagerTrailers
                = new GridLayoutManager(this, 1, RecyclerView.VERTICAL, false);
        mTrailerRecyclerView.setLayoutManager(layoutManagerTrailers);
        mTrailerRecyclerView.setHasFixedSize(true);
        mTrailerAdapter = new TrailerAdapter(this);
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);

        mDb = AppDatabase.getInstance(getApplicationContext());

        Intent intent = getIntent();

        int loaderId = TRAILER_LOADER_ID;
        Bundle bundleForLoader = new Bundle();

        if (intent.hasExtra("movie")) {
            mMovie = intent.getExtras().getParcelable("movie");
            displayMovieDetails();
        }

        getSupportLoaderManager().initLoader(loaderId, bundleForLoader, trailerResultLoaderListener);
    }

    private void displayMovieDetails() {
        Picasso.get().load(mMovie.getMoviePoster()).into(mBinding.movieDetailPoster);
        getSupportActionBar().setTitle(mMovie.getTitle());
        mBinding.movieDetailReleaseDate.setText(outputDateFormat.format(mMovie.getReleaseDate()));
        mBinding.movieDetailVoteAverage.setText(mMovie.getVoteAverage() + "/10");
        mBinding.movieDetailPlotSynopsis.setText(mMovie.getPlotSynopsis());

        mBinding.buttonMarkAsFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMarkAsFavoriteButtonClicked();
            }
        });

        mMovieId = mMovie.getId();

        final LiveData<Movie> movieFromDb = mDb.movieDao().loadMovieById(mMovieId);
        movieFromDb.observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(Movie movie) {
                if (movie != null) {
                    mExistsInDb = true;
                    mBinding.buttonMarkAsFavorite.setBackgroundResource(R.color.colorFavoriteButtonMarked);
                } else {
                    mExistsInDb = false;
                    mBinding.buttonMarkAsFavorite.setBackgroundResource(android.R.drawable.btn_default);
                }
            }
        });
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
                if (mExistsInDb) {
                    mDb.movieDao().deleteMovie(mMovie);
                } else {
                    mDb.movieDao().insertMovie(mMovie);
                }
            }
        });
    }

    @Override
    public void onClick(Trailer trailer) {

    }
}
