package br.com.angeloorrico.popularmovies.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import br.com.angeloorrico.popularmovies.R;
import br.com.angeloorrico.popularmovies.activities.MainActivity;
import br.com.angeloorrico.popularmovies.models.MovieModel;
import br.com.angeloorrico.popularmovies.utils.Utils;

/**
 * Created by Angelo on 27/10/2016.
 */

public class MovieDetailFragment extends Fragment {

    MovieModel mMovie;

    LinearLayout mMovieDetailContainer, mNoDataContainer;
    TextView mTvError, mTvOverview, mTvTitle, mTvReleaseDate, mTvVoteAverage;
    ImageView mIvMoviePoster, mIvMovieBackdrop;
    View mViewSeparator;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_movie_detail, container, false);

        mMovieDetailContainer = (LinearLayout) rootView.findViewById(R.id.movie_detail_container);
        mIvMoviePoster = (ImageView)rootView.findViewById(R.id.iv_movie_poster);
        mIvMovieBackdrop = (ImageView)rootView.findViewById(R.id.iv_movie_backdrop);
        mNoDataContainer = (LinearLayout)rootView.findViewById(R.id.no_data_container);
        mTvOverview = (TextView)rootView.findViewById(R.id.tv_overview);
        mTvTitle = (TextView)rootView.findViewById(R.id.tv_title);
        mTvReleaseDate = (TextView)rootView.findViewById(R.id.tv_release_date);
        mTvVoteAverage = (TextView)rootView.findViewById(R.id.tv_vote_average);
        mTvError = (TextView)rootView.findViewById(R.id.tv_error);
        mViewSeparator = rootView.findViewById(R.id.view_separator);

        if (getArguments() != null)
            loadMovieDetails();
        else
            showNoDataView(getString(R.string.msg_select_a_movie));

        return rootView;
    }

    public void loadMovieDetails() {
        mMovie = getArguments().getParcelable(MovieModel.MOVIE_PARCELABLE_PARAM);
        if (mMovie == null) {
            showNoDataView(getString(R.string.msg_no_data));
            return;
        }
        getActivity().setTitle(mMovie.getTitle());

        Picasso.with(getActivity())
                .load(Utils.getImageURL(true) + mMovie.getPosterPath())
                .placeholder(R.drawable.generic_movie)
                .error(R.drawable.generic_movie)
                .into(mIvMoviePoster);
        if (getContext() instanceof MainActivity) {
            mIvMovieBackdrop.setVisibility(View.VISIBLE);
            Picasso.with(getActivity())
                    .load(Utils.getImageURL(false) + mMovie.getBackdropPath())
                    .placeholder(R.drawable.generic_movie)
                    .error(R.drawable.generic_movie)
                    .into(mIvMovieBackdrop);
        }

        mMovieDetailContainer.setVisibility(View.VISIBLE);
        mTvTitle.setText(mMovie.getTitle());
        mTvReleaseDate.setText(mMovie.getReleaseDate());
        mTvOverview.setText(mMovie.getOverview());
        mTvVoteAverage.setText(String.format(getString(R.string.vote_average), mMovie.getVoteAverage()));
    }

    private void showNoDataView(String message) {
        mTvError.setText(message);
        mNoDataContainer.setVisibility(View.VISIBLE);
        mMovieDetailContainer.setVisibility(View.GONE);
        mTvOverview.setVisibility(View.GONE);
        mViewSeparator.setVisibility(View.GONE);
    }

}