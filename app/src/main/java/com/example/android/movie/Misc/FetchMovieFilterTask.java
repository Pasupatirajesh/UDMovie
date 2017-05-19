package com.example.android.movie.Misc;

import android.content.Context;
import android.os.AsyncTask;

import com.example.android.movie.Movie.Movie;
import com.example.android.movie.NetworkUtils.Network;

import java.util.ArrayList;

/**
 * Created by SSubra27 on 5/17/17.
 */

public class FetchMovieFilterTask extends AsyncTask<Void ,Void, ArrayList<Movie>>
{
    private Context mContext;
    private AsynctaskCompleteListener<ArrayList<Movie>> mArrayListAsynctaskCompleteListener;

    public FetchMovieFilterTask(Context ct, AsynctaskCompleteListener<ArrayList<Movie>> listener)
    {
        this.mContext = ct;
        this.mArrayListAsynctaskCompleteListener = listener;
    }


    @Override
    protected ArrayList<Movie> doInBackground(Void... params) {
        try
        {

            return new Network().fetchItems(false);
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> s) {
        super.onPostExecute(s);
        mArrayListAsynctaskCompleteListener.onTaskComplete(s);

    }
}
