package com.example.popularmovies.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Movie.class,version = 2, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {
    private static MovieDatabase instance;
    private static final String DB_NAME = "movie_database";

    public abstract MovieDao movieDao();

    public static synchronized MovieDatabase getInstance(Context context){
        if(instance ==null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    MovieDatabase.class,DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
