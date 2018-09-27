package com.fouomene.popularmovies.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fouomene.popularmovies.app.R;
import com.fouomene.popularmovies.app.database.MovieEntry;
import com.fouomene.popularmovies.app.utils.Utility;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * This MovieEntryAdapter creates and binds ViewHolders, that hold poster a movieEntry,
 * to a RecyclerView to efficiently display data.
 */
public class MovieEntryAdapter extends RecyclerView.Adapter<MovieEntryAdapter.MovieViewHolder> {


    // Member variable to handle item clicks
    final private ItemClickListener mItemClickListener;
    // Class variables for the List that holds movie data and the Context
    private List<MovieEntry> mMovieEntries;
    private Context mContext;

    /**
     * Constructor for the MovieEntryAdapter that initializes the Context.
     *
     * @param context  the current Context
     * @param listener the ItemClickListener
     */
    public MovieEntryAdapter(Context context, ItemClickListener listener) {
        mContext = context;
        mItemClickListener = listener;
    }

    /**
     * Called when ViewHolders are created to fill a RecyclerView.
     *
     * @return A new MovieViewHolder that holds the view for each task
     */
    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the movie_item to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.favorite_item, parent, false);

        return new MovieViewHolder(view);
    }

    /**
     * Called by the RecyclerView to display data at a specified position in the Cursor.
     *
     * @param holder   The ViewHolder to bind Cursor data to
     * @param position The position of the data in the Cursor
     */
    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        // Determine the values of the wanted data
        MovieEntry movieEntry = mMovieEntries.get(position);

        //Set values
        Picasso.with(mContext)
                .load(Utility.getFinalUrl("w185",movieEntry.getPoster_path()))
                .placeholder(R.drawable.noposter)
                .error(R.drawable.noposter)
                .into(holder.posterView);
        holder.titleView.setText(movieEntry.getTitle());
    }

    /**
     * Returns the number of items to display.
     */
    @Override
    public int getItemCount() {
        if (mMovieEntries == null) {
            return 0;
        }
        return mMovieEntries.size();
    }

    /**
     * When data changes, this method updates the list of movieEntries
     * and notifies the adapter to use the new values on it
     */
    public void setMovieEntries(List<MovieEntry> movieEntries) {
        mMovieEntries = movieEntries;
        notifyDataSetChanged();
    }

    public List<MovieEntry> getMovieEntries() {
        return mMovieEntries;
    }

    public interface ItemClickListener {
        void onItemClickListener(MovieEntry itemMovieEntry);
    }

    // Inner class for creating ViewHolders
    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView posterView;
        TextView titleView;

        /**
         * Constructor for the MovieViewHolders.
         *
         * @param itemView The view inflated in onCreateViewHolder
         */
        public MovieViewHolder(View itemView) {
            super(itemView);

            posterView = itemView.findViewById(R.id.movie_poster_favorite);
            titleView = itemView.findViewById(R.id.title_favorite);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            MovieEntry elementMovieEntry = mMovieEntries.get(getAdapterPosition());
            mItemClickListener.onItemClickListener(elementMovieEntry);
        }
    }
}