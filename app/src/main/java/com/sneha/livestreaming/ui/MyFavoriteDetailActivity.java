package com.sneha.livestreaming.ui;

import android.app.Activity;
import android.content.Intent;

import com.sneha.livestreaming.databinding.ActivityScrollingBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sneha.livestreaming.R;
import com.sneha.livestreaming.database.RealamDatabase;
import com.sneha.livestreaming.database.realmmodel.MyFavoritemodel;
import com.sneha.livestreaming.utility.Utility;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.thefinestartist.ytpa.enums.Orientation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyFavoriteDetailActivity extends BaseActivity {
    private RelativeLayout withoutSearch, withSearch;
    private LinearLayout containerView;
    private AppCompatImageView navigationdrawerandback, navigationSearch, navigationShare, main_imageview_placeholder, play_video;
    private TextView toolbarCenterText, title_text, date_text, description_text;
    private SearchView searchView;
    private FloatingActionButton fab;
    private int a = 0;
    private Intent intent;
    private MyFavoritemodel myFavoritemodel;
    Activity mActivity;
    ActivityScrollingBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_scrolling);
        mActivity = this;
        mBinding= DataBindingUtil.setContentView(mActivity,R.layout.activity_scrolling);

        intiView();
        intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("List")) {
                myFavoritemodel = (MyFavoritemodel) intent.getSerializableExtra("List");
                setFavIcon();
                setData();
            }
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (a == 0) {
                    a = 1;
                    saveToFavorate(myFavoritemodel);
                    fab.setImageResource(R.drawable.ic_favorite);
                    Snackbar.make(view, "Channel selected as Favorite ", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else if (a == 1) {
                    a = 0;
                    deleteFavorate(myFavoritemodel);
                    fab.setImageResource(R.drawable.ic_un_favorite);
                    Snackbar.make(view, "Channel selected as Non Favorite", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });
        navigationdrawerandback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        navigationShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.shareApp(MyFavoriteDetailActivity.this);
            }
        });
       /* play_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myFavoritemodel.getChannel_type().equalsIgnoreCase("youtube")) {
                    Intent intent = new Intent(MyFavoriteDetailActivity.this, YouTubePlayerActivity.class);
                    intent.putExtra(YouTubePlayerActivity.EXTRA_VIDEO_ID, myFavoritemodel.getChannels_url());
                    intent.putExtra(YouTubePlayerActivity.EXTRA_PLAYER_STYLE, YouTubePlayer.PlayerStyle.DEFAULT);
                    intent.putExtra(YouTubePlayerActivity.EXTRA_ORIENTATION, Orientation.AUTO);
                    intent.putExtra(YouTubePlayerActivity.EXTRA_SHOW_AUDIO_UI, true);
                    intent.putExtra(YouTubePlayerActivity.EXTRA_HANDLE_ERROR, true);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                } else {
                    startActivity(new Intent(MyFavoriteDetailActivity.this, VideoPlayerActivity.class).putExtra("ID", myFavoritemodel.getChannels_url()));
                }
            }
        });*/


        if (myFavoritemodel.getChannel_type().equalsIgnoreCase("youtube")&&myFavoritemodel.getChannels_url()!=null) {

                    /*Intent intent = new Intent(LatestChannelDetailActivity.this, YouTubePlayerActivity.class);
                    intent.putExtra(YouTubePlayerActivity.EXTRA_VIDEO_ID, listOfAllLatestChannels.getChannels_url());
                    intent.putExtra(YouTubePlayerActivity.EXTRA_PLAYER_STYLE, YouTubePlayer.PlayerStyle.DEFAULT);
                    intent.putExtra(YouTubePlayerActivity.EXTRA_ORIENTATION, Orientation.AUTO);
                    intent.putExtra(YouTubePlayerActivity.EXTRA_SHOW_AUDIO_UI, true);
                    intent.putExtra(YouTubePlayerActivity.EXTRA_HANDLE_ERROR, true);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);*/

            mBinding.videoPanel.setVisibility(View.VISIBLE);

            mBinding.thumbnail.initialize(getResources().getString(R.string.youtube_api_key), new YouTubeThumbnailView.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
                    //when initialization is sucess, set the video id to thumbnail to load
                    youTubeThumbnailLoader.setVideo(extractYTId("https://www.youtube.com/watch?v="+myFavoritemodel.getChannels_url()));

                    youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                        @Override
                        public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                            //when thumbnail loaded successfully release the thumbnail loader as we are showing thumbnail in adapter
                            youTubeThumbnailLoader.release();
                            mBinding.videoPanel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(mActivity, PlayFullScreenVideoActivity3.class);
                                    ///intent.putExtra("video_link","https://www.youtube.com/watch?v="+listOfAllLatestChannels.getChannels_url());
                                   // intent.putExtra("video_link","https://www.youtube.com/watch?v="+myFavoritemodel.getChannels_url());
                                    intent.putExtra("video_link",myFavoritemodel.getChannels_url());
                                    startActivity(intent);

                                }
                            });
                        }

                        @Override
                        public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
                            //print or show error when thumbnail load failed

                        }
                    });
                }

                @Override
                public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                    //print or show error when initialization failed
                    //Log.e(TAG, "Youtube Initialization Failure");

                }
            });

        } else if (myFavoritemodel.getChannels_url()!=null){
            mBinding.videoPanel.setVisibility(View.VISIBLE);
            String url1,url = null;
            url  = myFavoritemodel.getChannels_url();
            if (url.contains("https://youtu.be/")){

                url1= url.replace("https://youtu.be/","https://www.youtube.com/watch?v=");}
            else {
                url1=url;
            }

            //startActivity(new Intent(LatestChannelDetailActivity.this, VideoPlayerActivity.class).putExtra("ID", listOfAllLatestChannels.getChannels_url()));


            mBinding.thumbnail.initialize(getResources().getString(R.string.youtube_api_key), new YouTubeThumbnailView.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
                    //when initialization is sucess, set the video id to thumbnail to load
                    youTubeThumbnailLoader.setVideo(extractYTId(url1));

                    youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                        @Override
                        public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                            //when thumbnail loaded successfully release the thumbnail loader as we are showing thumbnail in adapter
                            youTubeThumbnailLoader.release();
                            mBinding.videoPanel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(mActivity, PlayFullScreenVideoActivity3.class);
                                    ///intent.putExtra("video_link","https://www.youtube.com/watch?v="+listOfAllLatestChannels.getChannels_url());
                                    intent.putExtra("video_link",url1);
                                    startActivity(intent);

                                }
                            });
                        }

                        @Override
                        public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
                            //print or show error when thumbnail load failed

                        }
                    });
                }

                @Override
                public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                    //print or show error when initialization failed
                    //Log.e(TAG, "Youtube Initialization Failure");

                }
            });
        }

    }


    public static String extractYTId(String ytUrl) {
        String pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(ytUrl);
        if(matcher.find()){
            return matcher.group();
        } else {
            return "error";
        }
    }

    private void deleteFavorate(MyFavoritemodel myFavoritemodel) {
        RealamDatabase.getInstance().deleteRowInMyFavorate(myFavoritemodel.getChannels_id());
    }



    private void saveToFavorate(MyFavoritemodel listOfAllLatestChannels) {
        MyFavoritemodel myFavoritemodel = new MyFavoritemodel();
        myFavoritemodel.setChannel_description(listOfAllLatestChannels.getChannel_description());
        myFavoritemodel.setChannel_type(listOfAllLatestChannels.getChannel_type());
        myFavoritemodel.setChannels_image(listOfAllLatestChannels.getChannels_image());
        myFavoritemodel.setChannels_name(listOfAllLatestChannels.getChannels_name());
        myFavoritemodel.setChannels_url(listOfAllLatestChannels.getChannels_url());
        myFavoritemodel.setCreated_at(listOfAllLatestChannels.getCreated_at());
        myFavoritemodel.setCat_id(listOfAllLatestChannels.getCat_id());
        myFavoritemodel.setChannels_id(listOfAllLatestChannels.getChannels_id());
        myFavoritemodel.setFavorite(true);
        RealamDatabase.getInstance().saveMyFavorite(myFavoritemodel);
    }

    private void setFavIcon() {

        if (myFavoritemodel.isFavorite()) {
            a = 1;
            fab.setImageResource(R.drawable.ic_favorite);
        } else {
            a = 0;
            fab.setImageResource(R.drawable.ic_un_favorite);
        }

    }

    private void setData() {

        Glide.with(MyFavoriteDetailActivity.this).load(myFavoritemodel.getChannels_image())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(main_imageview_placeholder);
        title_text.setText(myFavoritemodel.getChannels_name());
        date_text.setText(myFavoritemodel.getCreated_at());
        description_text.setText(myFavoritemodel.getChannel_description());
    }

    private void intiView() {
        fab = findViewById(R.id.fab);
        withoutSearch = findViewById(R.id.withoutSearch);
        withSearch = findViewById(R.id.withSearch);
        containerView = findViewById(R.id.containerView);
        navigationdrawerandback = findViewById(R.id.navigationdrawerandback);
        navigationSearch = findViewById(R.id.navigationSearch);
        navigationShare = findViewById(R.id.navigationShare);
        toolbarCenterText = findViewById(R.id.toolbarCenterText);
        searchView = findViewById(R.id.searchView);
        main_imageview_placeholder = findViewById(R.id.main_imageview_placeholder);
        play_video = findViewById(R.id.play_video);
        description_text = findViewById(R.id.description_text);
        title_text = findViewById(R.id.title_text);
        date_text = findViewById(R.id.date_text);
        navigationdrawerandback.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbarCenterText.setText("");
        navigationSearch.setVisibility(View.GONE);
    }


    @Override
    public void onBackPressed() {
        getBackEvet();
        startActivity(new Intent(MyFavoriteDetailActivity.this, MainActivity.class).putExtra("abc", ""));
        finish();
    }
}
