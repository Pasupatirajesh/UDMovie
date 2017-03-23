package com.example.android.movie.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.movie.Movie.Movie;
import com.example.android.movie.R;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

public class MovieDetailActivity extends AppCompatActivity {

    private TextView mTitleTextView;
    private ImageView mPosterImageView;
    private TextView mReleaseDateTextView;
    private TextView mAverageScoreTextView;
    private TextView mSynopsisTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mTitleTextView =(TextView) findViewById(R.id.tv_movie_name);
        mPosterImageView = (ImageView) findViewById(R.id.iv_poster_view);
        mReleaseDateTextView=(TextView) findViewById(R.id.tv_release_date);
        mAverageScoreTextView=(TextView)findViewById(R.id.tv_avg_rating);
        mSynopsisTextView =(TextView) findViewById(R.id.tv_synopsis_view);


        Bundle myIntent = getIntent().getExtras();

        if(myIntent !=null)
        {
            if(myIntent.getParcelable("MovieDataArrayList")!=null)
            {

               Movie movie = Parcels.unwrap(myIntent.getParcelable("MovieDataArrayList"));
                Log.i("MovieDetailActivity", ""+ movie.getMovieName());
                mTitleTextView.setText(movie.getMovieName());
                String imageUri = "https://image.tmdb.org/t/p/w185/"+movie.getMoviePosterPath();
                Picasso.with(getApplicationContext()).load(imageUri).into(mPosterImageView);
                String avgScore =""+movie.getMovieAvgScore();
                mAverageScoreTextView.setText(avgScore);
                mReleaseDateTextView.setText(movie.getMovieReleaseDate());
                mSynopsisTextView.setText(movie.getMovieInfo());


            }
        }


    }
}
