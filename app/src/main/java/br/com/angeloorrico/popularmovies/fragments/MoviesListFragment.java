package br.com.angeloorrico.popularmovies.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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

import br.com.angeloorrico.popularmovies.R;
import br.com.angeloorrico.popularmovies.activities.MovieDetailActivity;
import br.com.angeloorrico.popularmovies.adapters.MovieAdapter;
import br.com.angeloorrico.popularmovies.adapters.RecyclerItemClickListener;
import br.com.angeloorrico.popularmovies.connection.MovieTask;
import br.com.angeloorrico.popularmovies.interfaces.MoviesConnector;
import br.com.angeloorrico.popularmovies.models.MovieResponseModel;
import br.com.angeloorrico.popularmovies.utils.Utils;

/**
 * Created by Angelo on 25/10/2016.
 */

public class MoviesListFragment extends Fragment implements MoviesConnector {

    private final int SORT_BY_MOST_POPULAR  = 0;
    private final int SORT_BY_HIGHEST_RATED = 1;

    LinearLayout noDataContainer;
    TextView tvError;
    RecyclerView rvMovies;
    SwipeRefreshLayout swipeContainer;

    MovieAdapter moviesAdapter;

    RecyclerView.LayoutManager layoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_movies_list, container, false);

        noDataContainer = (LinearLayout)rootView.findViewById(R.id.no_data_container);
        tvError = (TextView)rootView.findViewById(R.id.tv_error);
        rvMovies = (RecyclerView) rootView.findViewById(R.id.rv_movies);
        swipeContainer = (SwipeRefreshLayout)rootView.findViewById(R.id.swipe_container);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchMoviesList();
            }
        });

        rvMovies.setHasFixedSize(true);
        rvMovies.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                        intent.putExtra(getActivity().getString(R.string.movie_extra),
                                moviesAdapter.getItem(position));
                        startActivity(intent);
                    }
                })
        );
        layoutManager = new GridLayoutManager(getActivity(),
                getResources().getInteger(R.integer.gallery_columns));
        rvMovies.setLayoutManager(layoutManager);
        moviesAdapter = new MovieAdapter(getActivity());
        rvMovies.setAdapter(moviesAdapter);

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

        if (item.getItemId() == R.id.mn_sort_by_most_popular)
            saveSortByPreference(SORT_BY_MOST_POPULAR);
        else
            saveSortByPreference(SORT_BY_HIGHEST_RATED);

        fetchMoviesList();
        return true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fetchMoviesList();
    }

    private void fetchMoviesList() {
        if (Utils.hasInternetConnection(getActivity())) {
            rvMovies.setVisibility(View.VISIBLE);
            noDataContainer.setVisibility(View.GONE);
            swipeContainer.setRefreshing(true);

            MovieTask movieTask = new MovieTask();
            movieTask.setConnector(this);
            movieTask.execute(getSortByPreference() == SORT_BY_MOST_POPULAR ?
                    MovieTask.PARAM_MOST_POPULAR : MovieTask.PARAM_TOP_RATED);
        } else {
            tvError.setText(getString(R.string.msg_no_internet_connection));
            rvMovies.setVisibility(View.GONE);
            noDataContainer.setVisibility(View.VISIBLE);
            swipeContainer.setRefreshing(false);
        }
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
        swipeContainer.setRefreshing(false);
        if (responseData != null) {
            MovieResponseModel moviesList = (MovieResponseModel) responseData;
            moviesAdapter.setMoviesList(moviesList.getResults());
        } else {
            tvError.setText(getString(R.string.msg_no_data));
            rvMovies.setVisibility(View.GONE);
            noDataContainer.setVisibility(View.VISIBLE);
        }
    }
}