package com.sneha.livestreaming.fragments;


import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.sneha.livestreaming.adapters.CaregoryAdapter;
import com.sneha.livestreaming.database.RealamDatabase;
import com.sneha.livestreaming.database.realmmodel.InnerCategoryModel;
import com.sneha.livestreaming.ui.MainActivity;
import com.sneha.livestreaming.utility.GetFullApiData;
import com.sneha.livestreaming.utility.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {

    private SwipeRefreshLayout swipeToRefreshCategory;
    private CaregoryAdapter caregoryAdapter;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView mCategoryRecyclerView;
    private TextView noCategoryFound;
    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_category, container, false);
        setUpView(view);
        setUpAdapterWithValues();
        swipeToRefreshCategory.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
                   // RealamDatabase.getInstance().resetRealm();
                    swipeToRefreshCategory.setRefreshing(false);
                }
            });
        }else{
            swipeToRefreshCategory.setRefreshing(false);
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
        caregoryAdapter.getFilter().filter(query);
    }

    private void setUpView(View view) {
        swipeToRefreshCategory = view.findViewById(R.id.swipeToRefreshCategory);
        mCategoryRecyclerView = view.findViewById(R.id.mCategoryRecyclerView);
        noCategoryFound = view.findViewById(R.id.noChannelFound);
    }
    private void setUpAdapterWithValues() {
        List<InnerCategoryModel> allDataModel = new ArrayList<>();
        allDataModel.addAll(RealamDatabase.getInstance().getAllDataModel());
        if(allDataModel.size() != 0){
            noCategoryFound.setVisibility(View.GONE);
            mCategoryRecyclerView.setVisibility(View.VISIBLE);

            caregoryAdapter = new CaregoryAdapter(getActivity(),CategoryFragment.this,allDataModel);
            mLayoutManager = new GridLayoutManager(getActivity(),2);
            mCategoryRecyclerView.setLayoutManager(mLayoutManager);
            mCategoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mCategoryRecyclerView.setAdapter(caregoryAdapter);
        }else{
            noCategoryFound.setVisibility(View.VISIBLE);
            mCategoryRecyclerView.setVisibility(View.GONE);
        }

    }
}
