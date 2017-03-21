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

public class Network {
    private static final String MOVIE_API_BASE_URL = "https://api.themoviedb.org/3/movie/popular?";
    private static final String API_KEY = "0c8e96b68c5e24e2ee85490b30b0e383";



    public static URL buildUrl() {

        Uri queryUrl = Uri.parse(MOVIE_API_BASE_URL).buildUpon()
                .appendQueryParameter("method", "get")
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("format", "json")
                .appendQueryParameter("nosjsoncallback", "1")
                .build();

        URL url = null;

        try {
            url = new URL(queryUrl.toString());
        } catch (MalformedURLException moe) {
            moe.printStackTrace();
        }

        return url;
    }
    public ArrayList<Movie> fetchItems() throws JSONException, IOException
    {
        ArrayList<Movie> arrayList = new ArrayList<>();
        URL url = buildUrl();
        String responseStringJson =  getResponsefromHttpUrl(url);


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

    public static String getResponsefromHttpUrl(URL queryUrl) throws IOException {
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
}

