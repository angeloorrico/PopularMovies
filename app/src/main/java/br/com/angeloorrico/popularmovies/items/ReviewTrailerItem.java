package br.com.angeloorrico.popularmovies.items;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.angeloorrico.popularmovies.R;
import br.com.angeloorrico.popularmovies.models.ReviewModel;

/**
 * Created by Angelo on 18/11/2016.
 */

public class ReviewTrailerItem extends LinearLayout {

    Context mContext;

    public ReviewTrailerItem(Context context, ReviewModel model) {
        super(context);
        this.mContext = context;

        inflate(context, R.layout.review_item_view, this);
        bindModel(model);
    }

    private void bindModel(ReviewModel model) {
        TextView tvContent = (TextView) findViewById(R.id.tv_content);
        TextView tvAuthor = (TextView) findViewById(R.id.tv_author);

        tvContent.setText(model.getContent());
        tvAuthor.setText(String.format(
                mContext.getString(R.string.label_by), model.getAuthor()));
    }

}
