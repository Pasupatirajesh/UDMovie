package com.example.android.movie.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.movie.Movie.Movie;
import com.example.android.movie.R;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

/**
 * Created by SSubra27 on 5/8/17.
 */

public class FavoriteMovieViewPagerFragment extends android.support.v4.app.Fragment {

    private TextView mTitleTextView;
    private ImageView mPosterImageView;
    private TextView mSysnopsisTextView;

    private Movie mMovie;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

         inflater= LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.favorites_fragment, container, false);


        Bundle myBundle = this.getArguments();

        if(myBundle!=null) {
            if (myBundle.getParcelable("Movie") != null) {

                mMovie = Parcels.unwrap(myBundle.getParcelable("Movie"));

                Log.i("movieName", mMovie.getMovieName());
                mTitleTextView =(TextView) v.findViewById(R.id.tv_fav_title_text_view);

                mPosterImageView = (ImageView) v.findViewById(R.id.iv_fav_poster_view);

                mSysnopsisTextView = (TextView) v.findViewById(R.id.tv_fav_synopsis_view);

                mTitleTextView.setText(mMovie.getMovieName());

                Picasso.with(getContext()).load(mMovie.getMoviePosterPath()).into(mPosterImageView);

                mSysnopsisTextView.setText(mMovie.getMovieInfo());

            }
        }
        return  v;

    }
}
