package com.example.popularmovies.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmovies.R;
import com.example.popularmovies.model.Reviews;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {
    private List<Reviews> reviewsList = new ArrayList<>();

    public ReviewAdapter() {
    }

    @NonNull
    @Override
    public ReviewAdapter.ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_list_item,parent,false);
        return new ReviewAdapter.ReviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ReviewHolder holder, int position) {
        holder.bindTrailer(position);
    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }
    public void setTrailer(List<Reviews> movies)
    {
        this.reviewsList.clear();
        if(movies !=null) {
            reviewsList.addAll(movies);
        }
        notifyDataSetChanged();
    }

    class ReviewHolder extends RecyclerView.ViewHolder{
        TextView reviewContent,reviewAuthor;

        public ReviewHolder(@NonNull View itemView) {
            super(itemView);
            reviewContent = itemView.findViewById(R.id.tv_ReviewContent);
            reviewAuthor = itemView.findViewById(R.id.tv_ReviewAuthor);
        }
        void bindTrailer(final int position) {
            reviewAuthor.setText(reviewsList.get(position).getAuthor());
            reviewContent.setText(reviewsList.get(position).getContent());
        }

    }
}
