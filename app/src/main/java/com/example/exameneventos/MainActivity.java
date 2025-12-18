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

import com.example.exameneventos.adapter.MovieAdapter;
import com.example.exameneventos.database.AppDatabase;
import com.example.exameneventos.model.Movie;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Variables
    private RecyclerView rvMovies;
    private AppDatabase db;
    private FirebaseAuth mAuth;
    private boolean isGridMode = false; // Variable para controlar el modo de vista

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Inicializar Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // --- COMENTADO HASTA QUE CREEMOS EL LOGIN ---
        // if (mAuth.getCurrentUser() == null) {
        //     startActivity(new Intent(this, LoginActivity.class));
        //     finish();
        //     return;
        // }

        // 2. Vincular vistas
        rvMovies = findViewById(R.id.rvMovies);

        // 3. Cargar preferencias (SharedPreferences)
        // Esto recupera si el usuario prefirió Grid o Linear la última vez
        loadPreferences();

        // 4. Inicializar Base de Datos (ROOM)
        // "allowMainThreadQueries" se usa solo en exámenes para simplificar
        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "movies-database")
                .allowMainThreadQueries()
                .build();

        // 5. Comprobar si está vacía y rellenar (Requisito: Rellenar al inicio)
        if (db.movieDao().getCount() == 0) {
            preloadData();
        }

        // 6. Configurar el RecyclerView con los datos
        setupRecyclerView();
    }

    // ----------------------------------------------------------------
    // LÓGICA DEL MENÚ (Requisito: Settings y Logout)
    // ----------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            // Logout de Firebase
            mAuth.signOut();
            // Ir al Login (Lo activaremos al final)
            // startActivity(new Intent(this, LoginActivity.class));
            // finish();
            Toast.makeText(this, "Logout clicked (Implementar LoginActivity)", Toast.LENGTH_SHORT).show();
            return true;

        } else if (id == R.id.action_settings) {
            // Mostrar diálogo para cambiar vista
            showViewModeDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // ----------------------------------------------------------------
    // LÓGICA DE SETTINGS Y SHAREDPREFERENCES
    // ----------------------------------------------------------------
    private void showViewModeDialog() {
        String[] options = {"Lista (Texto + Imagen)", "Cuadrícula (Solo Imagen)"};

        // Determinar cuál está seleccionado actualmente
        int checkedItem = isGridMode ? 1 : 0;

        new AlertDialog.Builder(this)
                .setTitle("Configuración de Visualización")
                .setSingleChoiceItems(options, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // which es 0 (Lista) o 1 (Cuadrícula)
                        boolean newMode = (which == 1);

                        // Guardar en SharedPreferences
                        savePreference(newMode);

                        // Cerrar diálogo y recargar la actividad para ver cambios
                        dialog.dismiss();
                        recreate(); // Truco: Recarga la pantalla entera para aplicar cambios
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
        // Por defecto false (Lista)
        isGridMode = prefs.getBoolean("grid_mode", false);
    }

    // ----------------------------------------------------------------
    // LÓGICA DE CARGA DE DATOS Y RECYCLER
    // ----------------------------------------------------------------
    private void setupRecyclerView() {
        // Obtener lista de Room
        List<Movie> movies = db.movieDao().getAllMovies();

        // Configurar Adapter pasando el modo (Grid o no)
        MovieAdapter adapter = new MovieAdapter(this, movies, isGridMode);
        rvMovies.setAdapter(adapter);

        // Cambiar el LayoutManager según el modo
        if (isGridMode) {
            // GridLayout con 2 columnas
            rvMovies.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            // LinearLayout normal
            rvMovies.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    private void preloadData() {
        // Requisito: Datos hardcoded de 3 películas
        Movie m1 = new Movie("El Caballero Oscuro", "Christopher Nolan", "https://almacen-rmr.tionazo.com/pelis/caballero-oscuro.jpg");
        Movie m2 = new Movie("Cadena Perpetua", "Frank Darabont", "https://almacen-rmr.tionazo.com/pelis/cadena-perpetua.jpg");
        Movie m3 = new Movie("El Padrino", "Francis Ford Coppola", "https://almacen-rmr.tionazo.com/pelis/padrino.jpg");
        Movie m4 = new Movie("Pulp Fiction", "Quentin Tarantino", "https://almacen-rmr.tionazo.com/pelis/pulp_fiction.jpg");

        // Insertar en Room
        db.movieDao().insertAll(m1, m2, m3, m4);

        Toast.makeText(this, "Datos iniciales cargados", Toast.LENGTH_SHORT).show();
    }
}