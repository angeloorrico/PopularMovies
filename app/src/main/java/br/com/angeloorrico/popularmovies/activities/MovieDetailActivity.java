package br.com.angeloorrico.popularmovies.activities;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import br.com.angeloorrico.popularmovies.R;
import br.com.angeloorrico.popularmovies.models.MovieModel;
import br.com.angeloorrico.popularmovies.utils.Utils;

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

        mIvToolbar = (ImageView) findViewById(R.id.iv_toolbar);
        MovieModel movieModel = getIntent()
                .getParcelableExtra(MovieModel.MOVIE_PARCELABLE_PARAM);
        Picasso.with(this)
                .load(Utils.getImageURL(false) + movieModel.getBackdropPath())
                .placeholder(R.drawable.generic_movie)
                .error(R.drawable.generic_movie)
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