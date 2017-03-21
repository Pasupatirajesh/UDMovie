package com.example.android.movie;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.android.movie.Adapter.MovieAdapter;
import com.example.android.movie.Movie.Movie;
import com.example.android.movie.NetworkUtils.Network;

import java.util.ArrayList;

public class MovieActivity extends AppCompatActivity {

    private RecyclerView mMovieRecyclerView;
    private RecyclerView.LayoutManager mGridLayoutManager;
    private MovieAdapter mMovieAdapter;

    private ArrayList<Movie> mMovieArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mMovieArrayList = new ArrayList<>();
        mMovieRecyclerView=(RecyclerView) findViewById(R.id.rv_movie_list_view);
        mGridLayoutManager = new LinearLayoutManager(MovieActivity.this);
        mMovieRecyclerView.setLayoutManager(mGridLayoutManager);
        mMovieRecyclerView.setHasFixedSize(true);
        mMovieRecyclerView.setAdapter(mMovieAdapter);

        new FetchMovieTask().execute();
    }
    public class FetchMovieTask extends AsyncTask<Void ,Void, ArrayList<Movie>>
    {


        @Override
        protected ArrayList<Movie> doInBackground(Void... params) {
            try
            {
               return new Network().fetchItems();
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
            mMovieAdapter = new MovieAdapter(getApplicationContext(), mMovieArrayList);
            mMovieAdapter.notifyDataSetChanged();


        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
