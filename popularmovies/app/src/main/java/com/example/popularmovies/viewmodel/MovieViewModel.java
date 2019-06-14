package com.example.popularmovies.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.popularmovies.model.Movie;
import com.example.popularmovies.model.MovieRepository;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {
    private MovieRepository movieRepository;
    private LiveData<List<Movie>> allMovies;

    public MovieViewModel(@NonNull Application application ) {
        super(application);
        movieRepository = new MovieRepository(application);
        allMovies = movieRepository.getAllMovies();
    }

    public void insetMovie(Movie movie){
        movieRepository.insertMovie(movie);
    }

    public void deleteMovie(Movie movie){
        movieRepository.deleteMovie(movie);
    }

    public void deleteAllMovie(){
        movieRepository.deleteAllMovies();
    }

    public LiveData<List<Movie>> getAllMovies(){
       return allMovies;
    }

    public LiveData<Movie> getMovieById(String id){
        return movieRepository.findMovieById(id);
    }
}
