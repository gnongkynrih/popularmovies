package com.example.popularmovies.model;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class MovieRepository {
    private MovieDao movieDao;
    private static LiveData<Movie>movie;
    private LiveData<List<Movie>> allMovies;
    private static final String INSERT = "insert";
    private static final String DELETE = "delete";
    private static final String DELETE_ALL = "delete_all";

    public MovieRepository(Application application) {
        MovieDatabase database = MovieDatabase.getInstance(application);
        movieDao = database.movieDao();
        allMovies = movieDao.getAllMovies();
    }

    public void insertMovie(Movie movie) {
        new CRUDAsyncTask(movieDao,INSERT).execute(movie);
    }


    public void deleteMovie(Movie movie) {
        new CRUDAsyncTask(movieDao,DELETE).execute(movie);
    }

    public void deleteAllMovies() {
        new CRUDAsyncTask(movieDao,DELETE_ALL).execute();
    }

    public LiveData<List<Movie>> getAllMovies() {
        return allMovies;
    }
    public LiveData<Movie> findMovieById(String id){
        return movieDao.findMovieById(id);
    }

    private static class CRUDAsyncTask extends AsyncTask<Movie, Void, Void> {

        private MovieDao movieDao;
        private String operation;

        private CRUDAsyncTask(MovieDao movieDao, String operation) {
            this.movieDao = movieDao;
            this.operation = operation;
        }

        @Override
        protected Void doInBackground(Movie... movies) {
            switch (operation) {
                case INSERT:
                    movieDao.insertMovie(movies[0]);
                    break;
                case DELETE:
                    movieDao.deleteMovie(movies[0]);
                    break;
                default:
                    movieDao.deleteAllMovies();
            }
            return null;
        }
    }
}
