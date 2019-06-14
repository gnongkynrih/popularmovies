package com.example.popularmovies.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmovies.R;
import com.example.popularmovies.model.Movie;
import com.example.popularmovies.utils.CommonUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder> {

    public interface onMovieClickListener {
        void onItemClick(Movie movie);
    }

    private List<Movie> movieList = new ArrayList<>();

    private onMovieClickListener listener;
    private int posterSize;

    public MovieAdapter(int posterSize,onMovieClickListener listener,List<Movie> movieList) {
        this.posterSize = posterSize;
        this.listener = listener;
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_item,parent,false);
        return new MovieHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieHolder holder, int position) {
        holder.bindMovie(movieList,position);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public void setMovie(List<Movie> movies)
    {
        this.movieList.clear();
        if(movies !=null) {
            movieList.addAll(movies);
        }
        notifyDataSetChanged();
    }

    class MovieHolder extends RecyclerView.ViewHolder{
        final ImageView poster_path;
        public MovieHolder(@NonNull View itemView) {

            super(itemView);
            poster_path = itemView.findViewById(R.id.poster_view);
        }

        void bindMovie(final List<Movie> movie, final int position) {
            Picasso.get()
                    .load(CommonUtils.buildImageUrl(movie.get(position).getPosterPath(), posterSize))
                    .into(poster_path);
            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    listener.onItemClick(movieList.get(getAdapterPosition()));
                }
            });
        }

    }
}
