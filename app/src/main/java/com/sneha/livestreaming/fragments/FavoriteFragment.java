package com.sneha.livestreaming.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sneha.livestreaming.R;
import com.sneha.livestreaming.adapters.FavoriteAdapter;
import com.sneha.livestreaming.database.RealamDatabase;
import com.sneha.livestreaming.database.realmmodel.MyFavoritemodel;
import com.sneha.livestreaming.ui.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment {

    FavoriteAdapter favoriteAdapter;
    private SwipeRefreshLayout swipeToRefreshLatestChannel;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private TextView noChannelFound;

    public FavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_latest_channel, container, false);
        setUpView(view);
        setUpAdapterWithValues();
        swipeToRefreshLatestChannel.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromServer();
            }
        });

        ((MainActivity) getActivity()).et.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int aft) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null) {
                    callSearch(s.toString());
                } else {
                    callSearch("");
                }

            }
        });


        return view;
    }


    private void getDataFromServer() {
        setUpAdapterWithValues();
        swipeToRefreshLatestChannel.setRefreshing(false);

    }

    private void callSearch(String query) {
        if (favoriteAdapter != null) {
            favoriteAdapter.getFilter().filter(query);
        }
    }

    private void setUpAdapterWithValues() {
        List<MyFavoritemodel> listOfAllLatestChannels = new ArrayList<>();
        listOfAllLatestChannels.addAll(RealamDatabase.getInstance().getAllMyFavorite());
        if (listOfAllLatestChannels.size() != 0) {
            noChannelFound.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);

            favoriteAdapter = new FavoriteAdapter(getActivity(), FavoriteFragment.this, listOfAllLatestChannels);
            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(favoriteAdapter);
        } else {

            noChannelFound.setVisibility(View.VISIBLE);
            noChannelFound.setText("No Favorite Channels. ");
            mRecyclerView.setVisibility(View.GONE);
        }

    }

    private void setUpView(View view) {
        swipeToRefreshLatestChannel = view.findViewById(R.id.swipeToRefreshLatestChannel);
        mRecyclerView = view.findViewById(R.id.mRecyclerView);
        noChannelFound = view.findViewById(R.id.noChannelFound);
    }

}
