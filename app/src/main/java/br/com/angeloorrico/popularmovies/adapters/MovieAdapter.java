package br.com.angeloorrico.popularmovies.adapters;

import android.content.Context;
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
import br.com.angeloorrico.popularmovies.models.MovieModel;

/**
 * Created by Angelo on 29/10/2016.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    Context mContext;
    List<MovieModel> mMoviesList;

    public MovieAdapter(Context mContext) {
        this.mContext = mContext;
        this.mMoviesList = new ArrayList<>();
    }

    public void setMoviesList(List<MovieModel> moviesList) {
        this.mMoviesList = moviesList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.movie_item_view,
                parent,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Picasso.with(mContext)
                .load(BuildConfig.POSTER_BASE_URL + mMoviesList.get(position).getPosterPath())
                .placeholder(R.drawable.generic_movie)
                .error(R.drawable.generic_movie)
                .into(holder.mImgMovie);
    }

    @Override
    public int getItemCount() {
        return mMoviesList.size();
    }

    public MovieModel getItem(int position) {
        return mMoviesList.get(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImgMovie;

        public ViewHolder(View itemView) {
            super(itemView);
            this.mImgMovie = (ImageView) itemView;
        }
    }

}