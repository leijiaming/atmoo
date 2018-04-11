package com.yysj.atmoo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by asus on 2018/3/24.
 */

public class ChannelAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    private List<String> titles;
    public ChannelAdapter(FragmentManager fm) {
        super(fm);
    }
    public void setFragments(List<Fragment> fragments,List<String> titles){
        this.fragments = fragments;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
