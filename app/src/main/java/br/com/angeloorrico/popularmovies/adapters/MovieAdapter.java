package br.com.angeloorrico.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import br.com.angeloorrico.popularmovies.BuildConfig;
import br.com.angeloorrico.popularmovies.R;
import br.com.angeloorrico.popularmovies.activities.MovieDetailActivity;
import br.com.angeloorrico.popularmovies.models.MovieModel;

/**
 * Created by Angelo on 29/10/2016.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    Context context;
    List<MovieModel> moviesList;

    public MovieAdapter(Context context) {
        this.context = context;
        this.moviesList = new ArrayList<>();
    }

    public void setMoviesList(List<MovieModel> moviesList) {
        this.moviesList = moviesList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.movie_item_view,
                parent,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Picasso.with(context)
                .load(BuildConfig.POSTER_BASE_URL + moviesList.get(position).getPosterPath())
                .placeholder(R.drawable.generic_movie)
                .error(R.drawable.generic_movie)
                .into(holder.imgMovie);
        holder.imgMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MovieDetailActivity.class);
                intent.putExtra(context.getString(R.string.movie_extra), moviesList.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public MovieModel getItem(int position) {
        return moviesList.get(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgMovie;

        public ViewHolder(View itemView) {
            super(itemView);
            this.imgMovie = (ImageView) itemView;
        }
    }

}