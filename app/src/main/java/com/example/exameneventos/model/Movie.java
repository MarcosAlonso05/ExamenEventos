package com.example.exameneventos.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "movies_table")
public class Movie implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "director")
    private String director;

    @ColumnInfo(name = "cover_url")
    private String coverUrl;

    public Movie() {
    }

    @androidx.room.Ignore
    public Movie(String title, String director, String coverUrl) {
        this.title = title;
        this.director = director;
        this.coverUrl = coverUrl;
    }



    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }

    public String getCoverUrl() { return coverUrl; }
    public void setCoverUrl(String coverUrl) { this.coverUrl = coverUrl; }
}