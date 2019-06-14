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
import com.example.popularmovies.model.Trailer;
import com.example.popularmovies.utils.CommonUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerHolder> {
    private List<Trailer> trailerList = new ArrayList<>();

    public interface onTrailerClickListener {
        void onTrailerClick(Trailer trailer);
    }

    private onTrailerClickListener listener;

    public TrailerAdapter(onTrailerClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public TrailerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_list_item,parent,false);
        return new TrailerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerHolder holder, int position) {
        holder.bindTrailer(position);
    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }
    public void setTrailer(List<Trailer> movies)
    {
        this.trailerList.clear();
        if(movies !=null) {
            trailerList.addAll(movies);
        }
        notifyDataSetChanged();
    }

    class TrailerHolder extends RecyclerView.ViewHolder{
        ImageView trailer_path;
        TextView trailerName;
        public TrailerHolder(@NonNull View itemView) {
            super(itemView);
            trailer_path = itemView.findViewById(R.id.iv_trailerImage);
            trailerName = itemView.findViewById(R.id.tv_TrailerName);
        }
        void bindTrailer(final int position) {

            //reference from
            //https://stackoverflow.com/questions/2068344/how-do-i-get-a-youtube-video-thumbnail-from-the-youtube-api
            final String YOUTUBE = "https://img.youtube.com/vi/";
            final String IMG_QUALITY = "/0.jpg";
            final String youTubeDefaultThumbnail =YOUTUBE.concat(trailerList.get(position).getKey()).concat(IMG_QUALITY);

            Picasso.get()
                    .load(Uri.parse(youTubeDefaultThumbnail))
                    .into(trailer_path);
            trailerName.setText(trailerList.get(position).getName());
            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    listener.onTrailerClick(trailerList.get(getAdapterPosition()));
                }
            });
        }

    }
}
