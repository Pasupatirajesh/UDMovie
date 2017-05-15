package com.example.android.movie.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
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

import java.util.ArrayList;

import static com.example.android.movie.Databases.FavoriteMovieContract.FavoriteMovieEntry;

public class  MovieDetailActivity extends AppCompatActivity {

    private TextView mTitleTextView;
    private ImageView mPosterImageView;
    private TextView mReleaseDateTextView;
    private TextView mAverageScoreTextView;
    private TextView mSynopsisTextView;
    private Button mFavButton;

    private Button mTrailerButton;

    private Movie movie;

    private SQLiteDatabase mDb;

    private Cursor mCursor;

    private ArrayList<Movie> mTrailerArrayList;

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


        mTrailerArrayList = new ArrayList<>();
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

                Log.i("TrailerURL", "" + Network.buildTrailerUrl(String.valueOf(movie.getMovieId())));
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
                new FetchTrailerTask().execute();
            }
        });

        mFavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClickAddMovie(v);
            }
        });
    }



    public class FetchTrailerTask extends AsyncTask<Void, Void, ArrayList<Movie>>
    {

        @Override
        protected ArrayList<Movie> doInBackground(Void... params) {

            try {
                return new Network().fetchTrailerItems(String.valueOf(movie.getMovieId()));

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            super.onPostExecute(movies);
            mTrailerArrayList=movies;
            Log.i("TrailerArrayList", ""+mTrailerArrayList.size());
            for(int j=0;j<mTrailerArrayList.size(); j++)
            {

                String trailerKey =mTrailerArrayList.get(0).getTrailerKey();
                Log.i("trailerKey", ""+trailerKey);
                Intent trailerIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtube.com/watch?v="+ trailerKey));
                startActivity(trailerIntent);
            }
        }
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











