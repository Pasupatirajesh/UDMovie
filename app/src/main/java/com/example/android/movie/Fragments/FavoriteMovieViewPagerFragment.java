package com.example.android.movie.Fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.movie.Databases.FavoriteMovieContract.FavoriteMovieEntry;
import com.example.android.movie.R;

/**
 * Created by SSubra27 on 5/8/17.
 */

public class FavoriteMovieViewPagerFragment extends android.support.v4.app.Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private TextView mTitleTextView;
    private ImageView mPosterImageView;
    private TextView mSysnopsisTextView;

    // Create a String containing our favorite columns from the database to be returned.

    String[] FAVORITE_MOVIE_PROJECTION = new String[]{FavoriteMovieEntry._ID,FavoriteMovieEntry.MOVIE_ID,FavoriteMovieEntry.MOVIE_NAME, FavoriteMovieEntry.RELEASE_DATE,FavoriteMovieEntry.MOVIE_REVIEW};

    public static final int FAVORITE_MOVIE_LOADER =44;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getSupportLoaderManager().initLoader(FAVORITE_MOVIE_LOADER,null,this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflater= LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.favorites_fragment, container, false);

        mTitleTextView =(TextView) v.findViewById(R.id.tv_fav_title_text_view);

        mPosterImageView = (ImageView) v.findViewById(R.id.iv_fav_poster_view);

        mSysnopsisTextView = (TextView) v.findViewById(R.id.tv_fav_synopsis_view);

        return  v;

    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {

        switch (id)
        {
          case  FAVORITE_MOVIE_LOADER: Uri favoriteMovieQueryUri = FavoriteMovieEntry.CONTENT_URI;

            String sortOrder = FavoriteMovieEntry.RELEASE_DATE + "ASC";

            String selection = FavoriteMovieEntry.TABLE_NAME;

            return new CursorLoader(getActivity(), favoriteMovieQueryUri, FAVORITE_MOVIE_PROJECTION, selection, null, sortOrder);
          default:
              throw new RuntimeException("Loader not implemented: "+id);
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();

        Log.i("FragmentCursor", data.getString(data.getColumnIndex(FavoriteMovieEntry.MOVIE_NAME)));
        while(!data.isAfterLast())
        {
            mTitleTextView.setText(data.getString(data.getColumnIndex(FavoriteMovieEntry.MOVIE_NAME)));
            mSysnopsisTextView.setText(data.getString(data.getColumnIndex(FavoriteMovieEntry.MOVIE_REVIEW)));
            data.moveToNext();
        }


    }
    @Override
    public void onLoaderReset(Loader loader) {

    }
}
