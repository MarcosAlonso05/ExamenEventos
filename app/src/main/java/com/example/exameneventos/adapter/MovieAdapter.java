package com.example.exameneventos.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.exameneventos.DetailActivity;
import com.example.exameneventos.R;
import com.example.exameneventos.model.Movie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private Context context;
    private List<Movie> movieList;
    private boolean isGridMode;

    public MovieAdapter(Context context, List<Movie> movieList, boolean isGridMode) {
        this.context = context;
        this.movieList = movieList;
        this.isGridMode = isGridMode;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);

        holder.tvTitle.setText(movie.getTitle());
        holder.tvDirector.setText(movie.getDirector());

        Glide.with(context)
                .load(movie.getCoverUrl())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(holder.ivCover);

        if (isGridMode) {
            holder.layoutTextInfo.setVisibility(View.GONE);
        } else {
            holder.layoutTextInfo.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("MOVIE_TITLE", movie.getTitle());
            intent.putExtra("MOVIE_DIRECTOR", movie.getDirector());
            intent.putExtra("MOVIE_COVER", movie.getCoverUrl());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDirector;
        ImageView ivCover;
        LinearLayout layoutTextInfo;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDirector = itemView.findViewById(R.id.tvDirector);
            ivCover = itemView.findViewById(R.id.ivCover);
            layoutTextInfo = itemView.findViewById(R.id.layoutTextInfo);
        }
    }
}