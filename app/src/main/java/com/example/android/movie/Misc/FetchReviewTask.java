package com.example.android.movie.Misc;

import android.content.Context;
import android.os.AsyncTask;

import com.example.android.movie.Movie.Movie;
import com.example.android.movie.NetworkUtils.Network;

import java.util.ArrayList;

/**
 * Created by SSubra27 on 5/19/17.
 */

public class FetchReviewTask extends AsyncTask<Void, Void, ArrayList<Movie>>

{
    private Context mContext;
    private AsynctaskCompleteListener<ArrayList<Movie>> mArrayListAsynctaskCompleteListener;
    private String mMovieId;

    public FetchReviewTask(Context ct, String id, AsynctaskCompleteListener<ArrayList<Movie>> listener)
    {
        this.mContext = ct;
        this.mArrayListAsynctaskCompleteListener = listener;
        this.mMovieId = id;
    }

    @Override
    protected ArrayList<Movie> doInBackground(Void... params) {
        try
        {
            return new Network().fetchTrailerItems(mMovieId,false);
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> movies) {
        super.onPostExecute(movies);
        mArrayListAsynctaskCompleteListener.onTaskComplete(movies);


    }
}

