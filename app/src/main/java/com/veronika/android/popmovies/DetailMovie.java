package com.veronika.android.popmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * This class used to define and present details of current movie
 */

public class DetailMovie extends AppCompatActivity {

    private static final String TAG = DetailMovie.class.getSimpleName();
    private TextView mMovieTitle;
    private TextView mReleaseDate;
    private TextView mVoteAverage;
    private TextView mMovieOverview;
    private ImageView mMovieImage;

   private AndroidMovie mCurrentMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mMovieTitle = (TextView) findViewById(R.id.movie_title_detail);
        mReleaseDate = (TextView) findViewById(R.id.tv_release_date);
        mVoteAverage = (TextView) findViewById(R.id.tv_vote_average);
        mMovieOverview = (TextView) findViewById(R.id.tv_detail_overview);
        mMovieImage = (ImageView) findViewById(R.id.movie_image_detail);

        if(savedInstanceState == null || !savedInstanceState.containsKey("moviesDetailsData")){

            Intent intentThatStartThisActivity = getIntent();
            this.mCurrentMovie = intentThatStartThisActivity.getParcelableExtra(Intent.EXTRA_TEXT);

        }
        else {
            //valid data are here
           this.mCurrentMovie = savedInstanceState.getParcelable("moviesDetailsData");

        }
        //Populating views with details of movie data
        if(mCurrentMovie != null){

            mMovieTitle.setText(mCurrentMovie.getTitle());
            mReleaseDate.setText(mCurrentMovie.getReleaseDate());
            mVoteAverage.setText(mCurrentMovie.getVoteAverage().toString()+"/10");
            mMovieOverview.setText(mCurrentMovie.getOverview());
            String picassoPathUrl = NetworkUtils.buildUrlPicasso(mCurrentMovie.getMovieImage());
            //Picasso.with(context).load(url).into(view);
            Picasso.with(mMovieImage.getContext()).load(picassoPathUrl).into(mMovieImage);

        }
        else {
            Log.v(TAG, "No details data are available");
        }
    }

}
