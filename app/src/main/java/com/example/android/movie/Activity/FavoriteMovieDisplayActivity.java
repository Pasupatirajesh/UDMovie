package com.example.android.movie.Activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.movie.Databases.FavoriteMovieContract;
import com.example.android.movie.Fragments.FavoriteMovieViewPagerFragment;
import com.example.android.movie.Movie.Movie;
import com.example.android.movie.R;

import java.util.ArrayList;

// Referred to guides.codepath.com and stackoverflow for implementing a viewpager and FragmentStatePagerAdapter

public class FavoriteMovieDisplayActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public  PagerAdapter mFragmentPagerAdapter;
    private ViewPager mViewPager;

    private ArrayList<Movie> mFavMovies = new ArrayList<>();
    private Movie movie;

    String[] FAVORITE_MOVIE_PROJECTION = new String[]{FavoriteMovieContract.FavoriteMovieEntry.MOVIE_ID,FavoriteMovieContract.FavoriteMovieEntry.MOVIE_NAME, FavoriteMovieContract.FavoriteMovieEntry.MOVIE_REVIEW, FavoriteMovieContract.FavoriteMovieEntry.MOVIE_POSTER_PATH};

    public static final int FAVORITE_MOVIE_LOADER =44;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_movie_display);

        getSupportLoaderManager().initLoader(FAVORITE_MOVIE_LOADER, null, this);

    }
    @Override
    public Loader onCreateLoader(int id, Bundle args) {

        switch (id)
        {
            case  FAVORITE_MOVIE_LOADER:

                Uri favoriteMovieQueryUri = FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI;

                String sortOrder = FavoriteMovieContract.FavoriteMovieEntry.RELEASE_DATE+ " ASC";

                String selection = null;

                return  new CursorLoader(this, favoriteMovieQueryUri, FAVORITE_MOVIE_PROJECTION, selection, null, sortOrder);

            default:
                throw new RuntimeException("Loader not implemented: "+id);
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if(data.getCount()==0)

        return;

        data.moveToFirst();

        int _IDIndex = data.getColumnIndex(FavoriteMovieContract.FavoriteMovieEntry.MOVIE_ID);

        int titleIndex = data.getColumnIndex(FavoriteMovieContract.FavoriteMovieEntry.MOVIE_NAME);

        int reviewIndex = data.getColumnIndex(FavoriteMovieContract.FavoriteMovieEntry.MOVIE_REVIEW);

        int posterIndex = data.getColumnIndex(FavoriteMovieContract.FavoriteMovieEntry.MOVIE_POSTER_PATH);

        while(!data.isAfterLast())
        {
            for (int i=0; i<data.getCount(); i++)
            {
                movie = new Movie();
                String movieId = data.getString(_IDIndex);
                String movieTitle=data.getString(titleIndex);
                String movieReview = data.getString(reviewIndex);
                String moviePosterPath = data.getString(posterIndex);
                movie.setMovieId(Integer.parseInt(movieId));
                movie.setMovieInfo(movieReview);
                movie.setMoviePosterPath(moviePosterPath);
                movie.setMovieName(movieTitle);
                mFavMovies.add(movie);
                data.moveToNext();
            }
        }

        mFragmentPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.vp_fav_movie_pager);

        mViewPager.setAdapter(mFragmentPagerAdapter);
    }
    @Override
    public void onLoaderReset(Loader loader) {

    }

    public class MyPagerAdapter extends FragmentStatePagerAdapter {

        public MyPagerAdapter(FragmentManager fm)
        {
            super(fm);

        }
        @Override
        public Fragment getItem(int position) {
           movie = mFavMovies.get(position);

          return FavoriteMovieViewPagerFragment.newInstance(movie);
        }

        @Override
        public int getCount() {


            return mFavMovies.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.fragment_menu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_delete:

                String movieId = null;

                if(movieId!=null)
                {
                    movieId = ""+ mFavMovies.get(mViewPager.getCurrentItem()).getMovieId();
                    Log.i("movieId", movieId);
                    Uri uri = FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI;

                    uri = uri.buildUpon().appendPath(movieId).build();

                    ContentResolver cr = getContentResolver();

                    cr.delete(uri, null , null);
                } else
                {
                    Toast.makeText(this, "No Favorites yet!!! start adding them", Toast.LENGTH_LONG).show();
                }
                Intent myIntent  = new Intent(this,FavoriteMovieDisplayActivity.class);

                startActivity(myIntent);

                break;

        }
        return super.onOptionsItemSelected(item);
    }
}



