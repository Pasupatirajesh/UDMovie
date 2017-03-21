package com.example.android.movie.Movie;

import java.io.Serializable;

/**
 * Created by SSubra27 on 3/20/17.
 */

public class Movie implements Serializable {

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


}
