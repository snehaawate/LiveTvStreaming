package com.sneha.livestreaming.ui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sneha.livestreaming.R;
import com.sneha.livestreaming.adapters.AllCategoryAdapter;
import com.sneha.livestreaming.database.RealamDatabase;
import com.sneha.livestreaming.database.realmmodel.InnerChannelModel;
import com.sneha.livestreaming.utility.Utility;

import java.util.ArrayList;
import java.util.List;

public class SelectedCategoryActivity extends BaseActivity {

    private RecyclerView mSelectedRecyclerView;
    private TextView noSelectedChannelFound;
    private RelativeLayout withoutSearch, withSearch;
    private LinearLayout containerView;
    private AppCompatImageView navigationdrawerandback, navigationSearch, navigationShare;
    private TextView toolbarCenterText;
    private SearchView searchView;
    private ImageView mCloseButton;
    private Intent intent;
    private String Channel_ID;
    private AllCategoryAdapter allCategoryAdapter;
    private LinearLayoutManager linearLayoutManager;
    private EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_category);
        intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("Channel_ID")) {
                Channel_ID = intent.getStringExtra("Channel_ID");
            }
        }
        initView();
        searchViewPreoperty();
        setUpAdapter();
        toolbarCenterText.setText("");


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                mCloseButton.setVisibility(View.VISIBLE );
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //    adapter.getFilter().filter(newText);
                if (newText != null) {
                    allCategoryAdapter.getFilter().filter(newText);
                } else {
                    allCategoryAdapter.getFilter().filter("");
                }
                mCloseButton.setVisibility(newText.isEmpty() ? View.VISIBLE : View.VISIBLE);
                return false;
            }
        });

        navigationSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withSearch.setVisibility(View.VISIBLE);
                withoutSearch.setVisibility(View.GONE);
                searchView.setFocusable(true);
                searchView.requestFocusFromTouch();
                mCloseButton.setVisibility(View.VISIBLE);
                InputMethodManager inputMethodManager =
                        (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInputFromWindow(
                        withSearch.getApplicationWindowToken(),
                        InputMethodManager.SHOW_FORCED, 0);
            }
        });

        navigationShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.shareApp(SelectedCategoryActivity.this);
            }
        });
        navigationdrawerandback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            onBackPressed();
            }
        });


    }

    private void setUpAdapter() {
        List<InnerChannelModel> listOfAllLatestChannels = new ArrayList<>();
        listOfAllLatestChannels.addAll(RealamDatabase.getInstance().getAllChannelList(Channel_ID));
        if (listOfAllLatestChannels.size() != 0) {
            noSelectedChannelFound.setVisibility(View.GONE);
            mSelectedRecyclerView.setVisibility(View.VISIBLE);
            allCategoryAdapter = new AllCategoryAdapter(SelectedCategoryActivity.this, listOfAllLatestChannels);
            linearLayoutManager = new LinearLayoutManager(SelectedCategoryActivity.this);
            mSelectedRecyclerView.setLayoutManager(linearLayoutManager);
            mSelectedRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mSelectedRecyclerView.setAdapter(allCategoryAdapter);
        } else {
            noSelectedChannelFound.setVisibility(View.VISIBLE);
            mSelectedRecyclerView.setVisibility(View.GONE);
        }


    }

    private void initView() {
        mSelectedRecyclerView = findViewById(R.id.mSelectedRecyclerView);
        noSelectedChannelFound = findViewById(R.id.noSelectedChannelFound);
        withoutSearch = findViewById(R.id.withoutSearch);
        withSearch = findViewById(R.id.withSearch);
        containerView = findViewById(R.id.containerView);
        navigationdrawerandback = findViewById(R.id.navigationdrawerandback);
        navigationSearch = findViewById(R.id.navigationSearch);
        navigationShare = findViewById(R.id.navigationShare);
        toolbarCenterText = findViewById(R.id.toolbarCenterText);
        searchView = findViewById(R.id.searchView);
        navigationdrawerandback.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_back));

    }

    private void searchViewPreoperty() {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mCloseButton = (ImageView) searchView.findViewById(R.id.search_close_btn);
        mCloseButton.setVisibility(View.VISIBLE);
        et = (EditText) findViewById(R.id.search_src_text);
        et.setTextColor(getResources().getColor(R.color.white));

        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et.setText("");
                withSearch.setVisibility(View.GONE);
                withoutSearch.setVisibility(View.VISIBLE);

            }
        });

    }

    @Override
    public void onBackPressed() {
        if(et.getText().toString().isEmpty()){
            withSearch.setVisibility(View.GONE);
            withoutSearch.setVisibility(View.VISIBLE);
        }
        getBackEvet();
        finish();
    }
}
