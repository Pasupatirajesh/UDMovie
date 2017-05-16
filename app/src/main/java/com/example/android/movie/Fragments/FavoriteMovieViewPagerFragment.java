package com.example.android.movie.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
    private TextView mSynopsisTextView;

    private static Movie mMovie = new Movie();

    public static FavoriteMovieViewPagerFragment newInstance(Movie movie) {
        FavoriteMovieViewPagerFragment f = new FavoriteMovieViewPagerFragment();
        Bundle args = new Bundle();
        args.putParcelable("Movie", Parcels.wrap(movie));
        f.setArguments(args);
        return f;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.favorites_fragment, container, false);
        mTitleTextView = (TextView) v.findViewById(R.id.tv_fav_title_text_view);

        mPosterImageView = (ImageView) v.findViewById(R.id.iv_fav_poster_view);

        mSynopsisTextView = (TextView) v.findViewById(R.id.tv_fav_synopsis_view);

        Bundle myBundle = this.getArguments();

        if (myBundle != null) {
            if (myBundle.getParcelable("Movie") != null) {

                mMovie = Parcels.unwrap(myBundle.getParcelable("Movie"));

                mTitleTextView.setText(mMovie.getMovieName());

                String imageUri = "https://image.tmdb.org/t/p/w185/" + mMovie.getMoviePosterPath();

                Picasso.with(getActivity()).load(imageUri).into(mPosterImageView);

                mSynopsisTextView.setText(mMovie.getMovieInfo());

                }
            }

            return v;
    }


}
