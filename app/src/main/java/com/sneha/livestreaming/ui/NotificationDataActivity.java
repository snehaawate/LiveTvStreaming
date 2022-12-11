package com.sneha.livestreaming.ui;

import android.app.Activity;
import android.content.Intent;

import com.sneha.livestreaming.databinding.ActivityScrollingBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;

import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sneha.livestreaming.R;
import com.sneha.livestreaming.database.RealamDatabase;
import com.sneha.livestreaming.database.realmmodel.LatestChannelModel;
import com.sneha.livestreaming.database.realmmodel.MyFavoritemodel;
import com.sneha.livestreaming.utility.Utility;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.thefinestartist.ytpa.enums.Orientation;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NotificationDataActivity extends BaseActivity {

    private AppCompatImageView navigationdrawerandback, navigationSearch, navigationShare, main_imageview_placeholder, play_video;
    private TextView toolbarCenterText, title_text, date_text, description_text;
    private SearchView searchView;
    private FloatingActionButton fab;
    private int a = 0;
    private String ID = "";
    private Intent intent;
    private List<LatestChannelModel> InnerChannelModel;
    private List<MyFavoritemodel> myFavoritemodel;
    ActivityScrollingBinding mBinding;
    Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity= this;
        mBinding= DataBindingUtil.setContentView(mActivity,R.layout.activity_scrolling);

      //  setContentView(R.layout.activity_scrolling);
        intiView();
        intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("id")) {
                ID = intent.getStringExtra("id");
                InnerChannelModel = RealamDatabase.getInstance().getListOfAllLatestChannelsID(ID);
                myFavoritemodel = RealamDatabase.getInstance().getMyFavorite(ID);
                setFavIcon();
                setData();
            }
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (a == 0) {
                    a = 1;

                    saveToFavorate(InnerChannelModel.get(0));
                    myFavoritemodel.clear();
                    myFavoritemodel = RealamDatabase.getInstance().getMyFavorite(InnerChannelModel.get(0).getChannels_id());
                    fab.setImageResource(R.drawable.ic_favorite);
                    Utility.showToastMessageShort(NotificationDataActivity.this, "Channel selected as Favorite");
                } else if (a == 1) {
                    a = 0;

                    deleteFavorate(InnerChannelModel.get(0));
                    myFavoritemodel.clear();
                    myFavoritemodel = RealamDatabase.getInstance().getMyFavorite(InnerChannelModel.get(0).getChannels_id());
                    fab.setImageResource(R.drawable.ic_un_favorite);
                    Utility.showToastMessageShort(NotificationDataActivity.this, "Channel selected as Non Favorite");

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
                Utility.shareApp(NotificationDataActivity.this);
            }
        });

        /*play_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InnerChannelModel.get(0).getChannel_type().equalsIgnoreCase("youtube")) {
                    Intent intent = new Intent(NotificationDataActivity.this, YouTubePlayerActivity.class);
                    intent.putExtra(YouTubePlayerActivity.EXTRA_VIDEO_ID, InnerChannelModel.get(0).getChannels_url());
                    intent.putExtra(YouTubePlayerActivity.EXTRA_PLAYER_STYLE, YouTubePlayer.PlayerStyle.DEFAULT);
                    intent.putExtra(YouTubePlayerActivity.EXTRA_ORIENTATION, Orientation.AUTO);
                    intent.putExtra(YouTubePlayerActivity.EXTRA_SHOW_AUDIO_UI, true);
                    intent.putExtra(YouTubePlayerActivity.EXTRA_HANDLE_ERROR, true);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                } else {
                    startActivity(new Intent(NotificationDataActivity.this, VideoPlayerActivity.class).putExtra("ID", InnerChannelModel.get(0).getChannels_url()));
                }
            }
        });*/

        if (InnerChannelModel.get(0).getChannel_type().equalsIgnoreCase("youtube")&&InnerChannelModel.get(0).getChannels_url()!=null) {

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
                    youTubeThumbnailLoader.setVideo(extractYTId("https://www.youtube.com/watch?v="+InnerChannelModel.get(0).getChannels_url()));

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
                                    //intent.putExtra("video_link","https://www.youtube.com/watch?v="+InnerChannelModel.get(0).getChannels_url());
                                    intent.putExtra("video_link",InnerChannelModel.get(0).getChannels_url());
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

        } else if (InnerChannelModel.get(0).getChannels_url()!=null){
            mBinding.videoPanel.setVisibility(View.VISIBLE);
            String url1,url = null;
            url  = InnerChannelModel.get(0).getChannels_url();
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


    private void deleteFavorate(LatestChannelModel listOfAllLatestChannels) {
        RealamDatabase.getInstance().deleteRowInMyFavorate(listOfAllLatestChannels.getChannels_id());
    }

    private void saveToFavorate(LatestChannelModel listOfAllLatestChannels) {
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
        if (myFavoritemodel.size() > 0) {
            if (myFavoritemodel.get(0).isFavorite()) {
                a = 1;
                fab.setImageResource(R.drawable.ic_favorite);
            } else {
                a = 0;
                fab.setImageResource(R.drawable.ic_un_favorite);
            }
        } else {
            a = 0;
            fab.setImageResource(R.drawable.ic_un_favorite);
        }
    }

    private void setData() {

        Glide.with(NotificationDataActivity.this).load(InnerChannelModel.get(0).getChannels_image())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(main_imageview_placeholder);
        title_text.setText(InnerChannelModel.get(0).getChannels_name());
        date_text.setText(InnerChannelModel.get(0).getCreated_at());
        description_text.setText(InnerChannelModel.get(0).getChannel_description());
    }

    private void intiView() {
        fab = findViewById(R.id.fab);
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
        startActivity(new Intent(NotificationDataActivity.this, MainActivity.class));
        finish();
    }
}
