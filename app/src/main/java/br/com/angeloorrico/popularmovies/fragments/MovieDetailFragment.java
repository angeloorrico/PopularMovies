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

    MovieModel mMovie;

    RelativeLayout mMovieDetailContainer;
    LinearLayout mNoDataContainer;
    TextView mTvError, mTvOverview, mTvTitle, mTvReleaseDate, mTvVoteAverage;
    ImageView mIvMovie;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMovie = (MovieModel) getActivity().getIntent()
                .getSerializableExtra(getString(R.string.movie_extra));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_movie_detail, container, false);

        mMovieDetailContainer = (RelativeLayout)rootView.findViewById(R.id.movie_detail_container);
        mIvMovie = (ImageView)rootView.findViewById(R.id.iv_movie);
        mNoDataContainer = (LinearLayout)rootView.findViewById(R.id.no_data_container);
        mTvOverview = (TextView)rootView.findViewById(R.id.tv_overview);
        mTvTitle = (TextView)rootView.findViewById(R.id.tv_title);
        mTvReleaseDate = (TextView)rootView.findViewById(R.id.tv_release_date);
        mTvVoteAverage = (TextView)rootView.findViewById(R.id.tv_vote_average);
        mTvError = (TextView)rootView.findViewById(R.id.tv_error);

        if (mMovie == null) {
            mTvError.setText(getString(R.string.msg_no_data));
            mNoDataContainer.setVisibility(View.VISIBLE);
            mMovieDetailContainer.setVisibility(View.GONE);
        } else
            loadMovieDetails();

        return rootView;
    }

    private void loadMovieDetails() {
        getActivity().setTitle(mMovie.getTitle());

        Picasso.with(getActivity())
                .load(BuildConfig.POSTER_BASE_URL + mMovie.getPosterPath())
                .placeholder(R.drawable.generic_movie)
                .error(R.drawable.generic_movie)
                .into(mIvMovie);

        mTvTitle.setText(mMovie.getTitle());
        mTvReleaseDate.setText(mMovie.getReleaseDate());
        mTvOverview.setText(mMovie.getOverview());
        mTvVoteAverage.setText(String.format(getString(R.string.msg_vote_average), mMovie.getVoteAverage()));
    }

}