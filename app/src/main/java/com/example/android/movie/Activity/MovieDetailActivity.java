package com.example.android.movie.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.movie.Databases.FavoriteMovieHelper;
import com.example.android.movie.Movie.Movie;
import com.example.android.movie.NetworkUtils.Network;
import com.example.android.movie.R;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import static com.example.android.movie.Databases.FavoriteMovieContract.FavoriteMovieEntry;

public class MovieDetailActivity extends AppCompatActivity {

    private TextView mTitleTextView;
    private ImageView mPosterImageView;
    private TextView mReleaseDateTextView;
    private TextView mAverageScoreTextView;
    private TextView mSynopsisTextView;
    private Button mFavButton;

    private Button mTrailerButton;

    private Movie movie;

    private SQLiteDatabase mDb;

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
        mFavButton = (Button)findViewById(R.id.add_to_fav_button);



        Bundle myIntent = getIntent().getExtras();

        if(myIntent !=null)
        {
            if(myIntent.getParcelable("MovieDataArrayList")!=null)
            {


               movie = Parcels.unwrap(myIntent.getParcelable("MovieDataArrayList"));

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

        mFavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final FavoriteMovieHelper mMovieHelper = new FavoriteMovieHelper(MovieDetailActivity.this);

                mDb = mMovieHelper.getWritableDatabase();

                ContentValues cv = new ContentValues();

                cv.put(FavoriteMovieEntry.MOVIE_NAME, movie.getMovieName());
                cv.put(FavoriteMovieEntry.RELEASE_DATE, movie.getMovieReleaseDate());
                cv.put(FavoriteMovieEntry.MOVIE_REVIEW, movie.getMovieInfo());


                Cursor mCursor = getMovieNames(movie.getMovieName());

                if(mCursor.equals(movie.getMovieName()))
                {
                    Toast.makeText(MovieDetailActivity.this,"This movie has already been favorited", Toast.LENGTH_SHORT).show();
                }
                mDb.insert(FavoriteMovieEntry.TABLE_NAME,null,cv);

           
            }
        });
    }


    private Cursor getMovieNames(String movieName)
    {
        return mDb.query(FavoriteMovieEntry.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        FavoriteMovieEntry.MOVIE_NAME);

    }
}











