package com.example.android.movie.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.android.movie.Fragments.FavoriteMovieViewPagerFragment;
import com.example.android.movie.R;

public class FavoriteMovieDisplayActivity extends AppCompatActivity  {

    public PagerAdapter mFragmentPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_movie_display);

        mFragmentPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.vp_fav_movie_pager);

        mViewPager.setAdapter(mFragmentPagerAdapter);


    }

    public class MyPagerAdapter extends FragmentStatePagerAdapter {

        public MyPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            Fragment frag = new FavoriteMovieViewPagerFragment();
            return frag;
        }

        @Override
        public int getCount() {
            return 100;
        }
    }
}



