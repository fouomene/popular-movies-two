package com.fouomene.popularmovies.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.fouomene.popularmovies.app.R;
import com.fouomene.popularmovies.app.database.MovieEntry;
import com.fouomene.popularmovies.app.model.Movie;

public class Utility {

    public static final String API_URL = "https://api.themoviedb.org/3/";
    public static final String EXTRA_DETAIL_MOVIE = "com.fouomene.popularmovies.app.model.movie";

    public static String getFinalUrl(String size, String posterPath){
        return  "http://image.tmdb.org/t/p/"+size+"/"+posterPath;
    }

    public static void setPreferredSortOrder(Context context, String sortOrder) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(context.getString(R.string.pref_sort_order_key), sortOrder);
        editor.commit();
    }

    public static String getPreferredSortOrder(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_sort_order_key),
                context.getString(R.string.pref_sort_order_default));
    }

    public static void setPreferredCurrentVisiblePosition(Context context, String position) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(context.getString(R.string.pref_current_position_key), position);
        editor.commit();
    }

    public static String getPreferredCurrentVisiblePosition(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_current_position_key),
                context.getString(R.string.pref_current_position_default));
    }

    public static MovieEntry movieToMovieEntry(Movie movie){
        MovieEntry entry = new MovieEntry();
        entry.setVote_count(movie.getVote_count().intValue());
        entry.setId_movie(movie.getId().intValue());
        entry.setVideo((movie.getVideo())?1:0);
        entry.setVote_average(movie.getVote_average().toString());
        entry.setTitle(movie.getTitle());
        entry.setPopularity(movie.getPopularity().toString());
        entry.setPoster_path(movie.getPoster_path());
        entry.setOriginal_language(movie.getOriginal_language());
        entry.setOriginal_title(movie.getOriginal_title());
        entry.setGenre_ids(movie.getGenre_ids());
        entry.setBackdrop_path(movie.getBackdrop_path());
        entry.setAdult((movie.getAdult())?1:0);
        entry.setOverview(movie.getOverview());
        entry.setRelease_date(movie.getRelease_date());
        return entry;
    }
}
