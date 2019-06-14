package com.example.popularmovies.utils;

import android.net.Uri;

import com.example.popularmovies.BuildConfig;

import java.io.IOException;
import java.net.URL;

public class CommonUtils {

    public static String buildImageUrl(String imageDir,  int posterSize) {
        String pSize = "w" + Integer.toString(posterSize);
        return Uri.parse(BuildConfig.BASE_IMAGE_URL)
                .buildUpon()
                .appendPath(pSize)
                .appendEncodedPath(imageDir)
                .toString();

    }

    public static String buildTrailerUrl(String key){

        final String youTube = "https://img.youtube.com/vi/";
        final String youTubeDefaultThumbnail =key.concat("/0.jpg");
        return Uri.parse(youTube)
                .buildUpon()
                .appendEncodedPath(youTubeDefaultThumbnail)
                .toString();
    }
}