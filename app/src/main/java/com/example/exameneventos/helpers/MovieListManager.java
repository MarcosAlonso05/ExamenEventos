package com.example.exameneventos.helpers;

import android.content.Context;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exameneventos.adapter.MovieAdapter;
import com.example.exameneventos.database.AppDatabase;
import com.example.exameneventos.model.Movie;

import java.util.List;

public class MovieListManager {

    private Context context;
    private AppDatabase db;

    public MovieListManager(Context context, AppDatabase db) {
        this.context = context;
        this.db = db;
    }

    public void setupRecyclerView(RecyclerView recyclerView, boolean isGridMode) {
        List<Movie> movies = db.movieDao().getAllMovies();

        MovieAdapter adapter = new MovieAdapter(context, movies, isGridMode);
        recyclerView.setAdapter(adapter);

        if (isGridMode) {
            recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        }
    }

    public void checkAndPreloadData() {
        if (db.movieDao().getCount() == 0) {
            Movie m1 = new Movie("El Caballero Oscuro", "Christopher Nolan", "https://almacen-rmr.tionazo.com/pelis/caballero-oscuro.jpg");
            Movie m2 = new Movie("Cadena Perpetua", "Frank Darabont", "https://almacen-rmr.tionazo.com/pelis/cadena-perpetua.jpg");
            Movie m3 = new Movie("El Padrino", "Francis Ford Coppola", "https://almacen-rmr.tionazo.com/pelis/padrino.jpg");
            Movie m4 = new Movie("Pulp Fiction", "Quentin Tarantino", "https://almacen-rmr.tionazo.com/pelis/pulp_fiction.jpg");

            db.movieDao().insertAll(m1, m2, m3, m4);
            Toast.makeText(context, "Initial data loaded", Toast.LENGTH_SHORT).show();
        }
    }
}