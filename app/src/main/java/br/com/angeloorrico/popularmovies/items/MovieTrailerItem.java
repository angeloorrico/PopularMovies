package br.com.angeloorrico.popularmovies.items;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import br.com.angeloorrico.popularmovies.BuildConfig;
import br.com.angeloorrico.popularmovies.R;
import br.com.angeloorrico.popularmovies.models.TrailerModel;

/**
 * Created by Angelo on 18/11/2016.
 */

public class MovieTrailerItem extends LinearLayout {

    Context mContext;

    public MovieTrailerItem(Context context, TrailerModel model) {
        super(context);
        this.mContext = context;

        inflate(context, R.layout.trailer_item_view, this);
        bindModel(model);
    }

    private void bindModel(final TrailerModel model) {
        ImageView ivThumb = (ImageView) findViewById(R.id.iv_thumb);
        TextView tvName = (TextView) findViewById(R.id.tv_name);

        Picasso.with(mContext)
                .load(String.format(BuildConfig.YOUTUBE_THUMB_URL, model.getKey()))
                .placeholder(R.drawable.generic_movie)
                .into(ivThumb);
        tvName.setText(model.getName());

        ivThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(BuildConfig.YOUTUBE_URL + model.getKey()));
                mContext.startActivity(intent);
            }
        });
    }

}
