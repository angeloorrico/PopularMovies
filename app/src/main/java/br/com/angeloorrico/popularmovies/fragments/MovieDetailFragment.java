package br.com.angeloorrico.popularmovies.fragments;

import android.animation.ObjectAnimator;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import br.com.angeloorrico.popularmovies.R;
import br.com.angeloorrico.popularmovies.activities.MainActivity;
import br.com.angeloorrico.popularmovies.database.MovieTable;
import br.com.angeloorrico.popularmovies.database.ReviewTable;
import br.com.angeloorrico.popularmovies.database.TrailerTable;
import br.com.angeloorrico.popularmovies.interfaces.MoviesConnector;
import br.com.angeloorrico.popularmovies.items.MovieTrailerItem;
import br.com.angeloorrico.popularmovies.items.ReviewTrailerItem;
import br.com.angeloorrico.popularmovies.models.MovieModel;
import br.com.angeloorrico.popularmovies.models.ReviewModel;
import br.com.angeloorrico.popularmovies.models.ReviewResponseModel;
import br.com.angeloorrico.popularmovies.models.TrailerModel;
import br.com.angeloorrico.popularmovies.models.TrailerResponseModel;
import br.com.angeloorrico.popularmovies.providers.MoviesContentProvider;
import br.com.angeloorrico.popularmovies.receivers.MovieReceiver;
import br.com.angeloorrico.popularmovies.services.MovieService;
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

    ImageView mIvFavorite;

    ArrayList<TrailerModel> mTrailers;
    ArrayList<ReviewModel> mReviews;

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

        mIvFavorite = (ImageView)rootView.findViewById(R.id.iv_favorite);
        mIvFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ObjectAnimator.ofFloat(view, "scaleX", 1.2f, 1.0f).setDuration(500).start();
                ObjectAnimator.ofFloat(view, "scaleY", 1.2f, 1.0f).setDuration(500).start();
                updateFavorites();
            }
        });

        if (savedInstanceState != null) {
            mReviews = savedInstanceState.getParcelableArrayList(ReviewModel.REVIEW_PARCELABLE_PARAM);
            mTrailers = savedInstanceState.getParcelableArrayList(TrailerModel.TRAILER_PARCELABLE_PARAM);
        }
        if (getArguments() != null)
            loadMovieDetails();
        else
            showNoDataView(getString(R.string.msg_select_a_movie));

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(ReviewModel.REVIEW_PARCELABLE_PARAM, mReviews);
        outState.putParcelableArrayList(TrailerModel.TRAILER_PARCELABLE_PARAM, mTrailers);
        super.onSaveInstanceState(outState);
    }

    private void loadMovieDetails() {
        mMovie = getArguments().getParcelable(MovieModel.MOVIE_PARCELABLE_PARAM);
        if (mMovie == null) {
            showNoDataView(getString(R.string.msg_no_data));
            return;
        }
        getActivity().setTitle(mMovie.getTitle());

        Uri uri = Uri.parse(MoviesContentProvider.CONTENT_URI_MOVIES + "/" + mMovie.getId());
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                mMovie.setFavorite(true);
                mIvFavorite.setBackground(getResources()
                        .getDrawable(R.drawable.remove_favorite_transition));
            }
            cursor.close();
        }
        loadTrailers();
        loadReviews();

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

    private void loadTrailers() {
        IntentFilter intentFilter = new IntentFilter(MovieService.BROADCAST_TRAILERS_ACTION);
        MovieReceiver movieReceiver = new MovieReceiver();
        movieReceiver.setConnector(this);
        LocalBroadcastManager.getInstance(getActivity())
                .registerReceiver(movieReceiver, intentFilter);

        if (mTrailers == null) {
            mTrailers = new ArrayList<>();
            if (mMovie.isFavorite()) {
                Uri uri = Uri.parse(MoviesContentProvider.CONTENT_URI_TRAILERS + "/" + mMovie.getId());
                Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    while(!cursor.isAfterLast()) {
                        TrailerModel tm = new TrailerModel();
                        tm.setId(cursor.getString(cursor
                                .getColumnIndexOrThrow(TrailerTable.COLUMN_ID)));
                        tm.setKey(cursor.getString(cursor
                                .getColumnIndexOrThrow(TrailerTable.COLUMN_KEY)));
                        tm.setName(cursor.getString(cursor
                                .getColumnIndexOrThrow(TrailerTable.COLUMN_NAME)));
                        mTrailers.add(tm);
                        cursor.moveToNext();
                    }
                    cursor.close();
                }
            } else {
                Intent intent = new Intent(getActivity(), MovieService.class);
                intent.putExtra(MovieService.PARAM_SELECTED_FILTER, MovieService.PARAM_TRAILERS);
                intent.putExtra(MovieService.PARAM_MOVIE_ID, String.valueOf(mMovie.getId()));
                getActivity().startService(intent);
            }
        }
        showTrailers();
    }

    private void loadReviews() {
        IntentFilter intentFilter = new IntentFilter(MovieService.BROADCAST_REVIEWS_ACTION);
        MovieReceiver movieReceiver = new MovieReceiver();
        movieReceiver.setConnector(this);
        LocalBroadcastManager.getInstance(getActivity())
                .registerReceiver(movieReceiver, intentFilter);

        if (mReviews == null) {
            mReviews = new ArrayList<>();
            if (mMovie.isFavorite()) {
                Uri uri = Uri.parse(MoviesContentProvider.CONTENT_URI_REVIEWS + "/" + mMovie.getId());
                Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    while(!cursor.isAfterLast()) {
                        ReviewModel rm = new ReviewModel();
                        rm.setId(cursor.getString(cursor
                                .getColumnIndexOrThrow(ReviewTable.COLUMN_ID)));
                        rm.setContent(cursor.getString(cursor
                                .getColumnIndexOrThrow(ReviewTable.COLUMN_CONTENT)));
                        rm.setAuthor(cursor.getString(cursor
                                .getColumnIndexOrThrow(ReviewTable.COLUMN_AUTHOR)));
                        mReviews.add(rm);
                        cursor.moveToNext();
                    }
                    cursor.close();
                }
            } else {
                Intent intent = new Intent(getActivity(), MovieService.class);
                intent.putExtra(MovieService.PARAM_SELECTED_FILTER, MovieService.PARAM_REVIEWS);
                intent.putExtra(MovieService.PARAM_MOVIE_ID, String.valueOf(mMovie.getId()));
                getActivity().startService(intent);
            }
        }
        showReviews();
    }

    public void showNoDataView(String message) {
        mTvError.setText(message);
        mNoDataContainer.setVisibility(View.VISIBLE);
        mIvMovieBackdrop.setVisibility(View.GONE);
        mMovieDetailContainer.setVisibility(View.GONE);
        mTrailersContainer.setVisibility(View.GONE);
        mReviewsContainer.setVisibility(View.GONE);
        mTvOverview.setVisibility(View.GONE);
        mViewSeparator.setVisibility(View.GONE);
    }

    @Override
    public void onConnectionResult(Object responseData) {
        if (responseData instanceof ReviewResponseModel) {
            mReviews = new ArrayList(((ReviewResponseModel) responseData).getResults());
            showReviews();
        } else if (responseData instanceof TrailerResponseModel) {
            mTrailers = new ArrayList(((TrailerResponseModel) responseData).getResults());
            showTrailers();
        }
    }

    private void showTrailers() {
        if (getContext() == null)
            return;
        if (mTrailers != null && mTrailers.size() > 0) {
            mTrailersContainer.setVisibility(View.VISIBLE);
            for (TrailerModel trailer : mTrailers)
                mTrailersContainer.addView(
                        new MovieTrailerItem(getContext(), trailer));
        }
    }

    private void showReviews() {
        if (getContext() == null)
            return;
        if (mReviews != null && mReviews.size() > 0) {
            mReviewsContainer.setVisibility(View.VISIBLE);
            for (ReviewModel review : mReviews)
                mReviewsContainer.addView(
                        new ReviewTrailerItem(getContext(), review));
        }
    }

    private void updateFavorites() {
        TransitionDrawable transition = (TransitionDrawable) mIvFavorite.getBackground();
        if (mMovie.isFavorite()) {
            transition.reverseTransition(400);
            Uri uri = Uri.parse(MoviesContentProvider.CONTENT_URI_MOVIES + "/" + mMovie.getId());
            int rows = getActivity().getContentResolver().delete(uri, null, null);
            if (rows > 0) {
                Snackbar.make(getView(), getString(R.string.msg_favorite_deleted), Snackbar.LENGTH_LONG).show();
                mMovie.setFavorite(false);
                mIvFavorite.setBackground(getResources()
                        .getDrawable(R.drawable.favorite_transition));
            }
        } else {
            transition.startTransition(400);
            ContentValues values = new ContentValues();
            values.put(MovieTable.COLUMN_ID, mMovie.getId());
            values.put(MovieTable.COLUMN_BACKDROP_PATH, mMovie.getBackdropPath());
            values.put(MovieTable.COLUMN_OVERVIEW, mMovie.getOverview());
            values.put(MovieTable.COLUMN_POSTER_PATH, mMovie.getPosterPath());
            values.put(MovieTable.COLUMN_RELEASE_DATE, mMovie.getReleaseDate());
            values.put(MovieTable.COLUMN_TITLE, mMovie.getTitle());
            values.put(MovieTable.COLUMN_VOTE_AVERAGE, mMovie.getVoteAverage());

            Uri uri = getActivity().getContentResolver().insert(
                    MoviesContentProvider.CONTENT_URI_MOVIES, values);
            if (uri != null) {
                if (mReviews != null) {
                    ArrayList<ContentValues> array = new ArrayList<>();
                    for (ReviewModel review : mReviews) {
                        values = new ContentValues();
                        values.put(ReviewTable.COLUMN_CONTENT, review.getContent());
                        values.put(ReviewTable.COLUMN_AUTHOR, review.getAuthor());
                        values.put(ReviewTable.COLUMN_MOVIE_ID, mMovie.getId());
                        array.add(values);
                    }
                    getActivity().getContentResolver().bulkInsert(
                            MoviesContentProvider.CONTENT_URI_REVIEWS,
                            array.toArray(new ContentValues[mReviews.size()]));
                }
                if (mTrailers != null) {
                    ArrayList<ContentValues> array = new ArrayList<>();
                    for (TrailerModel trailer : mTrailers) {
                        values = new ContentValues();
                        values.put(TrailerTable.COLUMN_KEY, trailer.getKey());
                        values.put(TrailerTable.COLUMN_NAME, trailer.getName());
                        values.put(TrailerTable.COLUMN_MOVIE_ID, mMovie.getId());
                        array.add(values);
                    }
                    getActivity().getContentResolver().bulkInsert(
                            MoviesContentProvider.CONTENT_URI_TRAILERS,
                            array.toArray(new ContentValues[mTrailers.size()]));
                }

                Snackbar.make(getView(), getString(R.string.msg_favorite_inserted), Snackbar.LENGTH_LONG).show();
                mMovie.setFavorite(true);
                mIvFavorite.setBackground(getResources()
                        .getDrawable(R.drawable.remove_favorite_transition));
            }
        }
    }

}