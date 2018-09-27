package com.fouomene.popularmovies.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.fouomene.popularmovies.app.R;
import com.fouomene.popularmovies.app.model.Movie;
import com.fouomene.popularmovies.app.utils.Utility;
import com.squareup.picasso.Picasso;


import java.util.List;


/**
 * This MovieAdapter creates and binds ViewHolders, that hold poster a movie,
 * to a RecyclerView to efficiently display data.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {


    // Member variable to handle item clicks
    final private ItemClickListener mItemClickListener;
    // Class variables for the List that holds movie data and the Context
    private List<Movie> mMovieEntries;
    private Context mContext;

    /**
     * Constructor for the MovieAdapter that initializes the Context.
     *
     * @param context  the current Context
     * @param listener the ItemClickListener
     */
    public MovieAdapter(Context context, ItemClickListener listener) {
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
                .inflate(R.layout.movie_item, parent, false);

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
        Movie movieEntry = mMovieEntries.get(position);

        //Set values
        Picasso.with(mContext)
                .load(Utility.getFinalUrl("w185",movieEntry.getPoster_path()))
                .placeholder(R.drawable.noposter)
                .error(R.drawable.noposter)
                .into(holder.posterView);

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
    public void setMovies(List<Movie> movieEntries) {
        mMovieEntries = movieEntries;
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void onItemClickListener(Movie itemMovie);
    }

    // Inner class for creating ViewHolders
    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView posterView;

        /**
         * Constructor for the MovieViewHolders.
         *
         * @param itemView The view inflated in onCreateViewHolder
         */
        public MovieViewHolder(View itemView) {
            super(itemView);

            posterView = itemView.findViewById(R.id.movie_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Movie elementMovie = mMovieEntries.get(getAdapterPosition());
            mItemClickListener.onItemClickListener(elementMovie);
        }
    }
}