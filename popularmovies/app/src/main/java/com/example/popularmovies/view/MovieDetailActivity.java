package com.example.popularmovies.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.popularmovies.R;
import com.example.popularmovies.adapter.MovieAdapter;
import com.example.popularmovies.adapter.ReviewAdapter;
import com.example.popularmovies.adapter.TrailerAdapter;
import com.example.popularmovies.model.Movie;
import com.example.popularmovies.model.MovieDatabase;
import com.example.popularmovies.model.MovieResults;
import com.example.popularmovies.model.ReviewApi;
import com.example.popularmovies.model.ReviewResults;
import com.example.popularmovies.model.Reviews;
import com.example.popularmovies.model.TheMovieDbApi;
import com.example.popularmovies.model.Trailer;
import com.example.popularmovies.model.TrailerApi;
import com.example.popularmovies.model.TrailerResults;
import com.example.popularmovies.viewmodel.MovieViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.popularmovies.utils.CommonUtils.buildImageUrl;
import static com.example.popularmovies.view.MainActivity.EXTRA_MOVIE;

public class MovieDetailActivity extends AppCompatActivity implements TrailerAdapter.onTrailerClickListener {

    private Movie movieDetail;
    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;
    private MovieViewModel movieViewModel;

    // private ImageView BackdropImageView,PosterImageView;
    @BindView(R.id.tv_poster)
    ImageView PosterImageView;
    @BindView(R.id.tv_Runtime)
    TextView Runtime_tv;
    @BindView(R.id.tv_OriginalTitle)
    TextView OriginalName_tv;
    @BindView(R.id.tv_Overview)
    TextView Overview_tv;
    @BindView(R.id.tv_ReleaseDate)
    TextView ReleaseDate_tv;
    @BindView(R.id.tv_voteAverage)
    TextView VoteAverage_tv;
    @BindView(R.id.iv_backdrop)
    ImageView Backdrop_iv;

    @BindView(R.id.progressBar)
    ProgressBar pgBar;

    @BindView(R.id.trailer_recyclerView)
    RecyclerView trailerReclerView;
    @BindView(R.id.review_recyclerView)
    RecyclerView review_recyclerView;
    @BindView(R.id.tv_Review_Header)
    TextView reviewHeader;

    @BindView(R.id.cv_MovieOverview)
    CardView cvTitle;
    @BindView(R.id.tv_TrailerHeader)
    TextView trailerHeader;

    @BindView(R.id.btn_SaveFavourite)
    ImageButton btnFavouriteImage;

    @BindView(R.id.tb_movie)
    Toolbar toolbar;

    Movie movie;
    private String Movie_Id;
    private boolean FavMovies;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie__detail);


        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        cvTitle.setVisibility(View.INVISIBLE);
        pgBar.setVisibility(View.VISIBLE);
        Intent intent = getIntent();


        trailerAdapter = new TrailerAdapter(this);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        trailerReclerView.setLayoutManager(linearLayout);
        trailerReclerView.setHasFixedSize(true);
        trailerReclerView.setAdapter(trailerAdapter);

        LinearLayoutManager Layout = new LinearLayoutManager(this);
        reviewAdapter = new ReviewAdapter();
        review_recyclerView.setLayoutManager(Layout);
        review_recyclerView.setHasFixedSize(true);
        review_recyclerView.setAdapter(reviewAdapter);

        try {
            movie = intent.getParcelableExtra(EXTRA_MOVIE);
            Movie_Id = String.valueOf(movie.getId());
            setViewModel(Movie_Id);
            showTrailer(String.valueOf(Movie_Id));
            showReviews(String.valueOf(Movie_Id));
        } catch (NullPointerException e) {
            e.printStackTrace();
            return;
        }

    }

    private void setViewModel(final String movieID) {
        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.getMovieById(movieID).observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(@Nullable Movie movie) {
                if (movie == null) {
                    TheMovieDbApi.getMovieDetial(movieID).enqueue(new Callback<Movie>() {
                        @Override
                        public void onResponse(Call<Movie> call, Response<Movie> response) {
                            if (response.body() == null || response.isSuccessful() == false) {
                                return;
                            }
                            movieDetail =  response.body();
                            FavMovies = false;
                            populateUI(movieDetail);
                        }

                        @Override
                        public void onFailure(Call<Movie> call, Throwable t) {
                            Toast.makeText(MovieDetailActivity.this, "Unable to display movie detail", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    populateUI(movie);
                    FavMovies = true;
                    btnFavouriteImage.setImageResource(R.drawable.ic_favorite_pink);
                }
                cvTitle.setVisibility(View.VISIBLE);
            }
        });
        pgBar.setVisibility(View.GONE);
    }

    private void showTrailer(String path) {

        TrailerApi.getTrailerList(path).enqueue(new Callback<TrailerResults>() {
            @Override
            public void onResponse(Call<TrailerResults> call, Response<TrailerResults> response) {
                if (response.body() != null) {
                    trailerAdapter.setTrailer(response.body().getTrailers());
                    trailerHeader.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<TrailerResults> call, Throwable t) {
                //cannot load trailer
                trailerHeader.setVisibility(View.GONE);
                trailerReclerView.setVisibility(View.GONE);
            }
        });
    }

    private void showReviews(String path) {

        ReviewApi.getReviewList(path).enqueue(new Callback<ReviewResults>() {
            @Override
            public void onResponse(Call<ReviewResults> call, Response<ReviewResults> response) {
                if (response.body() != null) {
                    reviewAdapter.setTrailer(response.body().getReviews());
                    reviewHeader.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ReviewResults> call, Throwable t) {
                //cannot load trailer
                reviewHeader.setVisibility(View.GONE);
                review_recyclerView.setVisibility(View.GONE);
            }
        });
    }

    private void populateUI(Movie selectedMovie) {
        if(selectedMovie.getRuntime()==null){
            Runtime_tv.setVisibility(View.GONE);
        }
        else
        {
            String mtime = (String) TextUtils.concat(selectedMovie.getRuntime(), "Min");
            Runtime_tv.setText(mtime);
        }

        ReleaseDate_tv.setText(selectedMovie.getReleaseDate());
        Overview_tv.setText(selectedMovie.getOverview());
        if (selectedMovie.getVoteAverage() != 0)
            VoteAverage_tv.setText(TextUtils.concat(String.valueOf(selectedMovie.getVoteAverage()), "/10"));
        else
            VoteAverage_tv.setVisibility(View.GONE);
        OriginalName_tv.setText(selectedMovie.getOriginalTitle());

        int backDropSize = 500;
        int posterSize = 185;
        Picasso.get()
                .load(buildImageUrl(selectedMovie.getBackdropPath(), backDropSize))
                .into(Backdrop_iv);
        Picasso.get()
                .load(buildImageUrl(selectedMovie.getPosterPath(), posterSize))
                .into(PosterImageView);
    }

    @Override
    public void onTrailerClick(Trailer trailer) {
        final String YOUTUBE_URL = "http://www.youtube.com/watch?v=";
        final String YOUTUBE_APP = "vnd.youtube:";

        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_URL.concat(trailer.getKey()))));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_APP.concat(trailer.getKey()))));
        }
    }

    public void saveFavourite(View view) {
        if(FavMovies==false)
        {
            Intent intent = getIntent();
            movie = intent.getParcelableExtra(EXTRA_MOVIE);
            movieViewModel.insetMovie(movieDetail);
            btnFavouriteImage.setImageResource(R.drawable.ic_favorite_pink);
            FavMovies=true;
            Toast.makeText(getApplicationContext(),"Movie added to favourite list",Toast.LENGTH_LONG).show();
        }
        else
        {
            movieViewModel.deleteMovie(movie);
            btnFavouriteImage.setImageResource(R.drawable.ic_favorite);
            FavMovies=false;
        }
    }
}