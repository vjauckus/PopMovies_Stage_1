package com.veronika.android.popmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by veronika on 25.09.17.
 * These utiliites will be used to communicate with Internet
 */

public class NetworkUtils {


    /**The format that we want the API to return**/
    //private static String format = "json";

    private final static String BASE_URL_MOVIE_DB_API = "https://api.themoviedb.org/3/movie";


    private final static String API_KEY_PARAM = "api_key";

    private final static String API_KEY_VALUE= "";

    private final static  String BASE_URL_PICASSO = "http://image.tmdb.org/t/p/";

    private final static String POSTER_SIZE ="w185/";

    /**
     * Builds the URL for MovieDB API.
     * @param sortByChoice The keyword for sorting the data
     * @return URL to use to query the Movie API from api.themoviedb.org
     */
    public static URL buildUrlMovieDbApi(String sortByChoice){

        Uri builtUri = Uri.parse(BASE_URL_MOVIE_DB_API).buildUpon()
                .appendEncodedPath(sortByChoice)
                .appendQueryParameter(API_KEY_PARAM, API_KEY_VALUE)
                .build();
        URL url = null;

        try {
            url = new URL(builtUri.toString());
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }
        //https://api.themoviedb.org/3/movie/popular?api_key=
        Log.v(NetworkUtils.class.getSimpleName(), "Building URL for MovieDB API: "+url);
        return url;
    }

    /**
     * Builds the URL for Picasso caching library
     * @param mPosterPath The path of poster got from JSON
     * @return URL for binding an image library
     */
    public static String buildUrlPicasso(String mPosterPath){

        String url = BASE_URL_PICASSO + POSTER_SIZE +"/"+ mPosterPath;
        Log.v(NetworkUtils.class.getSimpleName(), "Building URL for Picasso: "+url);
        return url;
    }

    public static boolean checkConnection(Context context) {

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     * This method returns the results from HTTP response-
     * @param url The URL to fetch the HTTP response from
     * @return The contents of the HTTP response
     * @throws IOException The exception in case of missing response from HttpUrl
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInputData = scanner.hasNext();

            if(hasInputData){
                return scanner.next();
            }
            else{
                return null;
            }

        }
        finally {
            urlConnection.disconnect();
        }
    }


}
