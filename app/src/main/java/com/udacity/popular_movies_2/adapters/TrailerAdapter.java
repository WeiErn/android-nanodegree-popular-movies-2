package com.udacity.popular_movies_2.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.udacity.popular_movies_2.R;
import com.udacity.popular_movies_2.database.Trailer;

import java.util.List;

import static androidx.core.content.ContextCompat.startActivity;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {

    private static final String TAG = TrailerAdapter.class.getSimpleName();
    private List<Trailer> mTrailerData;
    private final TrailerAdapterOnClickHandler mClickHandler;
//    private Context mContext;

    public interface TrailerAdapterOnClickHandler {
        void onClick(Trailer trailer);
    }

    public TrailerAdapter(TrailerAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder {
        public final TextView mTrailerTitle;
        public final Button mTrailerPlayButton;

        public TrailerAdapterViewHolder(View view) {
            super(view);
            mTrailerTitle = view.findViewById(R.id.trailer_title);
            mTrailerPlayButton = view.findViewById(R.id.btn_play_trailer);
//            mTrailerPlayButton.setOnClickListener(this);
        }
    }

    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.fragment_trailer;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TrailerAdapter.TrailerAdapterViewHolder trailerAdapterViewHolder, int position) {
        final Trailer trailerSelected = mTrailerData.get(position);
        trailerAdapterViewHolder.mTrailerPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = trailerSelected.getUrl();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                trailerAdapterViewHolder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (null == mTrailerData) return 0;
        return mTrailerData.size();
    }

    public void setTrailerData(List<Trailer> trailerData) {
        mTrailerData = trailerData;
        notifyDataSetChanged();
    }
}
