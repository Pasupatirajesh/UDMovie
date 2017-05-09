package com.example.android.movie.Databases;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by SSubra27 on 5/2/17.
 */

public class FavoriteMovieContract {

    // <scheme> :// <Content authority> /<path> ( scheme + Content authority + Base content + Path)
    // Base content URI = content_scheme + authority (A unique reference to the provider)
    //
    //

    public static final String AUTHORITY = "com.example.android.movie";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+AUTHORITY);

    public static final String PATH_TASKS = "favoriteMovies";



    private FavoriteMovieContract()
    {

    }

    public static class FavoriteMovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASKS).build();

        public static final String TABLE_NAME = "favoriteMovie";
        public static final String MOVIE_ID = "movieId";
        public static final String MOVIE_NAME = "movieName";
        public static final String RELEASE_DATE = "releaseDate";
        public static final String MOVIE_REVIEW = "moviewReview";

    }
}
