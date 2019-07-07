package com.udacity.popular_movies_2;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.udacity.popular_movies_2.database.AppDatabase;
import com.udacity.popular_movies_2.database.Movie;

public class AddMovieViewModel extends ViewModel {

    private LiveData<Movie> movie;

    public AddMovieViewModel(AppDatabase database, int movieId) {
        movie = database.movieDao().loadMovieById(movieId);
    }

    public LiveData<Movie> getMovie() { return movie; }
}
