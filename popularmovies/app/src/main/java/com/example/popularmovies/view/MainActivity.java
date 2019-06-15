package com.example.popularmovies.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.popularmovies.BuildConfig;
import com.example.popularmovies.R;
import com.example.popularmovies.adapter.MovieAdapter;
import com.example.popularmovies.model.Movie;
import com.example.popularmovies.model.MovieDatabase;
import com.example.popularmovies.model.MovieResults;
import com.example.popularmovies.model.TheMovieDbApi;
import com.example.popularmovies.viewmodel.MovieViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements MovieAdapter.onMovieClickListener {
    private MovieViewModel movieViewModel;
    private MovieAdapter movieAdapter;
    private int posterSize = 500;
    public static final String EXTRA_MOVIE = "MOVIE_DETAIL";
    private final String SORT_POPULAR = "popular";
    private final String SORT_RATING = "top_rated";
    private final String SORT_KEY="sort_key";
    private String orderBy = SORT_POPULAR;
    private final String SCREEN_POSITION="position";
    private Parcelable savedRecyclerLayoutState;
    private static String MOVIE_LIST_STATE = "movie_state";

    @BindView(R.id.toolbar)
    Toolbar mainToolBar;
    @BindView(R.id.movie_recyclerview)
    RecyclerView movieRecyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    GridLayoutManager layout;
    private ArrayList<Movie> movieInstance = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(mainToolBar);
        progressBar.setVisibility(View.VISIBLE);
        int spanCount = 2;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            spanCount = 3;
        layout = new GridLayoutManager(MainActivity.this, spanCount);
        movieRecyclerView.setLayoutManager(layout);
        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        if(savedInstanceState !=null){
            orderBy = savedInstanceState.getString(SORT_KEY);
            movieInstance = savedInstanceState.getParcelableArrayList(MOVIE_LIST_STATE);
            savedRecyclerLayoutState = savedInstanceState.getParcelable(SCREEN_POSITION);

            showMovies();
        }
        else{
            movieAdapter = new MovieAdapter(posterSize, this,movieInstance);
            initialSetup(orderBy);
        }
    }

    private void showMovies() {
        movieRecyclerView.setItemAnimator(new DefaultItemAnimator());
        movieRecyclerView.setHasFixedSize(true);
        movieAdapter = new MovieAdapter(posterSize,this,movieInstance);
        movieRecyclerView.setAdapter(movieAdapter);
        if (savedRecyclerLayoutState != null) {
            movieRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
        movieAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void initialSetup(String path)
    {

        TheMovieDbApi.getMovieList(path).enqueue(new Callback<MovieResults>() {
            @Override
            public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                if (response.body() != null) {
                    List<Movie> movies = response.body().getMovies();
                    movieInstance.clear();
                    movieInstance.addAll(movies);
                    movieRecyclerView.setAdapter(new MovieAdapter(posterSize,MainActivity.this,movieInstance));
                }
            }

            @Override
            public void onFailure(Call<MovieResults> call, Throwable t) {
                Snackbar.make(findViewById(R.id.movie_recyclerview), "Cannot load movie", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                initialSetup(orderBy);
                            }
                        }).show();
            }
        });
        progressBar.setVisibility(View.INVISIBLE);
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SORT_KEY,orderBy);
        outState.putParcelableArrayList(MOVIE_LIST_STATE,movieInstance);
        outState.putParcelable(SCREEN_POSITION, movieRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        orderBy = savedInstanceState.getString(SORT_KEY);
        movieInstance = savedInstanceState.getParcelableArrayList(MOVIE_LIST_STATE);
        savedRecyclerLayoutState = savedInstanceState.getParcelable(SCREEN_POSITION);
        if (savedRecyclerLayoutState != null) {
            movieRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sort, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnu_popular:

                    orderBy = SORT_POPULAR;
                    initialSetup(orderBy);
                break;
            case R.id.mnu_rating:
                    orderBy = SORT_RATING;
                    initialSetup(orderBy);
                break;
            case R.id.mnu_show_favourites:
                loadFavourites();
                break;
            default:
                deleteFavourites();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void deleteFavourites(){
        movieViewModel.deleteAllMovie();
        Toast.makeText(this,"Favourites movie lists cleared", Toast.LENGTH_SHORT).show();
        initialSetup(orderBy);
        Toast.makeText(this,"Popular Movies Loaded", Toast.LENGTH_SHORT).show();
    }
    private void loadFavourites(){
        movieViewModel.getAllMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                if(movies !=null){
                    movieInstance.clear();
                    movieInstance.addAll(movies);
                    showMovies();
                    //movieAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"Favourite movie list loaded",Toast.LENGTH_LONG);
                }
                else
                {
                    Toast.makeText(MainActivity.this,"No favourite movies",Toast.LENGTH_LONG).show();
                    movieRecyclerView.setVisibility(View.GONE);
                }

            }
        });
    }


    @Override
    public void onItemClick(Movie movie) {
        progressBar.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(EXTRA_MOVIE, movie);
        startActivity(intent);
        progressBar.setVisibility(View.GONE);
    }
}
