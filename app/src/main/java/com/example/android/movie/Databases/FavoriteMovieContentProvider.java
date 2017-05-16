package com.example.android.movie.Databases;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.example.android.movie.Databases.FavoriteMovieContract.AUTHORITY;
import static com.example.android.movie.Databases.FavoriteMovieContract.FavoriteMovieEntry;
import static com.example.android.movie.Databases.FavoriteMovieContract.FavoriteMovieEntry.MOVIE_ID;
import static com.example.android.movie.Databases.FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME;
import static com.example.android.movie.Databases.FavoriteMovieContract.PATH_TASKS;

/**
 * Created by SSubra27 on 5/8/17.
 */

//Implemented based on coursework completed on Udacity.com
public class FavoriteMovieContentProvider extends ContentProvider {

    private FavoriteMovieHelper mFavoriteMovieHelper; // member variable for he SQLite database that the content provider needs to work with

    public static final int FAVORITEMOVIES = 100;

    public static final int FAVORITEMOVIES_WITH_ID = 101;

    public static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher()
    {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(AUTHORITY, PATH_TASKS, FAVORITEMOVIES);

        uriMatcher.addURI(AUTHORITY, PATH_TASKS + "/#", FAVORITEMOVIES_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mFavoriteMovieHelper = new FavoriteMovieHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase mdb = mFavoriteMovieHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);

        Cursor retCursor;

        switch (match)
        {
            case FAVORITEMOVIES: {
                retCursor = mdb.query(FavoriteMovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: "+uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mFavoriteMovieHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        Uri returnUri ;
        switch (match)
        {
            case FAVORITEMOVIES:

                long id  = db.insert(TABLE_NAME, null, values);
                if(id >0)
                {
                    // Success
                    returnUri = ContentUris.withAppendedId(FavoriteMovieEntry.CONTENT_URI,id);

                } else
                {
                    throw new android.database.SQLException("Failed to insert row into "+ uri);
                }


                break;
            default:

                throw new UnsupportedOperationException("Unknown Uri: "+ uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase mDb = mFavoriteMovieHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        int numberOfMovieDeleted;

        String movieIdToDelete = uri.getPathSegments().get(1);

        Log.i("movieToDelete", movieIdToDelete);

        switch(match)
        {
            case FAVORITEMOVIES_WITH_ID:

                numberOfMovieDeleted = mDb.delete(TABLE_NAME, MOVIE_ID+ "=?", new String[]{movieIdToDelete});
                numberOfMovieDeleted++;

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: "+uri);
        }
        if(numberOfMovieDeleted !=0)
        {
            getContext().getContentResolver().notifyChange(uri,null);
        }

        Log.i("movieDeleted", numberOfMovieDeleted+"");
        return numberOfMovieDeleted;


    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
