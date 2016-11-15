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

import br.com.angeloorrico.popularmovies.R;
import br.com.angeloorrico.popularmovies.models.MovieModel;
import br.com.angeloorrico.popularmovies.utils.Utils;

/**
 * Created by Angelo on 29/10/2016.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    Context mContext;
    List<MovieModel> mMoviesList;

    int mSelectedPosition = -1;

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
                .load(Utils.getImageURL(true) + mMoviesList.get(position).getPosterPath())
                .placeholder(R.drawable.generic_movie)
                .error(R.drawable.generic_movie)
                .into(holder.mImgMovie);

        holder.setSelected(position == mSelectedPosition);
    }

    @Override
    public int getItemCount() {
        return mMoviesList.size();
    }

    public MovieModel getItem(int position) {
        return mMoviesList.get(position);
    }

    public void setSelectedPosition(int selectedPosition) {
        this.mSelectedPosition = selectedPosition;
    }

    public int getSelectedPosition() {
        return mSelectedPosition;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImgMovie;

        public ViewHolder(View itemView) {
            super(itemView);
            this.mImgMovie = (ImageView) itemView;
        }

        public void setSelected(boolean isSelected) {
            if (isSelected)
                mImgMovie.setAlpha(0.5f);
            else
                mImgMovie.setAlpha(1.0f);
        }
    }

}