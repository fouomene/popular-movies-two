package com.fouomene.popularmovies.app;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.widget.Toast;

import com.fouomene.popularmovies.app.adapter.MovieEntryAdapter;
import com.fouomene.popularmovies.app.database.AppDatabase;
import com.fouomene.popularmovies.app.database.MovieEntry;
import com.fouomene.popularmovies.app.utils.AppExecutors;
import com.fouomene.popularmovies.app.viewmodel.FavoriteViewModel;

import java.util.Collections;
import java.util.List;


public class FavoriteActivity extends AppCompatActivity implements MovieEntryAdapter.ItemClickListener{

    // Constant for logging
    private static final String TAG = FavoriteActivity.class.getSimpleName();

    private Toolbar toolbar;

    private List<MovieEntry> movieEntriesList = Collections.emptyList();

    private final  int numberOfColumnsGrid = 2;
    private RecyclerView mRecyclerViewFavorite;
    private MovieEntryAdapter mAdapter;
    // Member variable for the Database
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // to display Icon launcher
        ActionBar actionBar = getSupportActionBar();

        //icon back
        actionBar.setDisplayHomeAsUpEnabled(true);

        // set title bar
        /* actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.action_bar_title_detail);*/

        // Set the RecyclerView to its corresponding view
        mRecyclerViewFavorite = findViewById(R.id.recyclerViewFavorites);

        // Set the layout for the RecyclerView to be a linear layout, which measures and
        // positions items within a RecyclerView into a linear list


        mRecyclerViewFavorite.setLayoutManager( new GridLayoutManager(getApplicationContext(), numberOfColumnsGrid));

        // Initialize the adapter and attach it to the RecyclerView
        mAdapter = new MovieEntryAdapter(getApplicationContext(), this);
        mRecyclerViewFavorite.setAdapter(mAdapter);

        mDb = AppDatabase.getInstance(getApplicationContext());
        setupViewModel();

               /*
         Add a touch helper to the RecyclerView to recognize when a user swipes to delete an item.
         An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
         and uses callbacks to signal when a user is performing these actions.
         */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Here is where you'll implement swipe to delete
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<MovieEntry> movies = mAdapter.getMovieEntries();
                        mDb.movieDao().deleteMovie(movies.get(position));
                    }
                });
            }
        }).attachToRecyclerView(mRecyclerViewFavorite);

        setTitle(getResources().getText(R.string.my_favorite));

    }
    private void setupViewModel() {
        FavoriteViewModel viewModel = ViewModelProviders.of(this).get(FavoriteViewModel.class);
        viewModel.getMovies().observe(this, new Observer<List<MovieEntry>>() {
            @Override
            public void onChanged(@Nullable List<MovieEntry> movieEntries) {
                Log.d(TAG, "Updating list of movies farites from LiveData in ViewModel");
                if (!movieEntries.isEmpty()){
                     mAdapter.setMovieEntries(movieEntries);
                }else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_favorite), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onItemClickListener(MovieEntry itemMovieEntry) {
        // When the user clicks on the movie

    }
}
