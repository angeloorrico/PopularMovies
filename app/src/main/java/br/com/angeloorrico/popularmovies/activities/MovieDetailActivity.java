package br.com.angeloorrico.popularmovies.activities;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import br.com.angeloorrico.popularmovies.BuildConfig;
import br.com.angeloorrico.popularmovies.R;
import br.com.angeloorrico.popularmovies.models.MovieModel;

public class MovieDetailActivity extends AppCompatActivity {

    CollapsingToolbarLayout mCollapsingToolbarLayout;
    ImageView mIvToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actv_movie_detail);

        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCollapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.ctbl);
        mCollapsingToolbarLayout.setExpandedTitleColor(getResources()
                .getColor(android.R.color.transparent));

        if (Build.VERSION.SDK_INT >= 21)
            getWindow().setStatusBarColor(Color.TRANSPARENT);

        mIvToolbar = (ImageView) findViewById(R.id.iv_toolbar);
        MovieModel movieModel = (MovieModel) getIntent()
                .getSerializableExtra(getString(R.string.movie_extra));
        Picasso.with(this)
                .load(BuildConfig.POSTER_BASE_URL + movieModel.getBackdropPath())
                .into(mIvToolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}