package com.fouomene.popularmovies.app.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.fouomene.popularmovies.app.database.AppDatabase;
import com.fouomene.popularmovies.app.database.MovieEntry;


// Make this class extend ViewModel
public class DetailViewModel extends ViewModel {

    // Add a movie member variable for the MovieEntry object wrapped in a LiveData
    private LiveData<MovieEntry> movie;

    // Create a constructor where you call loadMovieById_movie of the movieDao to initialize the movie variable
    // Note: The constructor should receive the database and the movieId
    public DetailViewModel(AppDatabase database, int movieId) {
        movie = database.movieDao().loadMovieById_movie(movieId);
    }

    // Create a getter for the movie variable
    public LiveData<MovieEntry> getMovie() {
        return movie;
    }
}
