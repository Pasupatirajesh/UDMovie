package com.example.android.movie.Movie;

import org.parceler.Parcel;

/**
 * Created by SSubra27 on 3/20/17.
 */
@Parcel
public class Movie {

    public String mMovieName;

    public Integer mMovieId;

    public String mMoviePosterPath;

    public float mMovieAvgScore;

    public String mMovieReleaseDate;

    public String getMovieInfo() {
        return mMovieInfo;
    }

    public void setMovieInfo(String movieInfo) {
        mMovieInfo = movieInfo;
    }

    public String mMovieInfo;

    public String mUserReview;

    public String getTrailerKey() {
        return mTrailerKey;
    }

    public void setTrailerKey(String trailerKey) {
        mTrailerKey = trailerKey;
    }

    public String mTrailerKey;

    public Movie()
    {
        super();
    }

    public String getMovieName() {
        return mMovieName;
    }

    public void setMovieName(String movieName) {
        mMovieName = movieName;
    }

    public Integer getMovieId() {
        return mMovieId;
    }

    public void setMovieId(Integer movieId) {
        mMovieId = movieId;
    }

    public String getMoviePosterPath() {
        return mMoviePosterPath;
    }

    public void setMoviePosterPath(String moviePosterPath) {
        mMoviePosterPath = moviePosterPath;
    }

    public float getMovieAvgScore() {
        return mMovieAvgScore;
    }

    public void setMovieAvgScore(float movieAvgScore) {
        mMovieAvgScore = movieAvgScore;
    }

    public String getMovieReleaseDate() {
        return mMovieReleaseDate;
    }

    public void setMovieReleaseDate(String movieReleaseDate) {
        mMovieReleaseDate = movieReleaseDate;
    }


    public String getUserReview() {
        return mUserReview;
    }

    public void setUserReview(String userReview) {
        mUserReview = userReview;
    }
}
