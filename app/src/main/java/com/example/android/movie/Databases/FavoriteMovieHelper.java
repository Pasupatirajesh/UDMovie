package com.example.android.movie.Databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.android.movie.Databases.FavoriteMovieContract.*;

/**
 * Created by SSubra27 on 5/2/17.
 */

public class FavoriteMovieHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME ="favortieMovieHelper.db";

    private static final int DATABASE_VERSION = 1;

    public FavoriteMovieHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }




    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAVORITE_MOVIE_TABLE = "CREATE TABLE "+
                FavoriteMovieEntry.TABLE_NAME + " ("+
                FavoriteMovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FavoriteMovieEntry.MOVIE_NAME+ " TEXT NOT NULL," +
                FavoriteMovieEntry.RELEASE_DATE+ " TEXT NOT NULL," +
                FavoriteMovieEntry.MOVIE_REVIEW+ " TEXT NOT NULL" + ");";

        db.execSQL(SQL_CREATE_FAVORITE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE, IF EXISTS " + FavoriteMovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
