package com.example.android.movie.Databases;

import android.provider.BaseColumns;

/**
 * Created by SSubra27 on 5/2/17.
 */

public class FavoriteMovieContract {

    private FavoriteMovieContract()
    {

    }

    public static class FavoriteMovieEntry implements BaseColumns {

        public static final String TABLE_NAME = "favoriteMovie";
        public static final String MOVIE_NAME = "movieName";
        public static final String RELEASE_DATE = "releaseDate";
        public static final String MOVIE_REVIEW = "moviewReview";

    }
}
