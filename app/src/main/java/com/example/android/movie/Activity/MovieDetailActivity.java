package com.example.android.movie.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.movie.Databases.FavoriteMovieHelper;
import com.example.android.movie.Misc.AsynctaskCompleteListener;
import com.example.android.movie.Misc.FetchReviewTask;
import com.example.android.movie.Misc.FetchTrailerTask;
import com.example.android.movie.Movie.Movie;
import com.example.android.movie.R;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;

import static com.example.android.movie.Databases.FavoriteMovieContract.FavoriteMovieEntry;

public class  MovieDetailActivity extends AppCompatActivity {

    private TextView mTitleTextView;
    private ImageView mPosterImageView;
    private TextView mReleaseDateTextView;
    private TextView mAverageScoreTextView;
    private TextView mSynopsisTextView;
    private TextView mUserReviewTextView;
    private Button mFavButton;

    private Button mTrailerButton;

    private Movie movie;

    private SQLiteDatabase mDb;

    private Cursor mCursor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mTitleTextView =(TextView) findViewById(R.id.tv_movie_name);
        mPosterImageView = (ImageView) findViewById(R.id.iv_poster_view);
        mReleaseDateTextView=(TextView) findViewById(R.id.tv_release_date);
        mAverageScoreTextView=(TextView)findViewById(R.id.tv_avg_rating);
        mSynopsisTextView =(TextView) findViewById(R.id.tv_synopsis_view);
        mUserReviewTextView = (TextView)findViewById(R.id.user_reviews);

        mTrailerButton=(Button) findViewById(R.id.bv_trailer);
        mFavButton = (Button)findViewById(R.id.add_to_fav_button);

        Bundle myIntent = getIntent().getExtras();

        if(myIntent !=null) {
            if (myIntent.getParcelable("MovieDataArrayList") != null) {


                movie = Parcels.unwrap(myIntent.getParcelable("MovieDataArrayList"));
                mTitleTextView.setText(movie.getMovieName());
                String imageUri = "https://image.tmdb.org/t/p/w185/" + movie.getMoviePosterPath();
                Picasso.with(getApplicationContext()).load(imageUri).into(mPosterImageView);
                String avgScore = "" + movie.getMovieAvgScore();
                mAverageScoreTextView.setText(avgScore);
                mReleaseDateTextView.setText(movie.getMovieReleaseDate());
                mSynopsisTextView.setText(movie.getMovieInfo());
            }

            FavoriteMovieHelper favoriteMovieHelper = new FavoriteMovieHelper(this);

            mDb = favoriteMovieHelper.getReadableDatabase();

            mCursor = getMovieNames(movie.getMovieId());

            if(mCursor.getCount() !=0)
            {
                mCursor.moveToFirst();

                Log.i("cursorIndex", mCursor.getString(mCursor.getColumnIndex(FavoriteMovieEntry.MOVIE_ID)));

                String movieId = mCursor.getString(mCursor.getColumnIndex(FavoriteMovieEntry.MOVIE_ID));

                if (movieId.equals("" + movie.getMovieId())) {
                    Toast.makeText(MovieDetailActivity.this, "This button was already clicked", Toast.LENGTH_SHORT).show();
                    mFavButton.setEnabled(false);
                }
            } else
            {
                Log.i("Zero Cursor", "Zero items in the cursor");
            }


        }
        mTrailerButton.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  Log.i("mTrailerButton", "I was clicked");
                                                  new FetchTrailerTask(MovieDetailActivity.this, String.valueOf(movie.getMovieId()), new AsynctaskCompleteListener<ArrayList<Movie>>() {
                                                      @Override
                                                      public void onTaskComplete(ArrayList<Movie> result) {
                                                          for (int j = 0; j < result.size(); j++) {

                                                              String trailerKey = result.get(j).getTrailerKey();
                                                              Log.i("trailerKey", "" + trailerKey);
                                                              PackageManager pm = getPackageManager();

                                                              Intent trailerIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtube.com/watch?v=" + trailerKey));

                                                              if (trailerIntent.resolveActivity(pm) != null) {
                                                                  startActivity(trailerIntent);
                                                              } else {
                                                                  Toast.makeText(MovieDetailActivity.this, "Suitable App not available", Toast.LENGTH_SHORT).show();
                                                              }
                                                          }
                                                      }

                                                  }).execute();
                                              }
                                          });


        mFavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClickAddMovie(v);
            }
        });

        new FetchReviewTask(this, String.valueOf(movie.getMovieId()), new AsynctaskCompleteListener<ArrayList<Movie>>() {
            @Override
            public void onTaskComplete(ArrayList<Movie> result) {
                if(result.size()==0)
                {
                  mUserReviewTextView.setText("User Review not available");
                } else
                {
                    String userReview =result.get(0).getUserReview();
                    mUserReviewTextView.setText(userReview);
                }
            }
        }).execute();
    }
    private Cursor getMovieNames(long movieId)
    {
        String table = FavoriteMovieEntry.TABLE_NAME;

        String[] columns = new String[] {FavoriteMovieEntry.MOVIE_ID};

        String selection = FavoriteMovieEntry.MOVIE_ID + "=?";

        String[] selectionargs = {String.valueOf(movieId)};

        return mDb.query(table,columns,selection, selectionargs,null,null,null,null);

    }

    private void onClickAddMovie(View view)
    {
        if(mFavButton.isEnabled())
        {
            ContentValues cv = new ContentValues();
            cv.put(FavoriteMovieEntry.MOVIE_ID, ""+movie.getMovieId());
            cv.put(FavoriteMovieEntry.MOVIE_NAME, movie.getMovieName());
            cv.put(FavoriteMovieEntry.RELEASE_DATE, movie.getMovieReleaseDate());
            cv.put(FavoriteMovieEntry.MOVIE_REVIEW, movie.getMovieInfo());
            cv.put(FavoriteMovieEntry.MOVIE_POSTER_PATH, movie.getMoviePosterPath());

            Uri uri = getContentResolver().insert(FavoriteMovieEntry.CONTENT_URI, cv);

            if(uri !=null)
            {
                Toast.makeText(getApplicationContext(), uri.toString(), Toast.LENGTH_SHORT ).show();
            }
            finish();
        }
    }


}











