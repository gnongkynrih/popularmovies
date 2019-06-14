package com.example.popularmovies.model;

import com.example.popularmovies.BuildConfig;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class TrailerApi {

    public interface MovieApi {
        @GET("/3/movie/{movie_id}/videos")
        Call<TrailerResults> getTrailerFromTheMovieDb(@Path("movie_id") String path, @Query("api_key") String apiKey);
    }

    private static MovieApi movieApi;
    private static MovieApi getTrailerService() {
        if (movieApi == null) {
            Retrofit retrofit= new retrofit2.Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            movieApi = retrofit.create(MovieApi.class);
        }
        return movieApi;
    }


    public static Call<TrailerResults> getTrailerList(String path) {
        return getTrailerService().getTrailerFromTheMovieDb(path, BuildConfig.MOVIE_API);
    }

}
