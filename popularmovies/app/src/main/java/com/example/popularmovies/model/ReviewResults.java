package com.example.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewResults {
    @SerializedName("results")
    private List<Reviews> reviews;
    public List<Reviews> getReviews() {
        return reviews;
    }
}
