package com.fouomene.popularmovies.app;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fouomene.popularmovies.app.adapter.ReviewAdapter;
import com.fouomene.popularmovies.app.adapter.TrailerAdapter;
import com.fouomene.popularmovies.app.api.TheMovieDBService;
import com.fouomene.popularmovies.app.database.AppDatabase;
import com.fouomene.popularmovies.app.database.MovieEntry;
import com.fouomene.popularmovies.app.model.Movie;
import com.fouomene.popularmovies.app.model.Reviews;
import com.fouomene.popularmovies.app.model.Trailers;
import com.fouomene.popularmovies.app.utils.AppExecutors;
import com.fouomene.popularmovies.app.utils.Utility;
import com.fouomene.popularmovies.app.viewmodel.DetailViewModel;
import com.fouomene.popularmovies.app.viewmodel.DetailViewModelFactory;
import com.squareup.picasso.Picasso;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.fouomene.popularmovies.app.BuildConfig.API_KEY;

public class DetailActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private static final String MOVIE_SHARE_HASHTAG = " #PopularMovies";
    private String mFirstKeyURLTrailerYoutube;
    private ShareActionProvider mShareActionProvider;

    @BindView(R.id.title_movie) TextView titleMovie;
    @BindView(R.id.vote_average_movie) TextView  voteAverageMovie;
    @BindView(R.id.popularity_movie) TextView popularityMovie;
    @BindView(R.id.adulte_movie) TextView adulteMovie;
    @BindView(R.id.release_date_movie) TextView releaseDateMovie;
    @BindView(R.id.overview_movie) TextView overviewMovie;
    @BindView(R.id.poster_detail_movie) ImageView posterDetailMovie;
    @BindView(R.id.backdrop_detail_movie) ImageView backdropDetailMovie;
    @BindView(R.id.mark_favorie_btn) Button markFavorieBtn;

    private Movie movieSelect;
    private MovieEntry movieEntryFavorite;

    // Member variable for the Database
    private AppDatabase mDb;

    private RecyclerView mRecyclerViewTrailer;
    private TrailerAdapter mAdapterTrailer;
    private RecyclerView mRecyclerViewReview;
    private ReviewAdapter mAdapterReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

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

        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        if (savedInstanceState == null) {
            movieSelect = (Movie) intent.getSerializableExtra(Utility.EXTRA_DETAIL_MOVIE) ;
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(Utility.EXTRA_DETAIL_MOVIE)) {
            movieSelect = (Movie) savedInstanceState.getSerializable(Utility.EXTRA_DETAIL_MOVIE);
        }


        if (movieSelect == null) {
            // movie data unavailable
            closeOnError();
            return;
        }

        populateUI();
        Picasso.with(this)
                .load(Utility.getFinalUrl("w780",movieSelect.getBackdrop_path()))
                .placeholder(R.drawable.noposterdetail780)
                .error(R.drawable.noposterdetail780)
                .into(backdropDetailMovie);
        Picasso.with(this)
                .load(Utility.getFinalUrl("w92",movieSelect.getPoster_path()))
                .placeholder(R.drawable.noposterdetail780)
                .error(R.drawable.noposterdetail780)
                .into(posterDetailMovie);

        setTitle(movieSelect.getTitle());

        mDb = AppDatabase.getInstance(getApplicationContext());

        // Declare a DetailViewModelFactory using mDb and mMovieId
        DetailViewModelFactory factory = new DetailViewModelFactory(mDb, movieSelect.getId().intValue());
        // Declare a DetailViewModel variable and initialize it by calling ViewModelProviders.of
        // for that use the factory created above DetailViewModel
        final DetailViewModel viewModel
                = ViewModelProviders.of(this, factory).get(DetailViewModel.class);

        //Observe the LiveData object in the ViewModel. Use it also when removing the observer
        viewModel.getMovie().observe(this, new Observer<MovieEntry>() {
            @Override
            public void onChanged(@Nullable MovieEntry movieEntry) {
                viewModel.getMovie().removeObserver(this);
                movieEntryFavorite = movieEntry;

                if( movieEntryFavorite == null){
                    markFavorieBtn.setText(getResources().getText(R.string.mark_it_as_a_favorite));
                }else {
                    markFavorieBtn.setText(getResources().getText(R.string.unmark_it_as_a_favorite));
                }
            }
        });


        mRecyclerViewTrailer = findViewById(R.id.recyclerViewTrailers);
        mRecyclerViewTrailer.setLayoutManager( new LinearLayoutManager(getApplicationContext()));
        mAdapterTrailer = new TrailerAdapter(getApplicationContext());
        mRecyclerViewTrailer.setAdapter(mAdapterTrailer);

       /* OkHttpClient.Builder client = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.addInterceptor(loggingInterceptor);*/

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utility.API_URL)
               // .client(client.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TheMovieDBService service = retrofit.create(TheMovieDBService.class);

        service.getTrailersByIdMovie(movieSelect.getId().intValue(),API_KEY).enqueue(new Callback<Trailers>() {
            @Override
            public void onResponse(Call<Trailers> call, Response<Trailers> response) {

                if (response.body()!= null){

                    mAdapterTrailer.setTrailers(response.body().getResults());
                    mFirstKeyURLTrailerYoutube = response.body().getResults().get(0).getKey();
                    if (mFirstKeyURLTrailerYoutube != null) {
                        mShareActionProvider.setShareIntent(createShareMovieIntent());
                    }

                }else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.data_unavailable), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Trailers> call, Throwable t) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_unavailable), Toast.LENGTH_SHORT).show();
            }
        });

        mRecyclerViewReview = findViewById(R.id.recyclerViewReviews);
        mRecyclerViewReview.setLayoutManager( new LinearLayoutManager(getApplicationContext()));
        mAdapterReview = new ReviewAdapter(getApplicationContext());
        mRecyclerViewReview.setAdapter(mAdapterReview);

        service.getReviewsByIdMovie(movieSelect.getId().intValue(),API_KEY).enqueue(new Callback<Reviews>() {
            @Override
            public void onResponse(Call<Reviews> call, Response<Reviews> response) {

                if (response.body()!= null){
                    mAdapterReview.setReviews(response.body().getResults());
                }else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.data_unavailable), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Reviews> call, Throwable t) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_unavailable), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @OnClick(R.id.mark_favorie_btn)
    public void markAsFavorite(View view) {

        if( movieEntryFavorite == null){
            final MovieEntry movieEntry = Utility.movieToMovieEntry(movieSelect);
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    // insert new favorie movie
                    mDb.movieDao().insertMovie(movieEntry);
                }
            });
            markFavorieBtn.setText(getResources().getText(R.string.unmark_it_as_a_favorite));
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.mark_it_as_a_favorite), Toast.LENGTH_SHORT).show();
        }else {

            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    // delete favorie movie
                    mDb.movieDao().deleteMovie(movieEntryFavorite);
                }
            });

            markFavorieBtn.setText(getResources().getText(R.string.mark_it_as_a_favorite));
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.unmark_it_as_a_favorite), Toast.LENGTH_SHORT).show();

        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // When tablets rotate, the currently selected movie needs to be saved.
        outState.putSerializable(Utility.EXTRA_DETAIL_MOVIE,movieSelect);
        super.onSaveInstanceState(outState);
    }
    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI() {

        titleMovie.setText(movieSelect.getTitle());
        voteAverageMovie.setText(movieSelect.getVote_average()+"/10");
        popularityMovie.setText(movieSelect.getPopularity()+"");
        adulteMovie.setText(movieSelect.getAdult()+"");
        releaseDateMovie.setText(movieSelect.getRelease_date());
        overviewMovie.setText(movieSelect.getOverview());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.menu_detail, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }


    private Intent createShareMovieIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, movieSelect.getTitle()+ " http://www.youtube.com/watch?v="+mFirstKeyURLTrailerYoutube + MOVIE_SHARE_HASHTAG);
        return shareIntent;
    }


}
