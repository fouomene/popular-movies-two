package com.fouomene.popularmovies.app;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.widget.Toast;


import com.fouomene.popularmovies.app.adapter.MovieAdapter;
import com.fouomene.popularmovies.app.api.TheMovieDBService;
import com.fouomene.popularmovies.app.model.Movie;
import com.fouomene.popularmovies.app.model.Movies;
import com.fouomene.popularmovies.app.utils.Utility;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivityFragment extends Fragment implements MovieAdapter.ItemClickListener {

    private List<Movie> moviesList = Collections.emptyList();

    private final  int numberOfColumnsGrid = 2;
    private RecyclerView mRecyclerView;
    private MovieAdapter mAdapter;

    private static final String API_KEY = BuildConfig.API_KEY;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Set the RecyclerView to its corresponding view
        mRecyclerView = rootView.findViewById(R.id.recyclerViewMovies);

        // Set the layout for the RecyclerView to be a Grid Layout, which measures and
        // positions items within a RecyclerView into a Grid list
        mRecyclerView.setLayoutManager( new GridLayoutManager(getActivity().getApplicationContext(), numberOfColumnsGrid));

        // Initialize the adapter and attach it to the RecyclerView
        mAdapter = new MovieAdapter(getActivity().getApplicationContext(), this);
        mRecyclerView.setAdapter(mAdapter);


        //for log
       /* OkHttpClient.Builder client = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.addInterceptor(loggingInterceptor);*/

        if (isOnline()){

            if ("mostpopular".equals(Utility.getPreferredSortOrder(getActivity().getApplicationContext()))) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Utility.API_URL)
                        //.client(client.build())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                TheMovieDBService service = retrofit.create(TheMovieDBService.class);


                service.getMoviePopular(API_KEY).enqueue(new Callback<Movies>() {
                   @Override
                   public void onResponse(Call<Movies> call, Response<Movies> response) {

                       if (response.body()!= null){
                           moviesList = response.body().getResults();
                           mAdapter.setMovies(moviesList);
                           ((GridLayoutManager) mRecyclerView.getLayoutManager()).scrollToPosition(Integer.parseInt(Utility.getPreferredCurrentVisiblePosition(getActivity().getApplicationContext())));
                       }else {
                           Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.data_unavailable), Toast.LENGTH_SHORT).show();
                       }
                   }

                   @Override
                   public void onFailure(Call<Movies> call, Throwable t) {
                       Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.server_unavailable), Toast.LENGTH_SHORT).show();
                   }
               });

            }else{


                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Utility.API_URL)
                        //.client(client.build())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                TheMovieDBService service = retrofit.create(TheMovieDBService.class);


                service.getMovieTopRated(API_KEY).enqueue(new Callback<Movies>() {
                    @Override
                    public void onResponse(Call<Movies> call, Response<Movies> response) {

                        if (response.body()!= null){
                            moviesList = response.body().getResults();
                            mAdapter.setMovies(moviesList);
                            ((GridLayoutManager) mRecyclerView.getLayoutManager()).scrollToPosition(Integer.parseInt(Utility.getPreferredCurrentVisiblePosition(getActivity().getApplicationContext())));
                        }else {
                            Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.data_unavailable), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Movies> call, Throwable t) {

                        Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.server_unavailable), Toast.LENGTH_SHORT).show();

                    }
                });
            }

        }else {

            Snackbar snackbar = Snackbar
                    .make(rootView, getResources().getString(R.string.check_network), Snackbar.LENGTH_LONG);
            snackbar.show();


        }

        return rootView;
    }

    public boolean isOnline() {
        boolean status=false;
        try{
            ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState()==NetworkInfo.State.CONNECTED) {
                status= true;
            }else {
                netInfo = cm.getNetworkInfo(1);
                if(netInfo!=null && netInfo.getState()==NetworkInfo.State.CONNECTED)
                    status= true;
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return status;
    }

    @Override
    public void onItemClickListener(Movie itemMovie) {

        Utility.setPreferredCurrentVisiblePosition(getActivity().getApplicationContext(),((GridLayoutManager) mRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition()+"");
        // When the user clicks on the movie
        Intent intent = new Intent(getActivity().getApplicationContext(), DetailActivity.class);
        intent.putExtra(Utility.EXTRA_DETAIL_MOVIE, itemMovie);
        startActivity(intent);
    }

}