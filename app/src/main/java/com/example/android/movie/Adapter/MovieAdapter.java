package com.example.android.movie.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.movie.Movie.Movie;
import com.example.android.movie.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by SSubra27 on 3/20/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Movie> mMovieData;

    public MovieAdapter(Context ct, ArrayList mD)
    {
        context = ct;
        mMovieData = mD;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

      LayoutInflater inflater =  LayoutInflater.from(context);
        int id = R.layout.movie_detail;
        boolean attachToParent = false;
        View view = inflater.inflate(id,parent,attachToParent);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movie = mMovieData.get(position);
        String imageUri= "https://image.tmdb.org/t/p/w185/"+movie.getMoviePosterPath();

        Picasso.with(context).load(imageUri).into(holder.mMoviewPosterView);

        holder.mMovieName.setText(movie.getMovieName());
    }

    @Override
    public int getItemCount() {
        return mMovieData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView mMoviewPosterView;
        private TextView mMovieName;

        public ViewHolder(View itemView) {
            super(itemView);
            mMoviewPosterView=(ImageView) itemView.findViewById(R.id.iv_movie_poster);
            mMovieName=(TextView) itemView.findViewById(R.id.tv_movie_name);

        }
    }

    public void setMovieData(ArrayList<Movie> movieData)
    {
        mMovieData = movieData;
        notifyDataSetChanged();
    }
}
