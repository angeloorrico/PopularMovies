package br.com.angeloorrico.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.angeloorrico.popularmovies.R;
import br.com.angeloorrico.popularmovies.models.TrailerModel;

/**
 * Created by Angelo on 18/11/2016.
 */

public class TrailerAdapter extends ArrayAdapter<TrailerModel> {

    Context mContext;
    List<TrailerModel> mTrailerModels;

    public TrailerAdapter(Context context, int resource, List<TrailerModel> trailerModels) {
        super(context, resource);
        this.mContext = context;
        this.mTrailerModels = trailerModels;
    }

    @Nullable
    @Override
    public TrailerModel getItem(int position) {
        return mTrailerModels.get(position);
    }

    @Override
    public int getCount() {
        return mTrailerModels.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.trailer_item_view, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) view.getTag();

        final TrailerModel model = getItem(position);
        viewHolder.mTvName.setText(model.getName());
        Picasso.with(mContext).load(String.format("https://img.youtube.com/vi/%s/0.jpg", model.getKey())).into(viewHolder.mIvThumb);

        viewHolder.mIvThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + model.getKey()));
                mContext.startActivity(intent);
            }
        });

        return view;
    }

    public static class ViewHolder {
        TextView mTvName;
        ImageView mIvThumb;

        public ViewHolder(View view) {
            mTvName = (TextView) view.findViewById(R.id.tv_name);
            mIvThumb = (ImageView) view.findViewById(R.id.iv_thumb);
        }
    }

}
