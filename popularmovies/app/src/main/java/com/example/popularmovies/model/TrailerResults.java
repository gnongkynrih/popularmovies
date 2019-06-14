package com.example.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrailerResults {
    @SerializedName("results")
    private List<Trailer> trailers;
    public List<Trailer> getTrailers() {
        return trailers;
    }
}
