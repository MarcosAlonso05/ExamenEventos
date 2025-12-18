package com.example.exameneventos;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

// IMPORTANT: Import LoginActivity from the new 'auth' package
import com.example.exameneventos.auth.LoginActivity;
import com.example.exameneventos.adapter.MovieAdapter;
import com.example.exameneventos.database.AppDatabase;
import com.example.exameneventos.model.Movie;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvMovies;
    private AppDatabase db;
    private FirebaseAuth mAuth;
    private boolean isGridMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        rvMovies = findViewById(R.id.rvMovies);

        loadPreferences();

        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "movies-database")
                .allowMainThreadQueries()
                .build();

        if (db.movieDao().getCount() == 0) {
            preloadData();
        }

        setupRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            mAuth.signOut();

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;

        } else if (id == R.id.action_settings) {
            showViewModeDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showViewModeDialog() {
        String[] options = {"List View (Image + Text)", "Grid View (Image Only)"};

        int checkedItem = isGridMode ? 1 : 0;

        new AlertDialog.Builder(this)
                .setTitle("Select View Mode")
                .setSingleChoiceItems(options, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean newMode = (which == 1);

                        savePreference(newMode);

                        dialog.dismiss();
                        recreate();
                    }
                })
                .show();
    }

    private void savePreference(boolean isGrid) {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("grid_mode", isGrid);
        editor.apply();
    }

    private void loadPreferences() {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        isGridMode = prefs.getBoolean("grid_mode", false);
    }

    private void setupRecyclerView() {
        List<Movie> movies = db.movieDao().getAllMovies();

        MovieAdapter adapter = new MovieAdapter(this, movies, isGridMode);
        rvMovies.setAdapter(adapter);

        if (isGridMode) {
            rvMovies.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            rvMovies.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    private void preloadData() {
        Movie m1 = new Movie("El Caballero Oscuro", "Christopher Nolan", "https://almacen-rmr.tionazo.com/pelis/caballero-oscuro.jpg");
        Movie m2 = new Movie("Cadena Perpetua", "Frank Darabont", "https://almacen-rmr.tionazo.com/pelis/cadena-perpetua.jpg");
        Movie m3 = new Movie("El Padrino", "Francis Ford Coppola", "https://almacen-rmr.tionazo.com/pelis/padrino.jpg");
        Movie m4 = new Movie("Pulp Fiction", "Quentin Tarantino", "https://almacen-rmr.tionazo.com/pelis/pulp_fiction.jpg");

        db.movieDao().insertAll(m1, m2, m3, m4);

        Toast.makeText(this, "Initial data loaded", Toast.LENGTH_SHORT).show();
    }
}