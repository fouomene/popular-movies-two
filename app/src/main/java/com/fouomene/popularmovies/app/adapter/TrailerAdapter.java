package com.fouomene.popularmovies.app.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fouomene.popularmovies.app.R;
import com.fouomene.popularmovies.app.model.Trailer;

import java.util.List;


/**
 * This TrailerAdapter creates and binds ViewHolders, that hold trailer,
 * to a RecyclerView to efficiently display data.
 */
public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    // Class variables for the List that holds trailer data and the Context
    private List<Trailer> mTrailers;
    private Context mContext;

    /**
     * Constructor for the TrailerAdapter that initializes the Context.
     *
     * @param context  the current Context
     */
    public TrailerAdapter(Context context) {
        mContext = context;
    }

    /**
     * Called when ViewHolders are created to fill a RecyclerView.
     *
     * @return A new TrailerViewHolder that holds the view for each task
     */
    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the trailer_item to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.trailer_item, parent, false);

        return new TrailerViewHolder(view);
    }

    /**
     * Called by the RecyclerView to display data at a specified position in the Cursor.
     *
     * @param holder   The ViewHolder to bind Cursor data to
     * @param position The position of the data in the Cursor
     */
    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        // Determine the values of the wanted data
        Trailer trailer = mTrailers.get(position);

        //Set values
        holder.nameView.setText(trailer.getName());

    }

    /**
     * Returns the number of items to display.
     */
    @Override
    public int getItemCount() {
        if (mTrailers == null) {
            return 0;
        }
        return mTrailers.size();
    }

    /**
     * When data changes, this method updates the list of trailers
     * and notifies the adapter to use the new values on it
     */
    public void setTrailers(List<Trailer> trailers) {
        mTrailers = trailers;
        notifyDataSetChanged();
    }


    // Inner class for creating ViewHolders
    class TrailerViewHolder extends RecyclerView.ViewHolder {

        TextView nameView;
        public View view;

        /**
         * Constructor for the TrailerViewHolders.
         *
         * @param itemView The view inflated in onCreateViewHolder
         */
        public TrailerViewHolder(View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.name_trailer);
            view = itemView;
            view.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    Trailer currentTrailer = mTrailers.get(getAdapterPosition());
                    video(mContext,currentTrailer.getKey());
                }
            });

        }

    }

    public static void video(Context context, String id){
        Intent app = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        app.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Intent web = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        web.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(app);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(web);
        }
    }

}