package com.sneha.livestreaming.ui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.SearchView;

import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sneha.livestreaming.R;
import com.sneha.livestreaming.adapters.CustomAdapter;
import com.sneha.livestreaming.database.RealamDatabase;
import com.sneha.livestreaming.fragments.AboutUsFragment;
import com.sneha.livestreaming.fragments.FavoriteFragment;
import com.sneha.livestreaming.fragments.MainFragment;
import com.sneha.livestreaming.model.DataModel;
import com.sneha.livestreaming.utility.Utility;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends com.sneha.livestreaming.ui.BaseActivity {

    RelativeLayout withoutSearch, withSearch;
    LinearLayout containerView;
    AppCompatImageView navigationdrawerandback, navigationSearch, navigationShare;
    TextView toolbarCenterText;
    public SearchView searchView;
    ImageView mCloseButton;
    DrawerLayout drawer_layout;
    ListView navigationList;
    ActionBarDrawerToggle mDrawerToggle;
    FrameLayout fragmentContainer;
    private FirebaseAnalytics mFirebaseAnalytics;
    public EditText et;
    boolean doubleBackToExitPressedOnce = false;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, MainActivity.class.getName());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        initView();
        searchViewPreoperty();
        setUpDrawerAdapter();
        intent  = getIntent();

        if(intent != null){
            if(intent.hasExtra("abc")){
                toolbarCenterText.setText("Favorite");
                replaceFragment(new FavoriteFragment());
            }else{
                toolbarCenterText.setText("Home");
                replaceFragment(new MainFragment());
            }
        }else{
            toolbarCenterText.setText("Home");
            replaceFragment(new MainFragment());
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                mCloseButton.setVisibility(View.VISIBLE );
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //    adapter.getFilter().filter(newText);
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
                Utility.shareApp(MainActivity.this);
            }
        });

        navigationdrawerandback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer_layout.openDrawer(Gravity.START);
            }
        });
        containerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer_layout.closeDrawer(Gravity.START);
            }
        });
    }

    private void setUpDrawerAdapter() {

        CustomAdapter simpleAdapter = new CustomAdapter(MainActivity.this, getArrayList());
        navigationList.setAdapter(simpleAdapter);
        listViewCVlickEvent(navigationList);
        mDrawerToggle = new ActionBarDrawerToggle(this, drawer_layout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {

            }
        };
        drawer_layout.setDrawerListener(mDrawerToggle);

    }

    private List<DataModel> getArrayList() {
        List<DataModel> list = new ArrayList<>();
        list.add(new DataModel(R.drawable.ic_home, "Home"));
        list.add(new DataModel(R.drawable.ic_favorite, "Favorite"));
        list.add(new DataModel(R.drawable.ic_local_phone, "Contact Us"));
        list.add(new DataModel(R.drawable.ic_about, "About"));
        return list;
    }

    private void listViewCVlickEvent(ListView navigationList) {

        navigationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        toolbarCenterText.setText("Home");
                        replaceFragment(new MainFragment());
                        navigationSearch.setVisibility(View.VISIBLE);
                        drawer_layout.closeDrawer(Gravity.START);
                        break;
                    case 1:
                        toolbarCenterText.setText("Favorite");
                        replaceFragment(new FavoriteFragment());
                        navigationSearch.setVisibility(View.VISIBLE);
                        drawer_layout.closeDrawer(Gravity.START);
                        break;
                    case 2:
                        sendEmail();
                        navigationSearch.setVisibility(View.GONE);
                        drawer_layout.closeDrawer(Gravity.START);
                        break;
                    case 3:
                        toolbarCenterText.setText("About");
                        replaceFragment(new AboutUsFragment());
                        navigationSearch.setVisibility(View.GONE);
                        drawer_layout.closeDrawer(Gravity.START);
                        break;
                }
            }
        });
    }

    public void sendEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_VIEW);
        emailIntent.setData(Uri.parse("mailto:" + "info@abhiandroid.com" + "?subject=" + getResources().getString(R.string.app_name) + " feedback"));
        startActivity(Intent.createChooser(emailIntent, "E_mail"));

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

    private void initView() {
        withoutSearch = findViewById(R.id.withoutSearch);
        withSearch = findViewById(R.id.withSearch);
        navigationdrawerandback = findViewById(R.id.navigationdrawerandback);
        navigationSearch = findViewById(R.id.navigationSearch);
        navigationShare = findViewById(R.id.navigationShare);
        toolbarCenterText = findViewById(R.id.toolbarCenterText);
        searchView = findViewById(R.id.searchView);
        drawer_layout = findViewById(R.id.drawer_layout);
        navigationList = findViewById(R.id.left_drawer);
        fragmentContainer = findViewById(R.id.fragmentContainer);
        containerView = findViewById(R.id.containerView);
    }

    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
    }





    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finish();
            RealamDatabase.getInstance().resetRealm();

            super.onBackPressed();
            return;
        }
        if(et.getText().toString().isEmpty()){
            withSearch.setVisibility(View.GONE);
            withoutSearch.setVisibility(View.VISIBLE);
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please double tap to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Utility.hideKeyboard(MainActivity.this);
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }


}
