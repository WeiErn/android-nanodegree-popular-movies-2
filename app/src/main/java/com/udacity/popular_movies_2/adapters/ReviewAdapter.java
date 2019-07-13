package com.udacity.popular_movies_2.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.udacity.popular_movies_2.R;
import com.udacity.popular_movies_2.database.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    private static final String TAG = ReviewAdapter.class.getSimpleName();
    private List<Review> mReviewData;
    private final ReviewAdapterOnClickHandler mClickHandler;

    public interface ReviewAdapterOnClickHandler {
    }

    public ReviewAdapter(ReviewAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {
        public final TextView mReviewAuthor;
        public final TextView mReviewContent;
        public final TextView mReviewUrl;

        public ReviewAdapterViewHolder(View view) {
            super(view);
            mReviewAuthor = view.findViewById(R.id.review_author);
            mReviewContent = view.findViewById(R.id.review_content);
            mReviewUrl = view.findViewById(R.id.review_url);
        }
    }

    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.fragment_review;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ReviewAdapter.ReviewAdapterViewHolder reviewAdapterViewHolder, int position) {
        final Review reviewSelected = mReviewData.get(position);
        reviewAdapterViewHolder.mReviewAuthor.setText(reviewSelected.getAuthor());
        reviewAdapterViewHolder.mReviewContent.setText(reviewSelected.getContent());
        reviewAdapterViewHolder.mReviewUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = reviewSelected.getUrl();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                reviewAdapterViewHolder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (null == mReviewData) return 0;
        return mReviewData.size();
    }

    public void setReviewData(List<Review> reviewData) {
        mReviewData = reviewData;
        notifyDataSetChanged();
    }
}
