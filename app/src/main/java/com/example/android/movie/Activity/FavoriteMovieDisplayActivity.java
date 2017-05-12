package com.example.android.movie.Activity;

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

import com.example.android.movie.Databases.FavoriteMovieContract;
import com.example.android.movie.Fragments.FavoriteMovieViewPagerFragment;
import com.example.android.movie.Movie.Movie;
import com.example.android.movie.R;

import org.parceler.Parcels;

import java.util.ArrayList;

public class FavoriteMovieDisplayActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public PagerAdapter mFragmentPagerAdapter;
    private ViewPager mViewPager;

    private ArrayList<Movie> mFavMovies = new ArrayList<>();
    private Movie movie;

    String[] FAVORITE_MOVIE_PROJECTION = new String[]{FavoriteMovieContract.FavoriteMovieEntry.MOVIE_NAME, FavoriteMovieContract.FavoriteMovieEntry.MOVIE_REVIEW, FavoriteMovieContract.FavoriteMovieEntry.MOVIE_POSTER_PATH};

    public static final int FAVORITE_MOVIE_LOADER =44;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_movie_display);

        getSupportLoaderManager().initLoader(FAVORITE_MOVIE_LOADER,null,this);

        mFragmentPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.vp_fav_movie_pager);

        mViewPager.setAdapter(mFragmentPagerAdapter);
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
        data.moveToFirst();
        while(!data.isAfterLast())
        {
            for (int i=0; i<data.getCount(); i++)
            {
                movie = new Movie();
                String movieTitle=data.getString(data.getColumnIndex(FavoriteMovieContract.FavoriteMovieEntry.MOVIE_NAME));
                String movieReview = data.getString(data.getColumnIndex(FavoriteMovieContract.FavoriteMovieEntry.MOVIE_REVIEW));
                String moviePosterPath = data.getString(data.getColumnIndex(FavoriteMovieContract.FavoriteMovieEntry.MOVIE_POSTER_PATH));
                movie.setMovieInfo(movieReview);
                movie.setMoviePosterPath(moviePosterPath);
                movie.setMovieName(movieTitle);
                Log.i("movieTitile", movieTitle);
                mFavMovies.add(movie);

                data.moveToNext();
            }
        }
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
            Fragment frag = new FavoriteMovieViewPagerFragment();
            Bundle args = new Bundle();
            args.putParcelable("Movie", Parcels.wrap(movie));
            Log.i("parcels",""+ args);
            return frag;
        }

        @Override
        public int getCount() {

            return 10;
        }
    }
}



