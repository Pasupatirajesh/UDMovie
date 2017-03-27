package com.example.android.movie.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.movie.Movie.Movie;

import com.example.android.movie.NetworkUtils.Network;

import com.example.android.movie.R;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

public class MovieDetailActivity extends AppCompatActivity {

    private TextView mTitleTextView;
    private ImageView mPosterImageView;
    private TextView mReleaseDateTextView;
    private TextView mAverageScoreTextView;
    private TextView mSynopsisTextView;

    private Button mTrailerButton;

    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mTitleTextView =(TextView) findViewById(R.id.tv_movie_name);
        mPosterImageView = (ImageView) findViewById(R.id.iv_poster_view);
        mReleaseDateTextView=(TextView) findViewById(R.id.tv_release_date);
        mAverageScoreTextView=(TextView)findViewById(R.id.tv_avg_rating);
        mSynopsisTextView =(TextView) findViewById(R.id.tv_synopsis_view);

        mTrailerButton=(Button) findViewById(R.id.bv_trailer);



        Bundle myIntent = getIntent().getExtras();

        if(myIntent !=null)
        {
            if(myIntent.getParcelable("MovieDataArrayList")!=null)
            {


               movie = Parcels.unwrap(myIntent.getParcelable("MovieDataArrayList"));

               Movie movie = Parcels.unwrap(myIntent.getParcelable("MovieDataArrayList"));

                Log.i("MovieDetailActivity", ""+ movie.getMovieName());
                mTitleTextView.setText(movie.getMovieName());
                String imageUri = "https://image.tmdb.org/t/p/w185/"+movie.getMoviePosterPath();
                Picasso.with(getApplicationContext()).load(imageUri).into(mPosterImageView);
                String avgScore =""+movie.getMovieAvgScore();
                mAverageScoreTextView.setText(avgScore);
                mReleaseDateTextView.setText(movie.getMovieReleaseDate());
                mSynopsisTextView.setText(movie.getMovieInfo());

                Log.i("TrailerURL", ""+ Network.buildTrailerUrl(String.valueOf(movie.getMovieId())));
            }
        }

        // Method not complete yet, as I am trying to figure out the right way to pass the network endpoint

        mTrailerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("mTrailerButton", "I was clicked");
                Intent trailerIntent = new Intent(Intent.ACTION_VIEW);
                startActivity(trailerIntent);
            }
        });
    }
}






            }
        }


    }
}