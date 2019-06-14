package com.example.popularmovies.model;

import com.example.popularmovies.BuildConfig;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class TheMovieDbApi {

    public interface MovieApi {
        @GET("/3/movie/{path}")
        Call<MovieResults> getMovieFromTheMovieDb(@Path("path") String path, @Query("api_key") String apiKey);
    }

    public interface DetailMovieApi{
        @GET("/3/movie/{path}")
        Call<Movie>getMovieDetailFromTheMovieDb(@Path("path") String path, @Query("api_key") String apiKey);
    }

    private static MovieApi movieApi;
    public static DetailMovieApi detailMovieApi;

    private static MovieApi getMovieService() {
        if (movieApi == null) {
            Retrofit retrofit= new retrofit2.Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            movieApi = retrofit.create(MovieApi.class);
        }
        return movieApi;
    }


    public static Call<MovieResults> getMovieList(String path) {
        return getMovieService().getMovieFromTheMovieDb(path, BuildConfig.MOVIE_API);
    }

    private static DetailMovieApi getMovieDetailService() {
        if (detailMovieApi == null) {
            Retrofit retrofit= new retrofit2.Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            detailMovieApi = retrofit.create(DetailMovieApi.class);
        }
        return detailMovieApi;
    }
    public static Call<Movie> getMovieDetial(String path){
        return getMovieDetailService().getMovieDetailFromTheMovieDb(path,BuildConfig.MOVIE_API);
    }
}


