package com.veronika.android.popmovies;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AndroidMovieAdapter.MovieAdapterOnClickHandler{

    private final String TAG = MainActivity.class.getSimpleName();
    private final String sortByPopularity = "popular";
    private final String sortByRate="top_rated";

    private GridLayoutManager mGridLayoutManager;

    private AndroidMovieAdapter movieAdapter;

    private ArrayList<AndroidMovie> movieList;

    private String initialSort = sortByPopularity;
    private String prefferedUserSort = "";

    private int intOrientation;

    private RecyclerView mRecyclerView;
    private TextView mErrorMessageDisplay;
    private TextView mErrorTitleDisplay;
    private ProgressBar mLoadingIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if(savedInstanceState == null || !savedInstanceState.containsKey("movies")){
            movieList = new ArrayList<>();
        }
        else {
            movieList = savedInstanceState.getParcelableArrayList("movies");
        }
        setContentView(R.layout.activity_main);
        //Initial sort setting
        initialSort = sortByPopularity;
       // movieList = null;

        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view_movie);

        if(!NetworkUtils.checkConnection(this)){
            //if Network connection is NOT available
           showErrorMessage();
        }
        //If Network connection is checked and OK

        //If orientation portrait, then is intOrientation = 2, else 3
        intOrientation = checkDeviceOrientation(this);
      //  intOrientationColumn = Utility.calculateNoOfColumns(getApplicationContext());

        Log.v(TAG, "Calculated Nr of colums: " + intOrientation);

        mGridLayoutManager  = new GridLayoutManager(this, intOrientation);

        mGridLayoutManager.scrollToPositionWithOffset(0,0);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        mRecyclerView.setHasFixedSize(true);

        //load and get data
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        //Initial sort setting is sort by popularity
        if(UserPreferences.getUserPreferredSort(this).equals("")){
            loadMoviesData(initialSort);
        }
        else {
            loadMoviesData(UserPreferences.getUserPreferredSort(this));
        }


        movieAdapter = new AndroidMovieAdapter(this);
        movieAdapter.setMovieList(movieList);

        mRecyclerView.setAdapter(movieAdapter);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movies", movieList);
        super.onSaveInstanceState(outState);
    }

    private  void showMoviePoster(){

        Log.i(TAG, "I am trying to get response from HttpUrl");

        mRecyclerView.setVisibility(View.VISIBLE);

      //  Toast.makeText(this, "Loading of data takes a moment. PLease wait...", Toast.LENGTH_SHORT).show();

    }
    private  void showErrorMessage(){

        Log.i(TAG, "I am trying to get response from HttpUrl");

        mRecyclerView.setVisibility(View.INVISIBLE);

        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mErrorTitleDisplay = (TextView) findViewById(R.id.tv_error_title_display);
        mErrorTitleDisplay.setVisibility(View.VISIBLE);

        mErrorMessageDisplay.setVisibility(View.VISIBLE);

        Toast.makeText(this, "Loading of data does not work proper. PLease wait...", Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if(id == R.id.sort_by_popularity || id == R.id.action_refresh){

            prefferedUserSort = sortByPopularity;
            UserPreferences.setUserPrefferedSort(prefferedUserSort,this);

            loadMoviesData(prefferedUserSort);

            Toast.makeText(this, "Most popular movie", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if(id == R.id.sort_by_top_rated){

            prefferedUserSort = sortByRate;
            UserPreferences.setUserPrefferedSort(prefferedUserSort,this);

            loadMoviesData(prefferedUserSort);

            Toast.makeText(this, "Top rated movie", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private int checkDeviceOrientation(Context context) {
        intOrientation = context.getResources().getConfiguration().orientation;

        if(intOrientation == Configuration.ORIENTATION_PORTRAIT){
            Log.v(TAG, "*******Orientation is portrait");
            return 2;
        }
        else{
            Log.v(TAG, "*******Orientation is landshape");
             return 3;
        }
    }

    /**
     * This method will get the user's preferred sortBy setting for movies
     * and then tell some background method to get the movies data in the background
     * @param sortBy The initial sort or User's preferred sort by setting
     */
    private void loadMoviesData(String sortBy){

        //Must be changed, if user wants to change sort by category;
        Log.i(TAG, "I am trying to load movies data from API");

        try {
           // FetchMovieDataList fetchedData = new FetchMovieDataList(sortBy);
            new FetchMovieDataTask().execute(sortBy);

           // ArrayList<AndroidMovie> movieListLoaded =fetchedData.getMovieDataList();
            //Log.v(TAG, "RESULTS got in MainActivity delivered from FetchMovieDataList: " + Arrays.asList(movieListLoaded).toString());
            showMoviePoster();
        }
        catch (Exception e){

           // Toast.makeText(this, "Loading of data does not work proper. PLease try again", Toast.LENGTH_SHORT).show();
            showErrorMessage();
            e.printStackTrace();
        }

    }


    /**
     * AsyncTask enables proper and easy use of the UI thread.
     * This class allows us to perform background operations and publish results on the UI thread without having to manipulate threads and/or handlers.
     *<String, Void, ArrayList<AndroidMovie>>
     * 1. String: Params, the type of the parameters sent to the task upon execution.
     * 2. Void: Progress, the type of the progress units published during the background computation.
     * 3. ArrayList<AndroidMovie>: Result, the type of the result of the background computation.

     */

    private class FetchMovieDataTask extends AsyncTask<String, Void, ArrayList<AndroidMovie>> {

        @Override
        protected void onPreExecute() {
            mLoadingIndicator.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected ArrayList<AndroidMovie> doInBackground(String... params) {
            // if there is no data, then it is nothing to do
            if (params.length == 0 ){
                return null;
            }
            String preferredSortCategorie = params[0];

            String movieSearchResults;
            ArrayList<AndroidMovie> movieJSONList;

            URL movieRequestUrl = NetworkUtils.buildUrlMovieDbApi(preferredSortCategorie);

            try {

                Log.i(TAG, "I am trying to get response from HttpUrl");
                movieSearchResults = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
                Log.v(TAG, "MovieSearchResults: "+movieSearchResults);
                //Give results data to JSON
                movieJSONList= MovieJsonUtils.getMovieStringsFromJson(movieSearchResults);
                return   movieJSONList;
            }
            catch (Exception e){

                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(ArrayList<AndroidMovie> movieJSONList) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);

            if(movieJSONList != null){
                //if we have valid results

                movieList = movieJSONList;
                Log.v(TAG, "RESULTS: " + movieJSONList);
                //setMovieList(movieList);

                movieAdapter.setMovieList(movieJSONList);

            }
            else {
                //showErrorMessage();
                Log.v(TAG, "Didn't get data from JSONUtils");

            }

        }
    }

    @Override
    public void onClick(AndroidMovie currentMovie) {

        Context context = this;
      //  Toast.makeText(context, currentMovie.getTitle() + ". Image was clicked: "+currentMovie.getMovieImage(), Toast.LENGTH_SHORT).show();
        //Start DetailMovie activity on Click

        Class destinationClass = DetailMovie.class;
        Intent intentStartDetailMovie = new Intent(context, destinationClass);
       // Toast.makeText(this.getApplication(), "Open movie details", Toast.LENGTH_SHORT).show();
        intentStartDetailMovie.putExtra(Intent.EXTRA_TEXT, currentMovie);
        startActivity(intentStartDetailMovie);

    }
}
