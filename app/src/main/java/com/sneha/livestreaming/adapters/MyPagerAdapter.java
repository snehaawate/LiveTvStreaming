package com.sneha.livestreaming.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.sneha.livestreaming.fragments.CategoryFragment;
import com.sneha.livestreaming.fragments.LatestChannelFragment;
public class MyPagerAdapter extends FragmentPagerAdapter {


    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new LatestChannelFragment();
            case 1:
                return new CategoryFragment();
            default:
                return new LatestChannelFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Latest Channel";
            case 1:
                return "Categories";
            default:
                return null;
        }
    }
}
