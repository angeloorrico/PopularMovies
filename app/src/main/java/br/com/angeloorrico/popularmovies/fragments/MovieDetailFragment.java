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

import java.util.List;

import br.com.angeloorrico.popularmovies.R;
import br.com.angeloorrico.popularmovies.activities.MainActivity;
import br.com.angeloorrico.popularmovies.connection.ReviewTask;
import br.com.angeloorrico.popularmovies.connection.TrailerTask;
import br.com.angeloorrico.popularmovies.interfaces.MoviesConnector;
import br.com.angeloorrico.popularmovies.items.MovieTrailerItem;
import br.com.angeloorrico.popularmovies.items.ReviewTrailerItem;
import br.com.angeloorrico.popularmovies.models.MovieModel;
import br.com.angeloorrico.popularmovies.models.ReviewModel;
import br.com.angeloorrico.popularmovies.models.ReviewResponseModel;
import br.com.angeloorrico.popularmovies.models.TrailerModel;
import br.com.angeloorrico.popularmovies.models.TrailerResponseModel;
import br.com.angeloorrico.popularmovies.utils.Utils;

/**
 * Created by Angelo on 27/10/2016.
 */

public class MovieDetailFragment extends Fragment implements MoviesConnector {

    MovieModel mMovie;

    LinearLayout mMovieDetailContainer, mTrailersContainer, mReviewsContainer, mNoDataContainer;
    TextView mTvError, mTvOverview, mTvTitle, mTvReleaseDate, mTvVoteAverage;
    ImageView mIvMoviePoster, mIvMovieBackdrop;
    View mViewSeparator;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_movie_detail, container, false);

        mMovieDetailContainer = (LinearLayout) rootView.findViewById(R.id.movie_detail_container);
        mTrailersContainer = (LinearLayout) rootView.findViewById(R.id.trailers_container);
        mReviewsContainer = (LinearLayout) rootView.findViewById(R.id.reviews_container);
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
        TrailerTask trailerTask = new TrailerTask();
        trailerTask.setConnector(this);
        trailerTask.execute(String.valueOf(mMovie.getId()));

        ReviewTask reviewTask = new ReviewTask();
        reviewTask.setConnector(this);
        reviewTask.execute(String.valueOf(mMovie.getId()));

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
        mTrailersContainer.setVisibility(View.GONE);
        mReviewsContainer.setVisibility(View.GONE);
        mTvOverview.setVisibility(View.GONE);
        mViewSeparator.setVisibility(View.GONE);
    }

    @Override
    public void onConnectionResult(Object responseData) {
        if (responseData instanceof ReviewResponseModel) {
            showReviews(((ReviewResponseModel) responseData).getResults());
        } else if (responseData instanceof TrailerResponseModel) {
            showTrailers(((TrailerResponseModel) responseData).getResults());
        }
    }

    private void showTrailers(List<TrailerModel> trailers) {
        if (trailers != null && trailers.size() > 0) {
            mTrailersContainer.setVisibility(View.VISIBLE);
            for (TrailerModel trailer : trailers)
                mTrailersContainer.addView(
                        new MovieTrailerItem(getContext(), trailer));
        }
    }

    private void showReviews(List<ReviewModel> reviews) {
        if (reviews != null && reviews.size() > 0) {
            mReviewsContainer.setVisibility(View.VISIBLE);
            for (ReviewModel review : reviews)
                mReviewsContainer.addView(
                        new ReviewTrailerItem(getContext(), review));
        }
    }

}