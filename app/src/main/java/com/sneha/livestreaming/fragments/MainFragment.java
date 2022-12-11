package com.sneha.livestreaming.fragments;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sneha.livestreaming.DepthPageTransformer;
import com.sneha.livestreaming.R;
import com.sneha.livestreaming.adapters.MyPagerAdapter;
import com.sneha.livestreaming.ui.MainActivity;

import java.util.ArrayList;


public class MainFragment extends Fragment {

    TabLayout tablayout;
    ViewPager pager;
    MyPagerAdapter myPagerAdapter;
    ArrayList<Fragment> fragments = new ArrayList<>();
    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        setUpView(view);
        setUpViewpger();
        return view;
    }

    private void setUpViewpger() {

        myPagerAdapter = new MyPagerAdapter(getChildFragmentManager());
        pager.setAdapter(myPagerAdapter);
        pager.setOffscreenPageLimit(fragments.size());
        pager.setPageTransformer(true, new DepthPageTransformer());
        tablayout.setupWithViewPager(pager);
        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ((MainActivity)getActivity()).et.setText("");
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setUpView(View view) {
        tablayout = view.findViewById(R.id.tablayout);
        pager = view.findViewById(R.id.pager);
    }



}
