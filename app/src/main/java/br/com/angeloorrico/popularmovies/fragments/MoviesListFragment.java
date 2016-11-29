package br.com.angeloorrico.popularmovies.fragments;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.angeloorrico.popularmovies.R;
import br.com.angeloorrico.popularmovies.adapters.MovieAdapter;
import br.com.angeloorrico.popularmovies.adapters.RecyclerItemClickListener;
import br.com.angeloorrico.popularmovies.connection.MovieTask;
import br.com.angeloorrico.popularmovies.database.MovieTable;
import br.com.angeloorrico.popularmovies.interfaces.MoviesConnector;
import br.com.angeloorrico.popularmovies.models.MovieModel;
import br.com.angeloorrico.popularmovies.models.MovieResponseModel;
import br.com.angeloorrico.popularmovies.providers.MoviesContentProvider;
import br.com.angeloorrico.popularmovies.utils.Utils;

/**
 * Created by Angelo on 25/10/2016.
 */

public class MoviesListFragment extends Fragment implements MoviesConnector {

    private final int SORT_BY_MOST_POPULAR  = 0;
    private final int SORT_BY_HIGHEST_RATED = 1;

    LinearLayout mNoDataContainer;
    TextView mTvError;
    RecyclerView mRvMovies;
    SwipeRefreshLayout mSwipeContainer;

    MovieAdapter mMoviesAdapter;
    MovieResponseModel mMoviesList;

    RecyclerView.LayoutManager mLayoutManager;

    ItemCallback mCallback;

    boolean mIsTablet;

    int selectedPosition = -1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity)
            this.mCallback = (ItemCallback)context;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(MovieModel.MOVIE_PARCELABLE_PARAM, mMoviesList);
        outState.putInt(MovieModel.SELECTED_MOVIE_PARCELABLE_PARAM,
                mMoviesAdapter.getSelectedPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null)
            fetchMoviesList();
        else {
            selectedPosition = savedInstanceState.getInt(MovieModel.SELECTED_MOVIE_PARCELABLE_PARAM);
            onConnectionResult(savedInstanceState.get(MovieModel.MOVIE_PARCELABLE_PARAM));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_movies_list, container, false);

        mNoDataContainer = (LinearLayout)rootView.findViewById(R.id.no_data_container);
        mTvError = (TextView)rootView.findViewById(R.id.tv_error);
        mRvMovies = (RecyclerView) rootView.findViewById(R.id.rv_movies);
        mSwipeContainer = (SwipeRefreshLayout)rootView.findViewById(R.id.swipe_container);

        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchMoviesList();
            }
        });

        mRvMovies.setHasFixedSize(true);
        mRvMovies.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        mCallback.onItemSelected(view, mMoviesAdapter.getItem(position));
                        if (mIsTablet) {
                            if (mMoviesAdapter.getSelectedPosition() > -1)
                                mMoviesAdapter.notifyItemChanged(mMoviesAdapter.getSelectedPosition());
                            mMoviesAdapter.setSelectedPosition(position);
                            mMoviesAdapter.notifyItemChanged(position, null);
                        }
                    }
                })
        );
        mLayoutManager = new GridLayoutManager(getActivity(),
                getResources().getInteger(R.integer.gallery_columns));
        mRvMovies.setLayoutManager(mLayoutManager);
        mMoviesAdapter = new MovieAdapter(getActivity());
        mRvMovies.setAdapter(mMoviesAdapter);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_movies_list, menu);

        if (getSortByPreference() == SORT_BY_MOST_POPULAR)
            menu.findItem(R.id.mn_sort_by_most_popular).setChecked(true);
        else
            menu.findItem(R.id.mn_sort_by_highest_rated).setChecked(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        item.setChecked(item.isChecked());

        if (item.getItemId() == R.id.mn_sort_by_most_popular) {
            saveSortByPreference(SORT_BY_MOST_POPULAR);
            fetchMoviesList();
        } else if (item.getItemId() == R.id.mn_sort_by_highest_rated) {
            saveSortByPreference(SORT_BY_HIGHEST_RATED);
            fetchMoviesList();
        } else
            getFavoritesMovies();

        return true;
    }

    private void fetchMoviesList() {
        if (mIsTablet)
            mCallback.onItemSelected(null, null);

        if (Utils.hasInternetConnection(getActivity())) {
            mRvMovies.setVisibility(View.VISIBLE);
            mNoDataContainer.setVisibility(View.GONE);
            mSwipeContainer.setRefreshing(true);

            MovieTask movieTask = new MovieTask();
            movieTask.setConnector(this);
            movieTask.execute(getSortByPreference() == SORT_BY_MOST_POPULAR ?
                    MovieTask.PARAM_MOST_POPULAR : MovieTask.PARAM_TOP_RATED);
        } else {
            mTvError.setText(getString(R.string.msg_no_internet_connection));
            mRvMovies.setVisibility(View.GONE);
            mNoDataContainer.setVisibility(View.VISIBLE);
            mSwipeContainer.setRefreshing(false);
        }
    }

    private void getFavoritesMovies() {
        Cursor cursor = getActivity().getContentResolver()
                .query(MoviesContentProvider.CONTENT_URI_MOVIES, null, null, null, null);

        if (cursor != null) {
            ArrayList<MovieModel> results = new ArrayList<>();
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                results.add(cursorToMovie(cursor));
                cursor.moveToNext();
            }
            cursor.close();

            MovieResponseModel responseModel = new MovieResponseModel();
            responseModel.setResults(results);
            onConnectionResult(responseModel);
        } else
            onConnectionResult(null);
    }

    private MovieModel cursorToMovie(Cursor cursor) {
        MovieModel movie = new MovieModel();

        movie.setId(cursor.getInt(cursor
                .getColumnIndexOrThrow(MovieTable.COLUMN_ID)));
        movie.setTitle(cursor.getString(cursor
                .getColumnIndexOrThrow(MovieTable.COLUMN_TITLE)));
        movie.setBackdropPath(cursor.getString(cursor
                .getColumnIndexOrThrow(MovieTable.COLUMN_BACKDROP_PATH)));
        movie.setPosterPath(cursor.getString(cursor
                .getColumnIndexOrThrow(MovieTable.COLUMN_POSTER_PATH)));
        movie.setOverview(cursor.getString(cursor
                .getColumnIndexOrThrow(MovieTable.COLUMN_OVERVIEW)));
        movie.setReleaseDate(new Date(cursor.getString(cursor
                .getColumnIndexOrThrow(MovieTable.COLUMN_RELEASE_DATE))));
        movie.setVoteAverage(cursor.getString(cursor
                .getColumnIndexOrThrow(MovieTable.COLUMN_VOTE_AVERAGE)));

        return  movie;
    }

    private void saveSortByPreference(int prefValue) {
        PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putInt(
                getString(R.string.sort_by_pref), prefValue).commit();
    }

    private int getSortByPreference() {
        return PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt(
                getString(R.string.sort_by_pref), SORT_BY_MOST_POPULAR);
    }

    @Override
    public void onConnectionResult(Object responseData) {
        mSwipeContainer.setRefreshing(false);
        if (responseData != null) {
            mMoviesList = (MovieResponseModel) responseData;
            mMoviesAdapter.setMoviesList(mMoviesList.getResults());
            if (selectedPosition > -1) {
                mMoviesAdapter.setSelectedPosition(selectedPosition);
                mRvMovies.smoothScrollToPosition(selectedPosition);
            }
            mRvMovies.setVisibility(View.VISIBLE);
            mNoDataContainer.setVisibility(View.GONE);
        } else {
            mTvError.setText(getString(R.string.msg_no_data));
            mRvMovies.setVisibility(View.GONE);
            mNoDataContainer.setVisibility(View.VISIBLE);
        }
    }

    public void setTabletDevice(boolean isTablet) {
        this.mIsTablet = isTablet;

        mRvMovies.setItemAnimator(new DefaultItemAnimator() {
            @Override
            public boolean canReuseUpdatedViewHolder(@NonNull RecyclerView.ViewHolder viewHolder) {
                return true;
            }

            @Override
            public boolean canReuseUpdatedViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, @NonNull List<Object> payloads) {
                return true;
            }
        });
    }

    public interface ItemCallback {
        void onItemSelected(View view, MovieModel selectedMovie);
    }

}