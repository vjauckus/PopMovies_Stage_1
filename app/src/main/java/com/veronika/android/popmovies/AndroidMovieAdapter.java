package com.veronika.android.popmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * This class defines Adapter for handling with Movie objects
 */

public class AndroidMovieAdapter extends RecyclerView.Adapter<AndroidMovieAdapter.MovieAdapterViewHolder> {

    private static final String TAG = AndroidMovieAdapter.class.getSimpleName();
    private List<AndroidMovie> mMovieDataList;

    /**
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
    private final MovieAdapterOnClickHandler mClickHandler;


    /**
     * This is our own custom constructor
     * The context is used to inflate the layout file, and the List is the data we want
     * to populate into the lists
     * @param clickHandler The on-click handler makes it easy for an Activity to interface with
     * our RecyclerView and works with Adapter. this single handler is called.
     */

    public AndroidMovieAdapter(MovieAdapterOnClickHandler clickHandler){

        mClickHandler = clickHandler;
    }

    /**
     * Cache of the children views for a movie list item.
     */
    class MovieAdapterViewHolder extends RecyclerView.ViewHolder{

        public final ImageView mMovieImage;

        /**
         * Constructor
         * @param view The view that was clicked
         */
        public MovieAdapterViewHolder(View view){

            super(view);
            mMovieImage = (ImageView) view.findViewById(R.id.movie_poster);

            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickHandler.onClick(mMovieDataList.get(getAdapterPosition()));
                }
            });
        }

    }

    /**This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param parent The ViewGroup that these ViewHolders are contained within.
     * @param viewType If our RecyclerView has more than one type of items(which ours does not), we can use this integer to provide a different layout
     * @return A new MovieAdapterViewHolder that holds the View for each list item
     */
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent , int viewType){

        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_item;
        LayoutInflater inflator = LayoutInflater.from(context);

        View view = inflator.inflate(layoutIdForListItem, parent, false);
        return new MovieAdapterViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the movie data at the specified position
     * @param movieViewHolder The ViewHolder that should be updated to represent the content of the item at the given position
     * @param position The position of item within adapter's data list
     */
    public void onBindViewHolder(MovieAdapterViewHolder movieViewHolder, int position){

        AndroidMovie currentMovie = mMovieDataList.get(position);
        String posterPath = currentMovie.getMovieImage();
        Log.v(TAG, "Image found: "+ posterPath);
        String picassoPathUrl = NetworkUtils.buildUrlPicasso(posterPath);
        //Picasso.with(context).load(url).into(view);
        Picasso.with(movieViewHolder.mMovieImage.getContext()).load(picassoPathUrl).into(movieViewHolder.mMovieImage);

    }

    public int getItemCount(){
        if(mMovieDataList == null){
            return 0;
        }
        else{
            return mMovieDataList.size();
        }

    }
    public void setMovieList(ArrayList<AndroidMovie> movieList){
        mMovieDataList = movieList;
        notifyDataSetChanged();
    }

    /**
     * That interface receives onClick AndroidMovie object
     */
    public interface MovieAdapterOnClickHandler{

        void onClick(AndroidMovie currentMovie);
    }



}
