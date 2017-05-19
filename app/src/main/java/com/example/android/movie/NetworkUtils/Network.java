package com.example.android.movie.NetworkUtils;

import android.net.Uri;
import android.util.Log;

import com.example.android.movie.Movie.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by SSubra27 on 3/20/17.
 */

// Referred to guides.codepath.com and stackoverflow for constructing a network request,
// Converting JSON results into Models


public class Network {
    private static final String MOVIE_API_POPULAR_URL = "http://api.themoviedb.org/3/movie/popular?";
    private static final String MOVIE_API_TOP_RATED_URL = "http://api.themoviedb.org/3/movie/top_rated?";

    private static final String MOVIE_API_TRAILER_URL = "http://api.themoviedb.org/3/movie/";

    private static final String MOVIE_API_USER_REVIEW_URL = "http://api.themoviedb.org/3/movie/";

    private static final String API_KEY = "0c8e96b68c5e24e2ee85490b30b0e383";



    public static URL buildUrl(boolean topRated) {

        Uri queryUrl;

        if(topRated)
        {
            queryUrl = Uri.parse(MOVIE_API_TOP_RATED_URL).buildUpon()
                    .appendQueryParameter("method", "get")
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("nosjsoncallback", "1")
                    .build();

            Log.i("NetworkURL", ""+queryUrl);

        } else
        {
            queryUrl = Uri.parse(MOVIE_API_POPULAR_URL).buildUpon()
                    .appendQueryParameter("method", "get")
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("nosjsoncallback", "1")
                    .build();
        }

        URL url = null;

        try {
            url = new URL(queryUrl.toString());
        } catch (MalformedURLException moe) {
            moe.printStackTrace();
        }

        return url;
    }


    

    public static URL buildTrailerUrl(String movieId, boolean video)
    {
        if(video)
        {
            Uri trailerUri = Uri.parse(MOVIE_API_TRAILER_URL).buildUpon()
                    .appendPath(movieId).appendPath("videos")
                    .appendQueryParameter("method", "get")
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("nojsoncallback", "1")
                    .build();
            URL trailerUrl = null;
            Log.i("TrailerURL", ""+trailerUrl);
            try
            {
                trailerUrl = new URL(trailerUri.toString());
            }catch (MalformedURLException moe)
            {
                moe.printStackTrace();
            }
            return trailerUrl;
        } else
        {
            Uri reviewUri = Uri.parse(MOVIE_API_USER_REVIEW_URL).buildUpon()
                            .appendPath(movieId).appendPath("reviews")
                            .appendQueryParameter("method", "get")
                            .appendQueryParameter("api_key",API_KEY)
                            .appendQueryParameter("format", "json")
                            .appendQueryParameter("nojsoncallback", "1")
                            .build();
            URL reviewUrl = null;

            try
            {
                reviewUrl= new URL(reviewUri.toString());
            } catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            return reviewUrl;
        }

    }




    public static String getResponseFromHttpUrl(URL queryUrl) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) queryUrl.openConnection();
        try {
            InputStream in = connection.getInputStream();

            Scanner sc = new Scanner(in);
            sc.useDelimiter("//A");

            boolean hasInput = sc.hasNext();

            if (hasInput) {
                return sc.next();
            } else {
                return null;
            }
        } finally {
            connection.disconnect();
        }
    }

    public ArrayList<Movie> fetchTrailerItems(String movieId, boolean video) throws JSONException, IOException
    {
        ArrayList<Movie> arrayList = new ArrayList<>();

        ArrayList<Movie> reviewArrayList = new ArrayList<>();
        URL url=null;
        if(video)
        {
            url = buildTrailerUrl(movieId,true);
        } else
        {
            url = buildTrailerUrl(movieId,false);
            Log.i("falseUrl", url+"");
        }

        String responseStringJson = getResponseFromHttpUrl(url);

        Log.i("responseStringJson", responseStringJson);

        JSONObject jsonObject = new JSONObject(responseStringJson);

        if(video)
        {
            parseTrailerItems(arrayList,jsonObject);
            return arrayList;
        } else
        {
            parseReviewItems(reviewArrayList,jsonObject);
            return reviewArrayList;
        }
    }

    private void parseTrailerItems(ArrayList<Movie> arrayList, JSONObject jsonObject) throws JSONException {
        JSONArray jsonArray = jsonObject.getJSONArray("results");
        int i=0;
        {
            JSONObject trailerJsonObject = jsonArray.getJSONObject(i);

            Movie movie = new Movie();

            movie.mTrailerKey = trailerJsonObject.getString("key");

//            movie.mUserReview = trailerJsonObject.getString("content");

            arrayList.add(movie);
        }
    }
    private void parseReviewItems(ArrayList<Movie> reviewArrayList, JSONObject jsonObject) throws JSONException {
        JSONArray jsonArray = jsonObject.getJSONArray("results");

        for(int i=0; i < jsonArray.length(); i++)
        {
            JSONObject trailerJsonObject = jsonArray.getJSONObject(i);

            Movie movie = new Movie();

//            movie.mTrailerKey = trailerJsonObject.getString("key");

            movie.mUserReview = trailerJsonObject.getString("content");

            reviewArrayList.add(movie);
        }
    }

    public ArrayList<Movie> fetchItems(boolean filter) throws JSONException, IOException
    {
        ArrayList<Movie> arrayList = new ArrayList<>();
        URL url = buildUrl(filter);
        String responseStringJson =  getResponseFromHttpUrl(url);
        JSONObject jsonObject = new JSONObject(responseStringJson);
        parseItems(arrayList, jsonObject);

        return arrayList;
    }

    private void parseItems(ArrayList<Movie> movies, JSONObject jsonObject) throws JSONException
    {

        JSONArray jsonArray = jsonObject.getJSONArray("results");

        for(int i=0; i<jsonArray.length(); i++)
        {
            JSONObject movieJsonObject = jsonArray.getJSONObject(i);

            Movie movie = new Movie();

            movie.mMovieName = movieJsonObject.getString("title");
            movie.mMovieId = movieJsonObject.getInt("id");
            movie.mMoviePosterPath = movieJsonObject.getString("poster_path");
            movie.mMovieInfo = movieJsonObject.getString("overview");
            movie.mMovieAvgScore =(float)movieJsonObject.getInt("vote_average");
            movie.mMovieReleaseDate = movieJsonObject.getString("release_date");

            movies.add(movie);
            Log.i("MOVIE", movies.get(i).getMovieInfo()+"");
        }
    }
}

