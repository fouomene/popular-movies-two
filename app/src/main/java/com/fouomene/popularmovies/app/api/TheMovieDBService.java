package com.fouomene.popularmovies.app.api;

import com.fouomene.popularmovies.app.model.Movies;
import com.fouomene.popularmovies.app.model.Reviews;
import com.fouomene.popularmovies.app.model.Trailers;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TheMovieDBService {

    @GET("movie/popular")
    Call<Movies> getMoviePopular(@Query("api_key") String api_key);

    @GET("movie/top_rated")
    Call<Movies> getMovieTopRated(@Query("api_key") String api_key);

    @GET("movie/{movie_id}/videos")
    Call<Trailers> getTrailersByIdMovie(@Path("movie_id") int movie_id, @Query("api_key") String api_key);

    @GET("movie/{movie_id}/reviews")
    Call<Reviews> getReviewsByIdMovie(@Path("movie_id") int movie_id, @Query("api_key") String api_key);

}
