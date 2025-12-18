package com.example.exameneventos;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ivCover = findViewById(R.id.ivDetailCover);
        TextView tvTitle = findViewById(R.id.tvDetailTitle);
        TextView tvDirector = findViewById(R.id.tvDetailDirector);

        String title = getIntent().getStringExtra("MOVIE_TITLE");
        String director = getIntent().getStringExtra("MOVIE_DIRECTOR");
        String coverUrl = getIntent().getStringExtra("MOVIE_COVER");

        tvTitle.setText(title);
        tvDirector.setText(director);

        Glide.with(this)
                .load(coverUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(ivCover);
    }
}