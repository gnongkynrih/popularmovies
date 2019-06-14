package com.example.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "moviedb_table")
public class Movie implements Parcelable {
    @PrimaryKey
    private int id;

    @Ignore
    protected Movie(Parcel in) {
        id = in.readInt();
        posterPath = in.readString();
        title = in.readString();
        backdropPath = in.readString();
        originalTitle = in.readString();
        releaseDate = in.readString();
        overview = in.readString();
        voteAverage = in.readDouble();
        runtime = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    @Ignore
    public Movie(String posterPath,int movie_id)
    {
        this.posterPath = posterPath;
        this.id = movie_id;
    }
    public Movie(int id, String posterPath, String title, String backdropPath, String originalTitle, String releaseDate, String overview, double voteAverage, String runtime) {
        this.id = id;
        this.posterPath = posterPath;
        this.title = title;
        this.backdropPath = backdropPath;
        this.originalTitle = originalTitle;
        this.releaseDate = releaseDate;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.runtime = runtime;
    }


    @SerializedName("poster_path")
    private String posterPath;

    @ColumnInfo(name="original_title")
    private String title;

    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("original_title")
    private String originalTitle;

    @SerializedName("release_date")
    private String releaseDate;

    @ColumnInfo(name="overview")
    private String overview;
    @SerializedName("vote_average")
    private double voteAverage;

    @ColumnInfo(name="runtime")
    private String runtime;


    public void setMovie_id(int movie_id) {
        this.id = movie_id;
    }

    public String getTitle() {
        return title;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getOverview() {
        return overview;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getRuntime() {
        return runtime;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public int getId(){return id;}

    @Ignore
    @Override
    public int describeContents() {
        return 0;
    }

    @Ignore
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(posterPath);
        dest.writeString(title);
        dest.writeString(backdropPath);
        dest.writeString(originalTitle);
        dest.writeString(releaseDate);
        dest.writeString(overview);
        dest.writeDouble(voteAverage);
        dest.writeString(runtime);
    }
}
