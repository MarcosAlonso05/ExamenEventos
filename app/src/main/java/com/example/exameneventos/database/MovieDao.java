package com.example.exameneventos.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.exameneventos.model.Movie;

import java.util.List;

@Dao
public interface MovieDao {

    @Insert
    void insertAll(Movie... movies);

    @Query("SELECT * FROM movies_table")
    List<Movie> getAllMovies();

    @Query("SELECT COUNT(*) FROM movies_table")
    int getCount();
}