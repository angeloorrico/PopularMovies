package br.com.angeloorrico.popularmovies.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import br.com.angeloorrico.popularmovies.R;
import br.com.angeloorrico.popularmovies.fragments.MovieDetailFragment;
import br.com.angeloorrico.popularmovies.fragments.MoviesListFragment;
import br.com.angeloorrico.popularmovies.models.MovieModel;

/**
 * Created by Angelo on 25/10/2016.
 */

public class MainActivity extends AppCompatActivity implements MoviesListFragment.ItemCallback {

    private static final String MOVIE_DETAIL_FRAG_TAG = "MDFTAG";

    boolean isTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actv_movies_main);

        if (findViewById(R.id.movie_detail_container) != null) {
            isTablet = true;

            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().add(
                        R.id.movie_detail_container,
                        new MovieDetailFragment(),
                        MOVIE_DETAIL_FRAG_TAG
                ).commit();
            }
        } else
            isTablet = false;
    }

    @Override
    public void onItemSelected(View view, MovieModel selectedMovie) {
        if (isTablet) {
            MovieDetailFragment fragment = new  MovieDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(MovieModel.MOVIE_PARCELABLE_PARAM, selectedMovie);
            fragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, MOVIE_DETAIL_FRAG_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra(MovieModel.MOVIE_PARCELABLE_PARAM, selectedMovie);
            ActivityOptionsCompat options =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                            this, view, getString(R.string.image_transition));

            startActivity(intent, options.toBundle());
        }
    }

}