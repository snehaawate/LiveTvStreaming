package com.sneha.livestreaming.fragments;


import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
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

import com.sneha.livestreaming.OnSuccessFullyFetch;
import com.sneha.livestreaming.R;
import com.sneha.livestreaming.adapters.LatestNewsAdapter;
import com.sneha.livestreaming.database.RealamDatabase;
import com.sneha.livestreaming.database.realmmodel.InnerCategoryModel;
import com.sneha.livestreaming.database.realmmodel.LatestChannelModel;
import com.sneha.livestreaming.ui.MainActivity;
import com.sneha.livestreaming.utility.GetFullApiData;
import com.sneha.livestreaming.utility.Utility;

import java.util.ArrayList;
import java.util.List;


public class LatestChannelFragment extends Fragment {

    private SwipeRefreshLayout swipeToRefreshLatestChannel;
    private LatestNewsAdapter latestNewsAdapter;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private TextView noChannelFound;

    int position = 0;

    public LatestChannelFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_latest_channel, container, false);


        setUpView(view);
        setUpAdapterWithValues();
        swipeToRefreshLatestChannel.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
              getDataFromServer(view);
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
                if(s != null) {
                    callSearch(s.toString());
                }else{
                    callSearch("");
                }

            }
        });

        return view;
    }



    private void getDataFromServer(View view) {
        if(Utility.isInternetConnection(getActivity())) {
            new GetFullApiData(getActivity(), new OnSuccessFullyFetch() {
                @Override
                public void onSuccess() {
                    setUpAdapterWithValues();
                    swipeToRefreshLatestChannel.setRefreshing(false);
                }
            });
        }else{
            swipeToRefreshLatestChannel.setRefreshing(false);
            Snackbar
                    .make(view, "No internet connection!", Snackbar.LENGTH_LONG)
                    .setAction("TRY AGAIN", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getDataFromServer(view);
                        }
                    }).show();
        }
    }

    private void callSearch(String query) {
        if(latestNewsAdapter != null) {
            latestNewsAdapter.getFilter().filter(query);
        }
    }

    private void setUpAdapterWithValues() {

        List<LatestChannelModel> listOfAllLatestChannels = new ArrayList<>();
        List<InnerCategoryModel> listOfAllLatestChannels1 = new ArrayList<>();
        listOfAllLatestChannels1.addAll(RealamDatabase.getInstance().getAllDataModel());
        listOfAllLatestChannels.addAll(RealamDatabase.getInstance().getListOfAllLatestChannels());
        if (listOfAllLatestChannels.size() != 0) {
            noChannelFound.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);

            latestNewsAdapter = new LatestNewsAdapter(getActivity(), LatestChannelFragment.this, listOfAllLatestChannels,listOfAllLatestChannels1);
            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(latestNewsAdapter);
        } else {
            noChannelFound.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }

    }

    private void setUpView(View view) {
        swipeToRefreshLatestChannel = view.findViewById(R.id.swipeToRefreshLatestChannel);
        mRecyclerView = view.findViewById(R.id.mRecyclerView);
        noChannelFound = view.findViewById(R.id.noChannelFound);
    }


}
