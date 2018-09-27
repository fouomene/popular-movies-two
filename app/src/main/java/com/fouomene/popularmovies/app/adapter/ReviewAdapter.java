package com.fouomene.popularmovies.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fouomene.popularmovies.app.R;
import com.fouomene.popularmovies.app.model.Review;

import java.util.List;


/**
 * This ReviewAdapter creates and binds ViewHolders, that hold review,
 * to a RecyclerView to efficiently display data.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    // Class variables for the List that holds review data and the Context
    private List<Review> mReviews;
    private Context mContext;

    /**
     * Constructor for the ReviewAdapter that initializes the Context.
     *
     * @param context  the current Context
     */
    public ReviewAdapter(Context context) {
        mContext = context;
    }

    /**
     * Called when ViewHolders are created to fill a RecyclerView.
     *
     * @return A new ReviewViewHolder that holds the view for each task
     */
    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the review_item to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.review_item, parent, false);

        return new ReviewViewHolder(view);
    }

    /**
     * Called by the RecyclerView to display data at a specified position in the Cursor.
     *
     * @param holder   The ViewHolder to bind Cursor data to
     * @param position The position of the data in the Cursor
     */
    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        // Determine the values of the wanted data
        Review review = mReviews.get(position);

        //Set values
        holder.authorView.setText("> "+review.getAuthor());
        holder.contentView.setText(review.getContent());

    }

    /**
     * Returns the number of items to display.
     */
    @Override
    public int getItemCount() {
        if (mReviews == null) {
            return 0;
        }
        return mReviews.size();
    }

    /**
     * When data changes, this method updates the list of reviews
     * and notifies the adapter to use the new values on it
     */
    public void setReviews(List<Review> reviews) {
        mReviews = reviews;
        notifyDataSetChanged();
    }


    // Inner class for creating ViewHolders
    class ReviewViewHolder extends RecyclerView.ViewHolder {

        TextView authorView;
        TextView contentView;
        public View view;

        /**
         * Constructor for the ViewHolders.
         *
         * @param itemView The view inflated in onCreateViewHolder
         */
        public ReviewViewHolder(View itemView) {
            super(itemView);
            authorView = itemView.findViewById(R.id.author_review);
            contentView = itemView.findViewById(R.id.content_review);
            view = itemView;
            view.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                   //ok
                }
            });

        }
    }

}