package com.fouomene.popularmovies.app.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.fouomene.popularmovies.app.database.AppDatabase;

// Make this class extend ViewModel ViewModelProvider.NewInstanceFactory
public class DetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    // Add two member variables. One for the database and one for the movieId
    private final AppDatabase mDb;
    private final int mMovieId;

    // Initialize the member variables in the constructor with the parameters received
    public DetailViewModelFactory(AppDatabase database, int movieId) {
        mDb = database;
        mMovieId = movieId;
    }

    // Uncomment the following method
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new DetailViewModel(mDb, mMovieId);
    }
}
