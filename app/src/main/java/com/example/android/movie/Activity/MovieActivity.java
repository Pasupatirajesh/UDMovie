package com.example.android.movie.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.movie.Adapter.MovieAdapter;
import com.example.android.movie.Misc.AsynctaskCompleteListener;
import com.example.android.movie.Misc.FetchMovieTask;
import com.example.android.movie.Movie.Movie;
import com.example.android.movie.NetworkUtils.Network;
import com.example.android.movie.R;
import com.facebook.stetho.Stetho;

import org.parceler.Parcels;

import java.util.ArrayList;

//Referred to guides.codepath.com for checking network connectivity

public class MovieActivity extends AppCompatActivity implements MovieAdapter.onItemClickInterface{

    private static final String FILTER = "filter";

    private RecyclerView mMovieRecyclerView;
    private RecyclerView.LayoutManager mGridLayoutManager;
    private MovieAdapter mMovieAdapter;

    public static Parcelable wrapper;

    private ArrayList<Movie> mMovieArrayList;
    private TextView mNonetworkTextView;

    private boolean sort;
    Toast mToast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Stetho.initializeWithDefaults(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        mMovieArrayList = new ArrayList<>();
        mMovieRecyclerView = (RecyclerView) findViewById(R.id.rv_movie_list_view);
        mGridLayoutManager = new GridLayoutManager(MovieActivity.this, 2);
        mMovieRecyclerView.setLayoutManager(mGridLayoutManager);

        if(savedInstanceState!=null) {

            sort = savedInstanceState.getBoolean(FILTER);
            mMovieArrayList = Parcels.unwrap(savedInstanceState.getParcelable("PopularData"));
            mGridLayoutManager.onRestoreInstanceState(savedInstanceState.getParcelable("scrollPosition"));
            mMovieAdapter = new MovieAdapter(getApplicationContext(), mMovieArrayList, this);
            mMovieRecyclerView.setAdapter(mMovieAdapter);
            mMovieRecyclerView.setHasFixedSize(true);
        } else
        {
            new FetchMovieTask(this, new AsynctaskCompleteListener<ArrayList<Movie>>() {
                @Override
                public void onTaskComplete(ArrayList<Movie> result) {
                    mMovieArrayList = result;
                    mMovieAdapter = new MovieAdapter(getApplicationContext(), mMovieArrayList, MovieActivity.this);
                    mMovieRecyclerView.setAdapter(mMovieAdapter);
                    mMovieRecyclerView.setHasFixedSize(true);
                }
            }).execute();
        }


        if(!isOnline())
        {
            setContentView(R.layout.activity_main_no_network);
            mNonetworkTextView = (TextView) findViewById(R.id.tv_offline_text_view);
            mNonetworkTextView.setText(R.string.message_for_no_network);
        }
    }

    @Override
    public void onItemClicked(int clickedListItem) {
        if(mToast!=null)
        {
            mToast.cancel();
        }
        mToast = Toast.makeText(this, " "+ mMovieArrayList.get(clickedListItem).mMovieName+ "clicked", Toast.LENGTH_SHORT);
        mToast.show();

        Context context = this;
        Class movieDetailClass = MovieDetailActivity.class;
        Intent myIntent = new Intent(context,movieDetailClass);

        Movie movie = mMovieArrayList.get(clickedListItem);
        wrapper = Parcels.wrap(movie);
        myIntent.putExtra("MovieDataArrayList", wrapper);

        Log.i("MovieParcelActivity", "onItemClicked: "+ wrapper);
        startActivity(myIntent);
    }

    public class FetchFilterMovieTask extends AsyncTask<Void ,Void, ArrayList<Movie>>
    {

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
            mMovieArrayList = s;
            mMovieAdapter.setMovieData(mMovieArrayList);
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        outState.putBoolean(FILTER, sort);
        outState.putParcelable("PopularData",Parcels.wrap(mMovieArrayList));
        outState.putParcelable("scrollPosition", mGridLayoutManager.onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie, menu);
        MenuItem filter = menu.findItem(R.id.menu_filter);
        // Creating and updating menu items helps in recreating menu items across rotation
        if(sort)
        {
            filter.setTitle(R.string.popular_filter);
        } else
        {
            filter.setTitle(R.string.top_rated_filter);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_filter:
                new FetchMovieTask(this, new AsynctaskCompleteListener<ArrayList<Movie>>() {
                    @Override
                    public void onTaskComplete(ArrayList<Movie> result) {
                        mMovieArrayList = result;
                        mMovieAdapter = new MovieAdapter(getApplicationContext(), mMovieArrayList, MovieActivity.this);
                        mMovieRecyclerView.setAdapter(mMovieAdapter);
                        mMovieRecyclerView.setHasFixedSize(true);
                    }
                    }).execute();
                    sort = !sort;
                    this.invalidateOptionsMenu();
                    upDateUI();
                    return true;
            case R.id.menu_favorites:
                Intent intent = new Intent(this, FavoriteMovieDisplayActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void upDateUI() {
        if(sort)
        new FetchFilterMovieTask().execute();
    }

    private boolean isOnline()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo !=null && netInfo.isConnectedOrConnecting();
    }


}
