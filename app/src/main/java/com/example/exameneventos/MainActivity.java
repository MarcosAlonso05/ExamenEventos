package com.example.exameneventos;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.exameneventos.auth.LoginActivity;
import com.example.exameneventos.database.AppDatabase;
import com.example.exameneventos.helpers.MenuManager;
import com.example.exameneventos.helpers.MovieListManager;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvMovies;
    private MenuManager menuManager;
    private MovieListManager movieListManager;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "movies-database")
                .allowMainThreadQueries()
                .build();

        menuManager = new MenuManager(this);
        movieListManager = new MovieListManager(this, db);

        movieListManager.checkAndPreloadData();

        rvMovies = findViewById(R.id.rvMovies);
        boolean isGridMode = menuManager.loadGridPreference(); // Ask menu manager for the pref
        movieListManager.setupRecyclerView(rvMovies, isGridMode);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (menuManager.handleItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}