package br.com.angeloorrico.popularmovies.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import br.com.angeloorrico.popularmovies.BuildConfig;
import br.com.angeloorrico.popularmovies.R;
import br.com.angeloorrico.popularmovies.models.MovieModel;

/**
 * Created by Angelo on 27/10/2016.
 */

public class MovieDetailFragment extends Fragment {

    MovieModel movie;

    RelativeLayout movieDetailContainer;
    LinearLayout noDataContainer;
    TextView tvError, tvOverview, tvTitle, tvReleaseDate, tvVoteAverage;
    ImageView ivMovie;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movie = (MovieModel) getActivity().getIntent()
                .getSerializableExtra(getString(R.string.movie_extra));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_movie_detail, container, false);

        movieDetailContainer = (RelativeLayout)rootView.findViewById(R.id.movie_detail_container);
        ivMovie = (ImageView)rootView.findViewById(R.id.iv_movie);
        noDataContainer = (LinearLayout)rootView.findViewById(R.id.no_data_container);
        tvOverview = (TextView)rootView.findViewById(R.id.tv_overview);
        tvTitle = (TextView)rootView.findViewById(R.id.tv_title);
        tvReleaseDate = (TextView)rootView.findViewById(R.id.tv_release_date);
        tvVoteAverage = (TextView)rootView.findViewById(R.id.tv_vote_average);
        tvError = (TextView)rootView.findViewById(R.id.tv_error);

        if (movie == null) {
            tvError.setText(getString(R.string.msg_no_data));
            noDataContainer.setVisibility(View.VISIBLE);
            movieDetailContainer.setVisibility(View.GONE);
        } else
            loadMovieDetails();

        return rootView;
    }

    private void loadMovieDetails() {
        getActivity().setTitle(movie.getTitle());

        Picasso.with(getActivity())
                .load(BuildConfig.POSTER_BASE_URL + movie.getPosterPath())
                .placeholder(R.drawable.generic_movie)
                .error(R.drawable.generic_movie)
                .into(ivMovie);

        tvTitle.setText(movie.getTitle());
        tvReleaseDate.setText(movie.getReleaseDate());
        tvOverview.setText(movie.getOverview());
        tvVoteAverage.setText(String.format(getString(R.string.msg_vote_average), movie.getVoteAverage()));
    }

}