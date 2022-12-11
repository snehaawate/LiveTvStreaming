package com.sneha.livestreaming.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


import com.sneha.livestreaming.R;
import com.sneha.livestreaming.video_integration.ExoPlayerManager;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.regex.Pattern;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YouTubeUriExtractor;
import at.huber.youtubeExtractor.YtFile;

import static io.reactivex.plugins.RxJavaPlugins.onError;

public class PlayFullScreenVideoActivity extends ParentActivity {

    private final String STATE_RESUME_WINDOW = "resumeWindow";
    private final String STATE_RESUME_POSITION = "resumePosition";
    private final String STATE_PLAYER_FULLSCREEN = "playerFullscreen";
    private ProgressBar progressBar;
    private SimpleExoPlayerView mExoPlayerView;
    private MediaSource mVideoSource;
    private boolean mExoPlayerFullscreen = false;
    private FrameLayout mFullScreenButton;
    private ImageView mFullScreenIcon;
    private Dialog mFullScreenDialog;

    private int mResumeWindow;
    private long mResumePosition;
    private String downloadedUrl;
    private SimpleExoPlayer mResumePlayer;
    private boolean playerResumed;
    String video_link;
    boolean isClickable = false;
    TextView video_error_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_full_screen_video);
        video_error_tv=findViewById(R.id.video_error_tv);
        mExoPlayerView=null;
         video_link=getIntent().getStringExtra("video_link");
         isClickable=false;
       /* if (savedInstanceState != null) {
            mResumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
            mResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
            mExoPlayerFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN);
        }*/
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putInt(STATE_RESUME_WINDOW, mResumeWindow);
        outState.putLong(STATE_RESUME_POSITION, mResumePosition);
        outState.putBoolean(STATE_PLAYER_FULLSCREEN, mExoPlayerFullscreen);
        super.onSaveInstanceState(outState);
    }
    public void back(View view){
        if (mExoPlayerView != null && mExoPlayerView.getPlayer() != null) {
            mResumeWindow = mExoPlayerView.getPlayer().getCurrentWindowIndex();
            mResumePosition = Math.max(0, mExoPlayerView.getPlayer().getContentPosition());
            mExoPlayerView.getPlayer().setPlayWhenReady(false);
           // mExoPlayerView.getPlayer().release();
        }

        if (mFullScreenDialog != null)
            mFullScreenDialog.dismiss();

        finish();
    }

    @Override
    public void onBackPressed() {

        if (isClickable) {

            super.onBackPressed();
            back(null);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {

        super.onResume();
        ImageView back = findViewById(R.id.back);
        if (mExoPlayerView == null) {

            mExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.exoplayer);
            mExoPlayerView =  findViewById(R.id.exoplayer);
            progressBar = (ProgressBar) findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.VISIBLE);
            mExoPlayerView.setVisibility(View.GONE);
            isClickable=false;
            back.setClickable(false);
           // initFullscreenDialog();
           // initFullscreenButton();


           /* String userAgent = Util.getUserAgent(mContext, getApplicationContext().getApplicationInfo().packageName);
            DefaultHttpDataSourceFactory httpDataSourceFactory = new DefaultHttpDataSourceFactory(userAgent, null, DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS, DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS, true);
            DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(mContext, null, httpDataSourceFactory);
            Uri daUri = Uri.parse(URL);

            mVideoSource = new HlsMediaSource(daUri, dataSourceFactory, 1, null, null);*/
        }else{


         //   mExoPlayerView.getPlayer().setPlayWhenReady(true);
            return;
        }
        initFullscreenDialog();
        initFullscreenButton();

        extractYoutubeUrl();
       // mExoPlayerView.getPlayer().isLoading();





      //  initExoPlayer();

        if (mExoPlayerFullscreen) {
            ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
            mFullScreenDialog.addContentView(mExoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_fullscreen_skrink));
            mFullScreenDialog.show();
        }
    }


    @Override
    protected void onPause() {

        super.onPause();
        //finish();
        if (mExoPlayerView != null && mExoPlayerView.getPlayer() != null) {
            mResumeWindow = mExoPlayerView.getPlayer().getCurrentWindowIndex();
            mResumePosition = Math.max(0, mExoPlayerView.getPlayer().getContentPosition());
             mResumePlayer= (SimpleExoPlayer) mExoPlayerView.getPlayer();
            //mExoPlayerView.getPlayer().release();
             mExoPlayerView.getPlayer().setPlayWhenReady(false);
            playerResumed=true;


        }

       /* if (mFullScreenDialog != null)
            mFullScreenDialog.dismiss();*/
    }


    private void initFullscreenDialog() {

        mFullScreenDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (mExoPlayerFullscreen)
                    closeFullscreenDialog();
                super.onBackPressed();
            }
        };
    }


    private void openFullscreenDialog() {

        ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
        mFullScreenDialog.addContentView(mExoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_fullscreen_skrink));
        mExoPlayerFullscreen = true;
        mFullScreenDialog.show();
    }


    private void closeFullscreenDialog() {

        ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
        ((FrameLayout) findViewById(R.id.main_media_frame)).addView(mExoPlayerView);
        mExoPlayerFullscreen = false;
        mFullScreenDialog.dismiss();
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_fullscreen_expand));
    }


    private void initFullscreenButton() {

        PlaybackControlView controlView = mExoPlayerView.findViewById(R.id.exo_controller);
        mFullScreenIcon = controlView.findViewById(R.id.exo_fullscreen_icon);
        mFullScreenButton = controlView.findViewById(R.id.exo_fullscreen_button);
        mFullScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mExoPlayerFullscreen)
                    openFullscreenDialog();
                else
                    closeFullscreenDialog();
            }
        });
    }


    private void initExoPlayer() {

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        LoadControl loadControl = new DefaultLoadControl();
        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(this), trackSelector, loadControl);
        mExoPlayerView.setPlayer(player);

        boolean haveResumePosition = mResumeWindow != C.INDEX_UNSET;

        if (haveResumePosition) {
            mExoPlayerView.getPlayer().seekTo(mResumeWindow, mResumePosition);
        }
        mExoPlayerView.setPlayer(ExoPlayerManager.getSharedInstance(mContext).getPlayerView().getPlayer());
       // mExoPlayerView.getPlayer().prepare(mVideoSource);
        mExoPlayerView.getPlayer().setPlayWhenReady(true);
    }

    private void extractYoutubeUrl() {
       /* @SuppressLint("StaticFieldLeak")
        YouTubeExtractor mExtractor = new YouTubeExtractor(mContext) {
            @Override
            protected void onExtractionComplete(SparseArray<YtFile> sparseArray, VideoMeta videoMeta) {
                for(int i = 0, nsize = sparseArray.size(); i < nsize; i++) {
                    Object obj = sparseArray.valueAt(i);
                }
                if (sparseArray != null && sparseArray.get(18)!=null) {

                    playVideo(sparseArray.get(18).getUrl());
                }else{
                   // AppLog.getInstance().printToast(mContext,"Error occured!");
                    Toast.makeText(PlayFullScreenVideoActivity.this, "Video not playing", Toast.LENGTH_LONG).show();
                    video_error_tv.setText("We are unable to play this video due to some security reasons.");
                    isClickable=true;
                    back(null);
                }


               //////////////////////////new starts below

            }
        };
        mExtractor.setParseDashManifest(true);
        mExtractor.extract(video_link, true, true);*/
       // mExtractor.extract("https://www.youtube.com/watch?v=eEMwR2EVQvo", true, true);

////////////////////////////new one   /////////////////////////
        @SuppressLint("StaticFieldLeak") YouTubeUriExtractor ytEx = new YouTubeUriExtractor(mContext) {
            @Override
            public void onUrisAvailable(String videoId, String videoTitle, SparseArray<YtFile> ytFiles) {
                if (ytFiles != null) {
                    int itag = 18;
                    String downloadUrl = ytFiles.get(itag).getUrl();

                    Log.d("YTasdasd", "down_yt = " + downloadUrl);

                    playVideo(ytFiles.get(18).getUrl());
                }else {
                    // AppLog.getInstance().printToast(mContext,"Error occured!");
                    Toast.makeText(PlayFullScreenVideoActivity.this, "Video not playing", Toast.LENGTH_LONG).show();
                    video_error_tv.setText("We are unable to play this video due to some security reasons.");
                    isClickable=true;
                    back(null);
                }
            }
        };
        ytEx.setParseDashManifest(true);
        ytEx.setIncludeWebM(true);
        ytEx.execute(video_link);

    }
    private void playVideo(String downloadUrl) {
        if(downloadUrl==null) {
            //AppLog.getInstance().printToast(mContext,"Video not loaded");
            return;
        }
        final ImageView back = findViewById(R.id.back);
            mExoPlayerView.setPlayer(ExoPlayerManager.getSharedInstance(mContext).getPlayerView().getPlayer());
            ExoPlayerManager.getSharedInstance(mContext).playStream(downloadUrl);
         progressBar.setVisibility(View.GONE);

        mExoPlayerView.setVisibility(View.VISIBLE);

        isClickable=true;
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               back(null);
            }
        });

     //   mResumePlayer.setPlayWhenReady(true);
     //   mExoPlayerView.setPlayer(mResumePlayer);
        //boolean haveResumePosition = mResumeWindow != C.INDEX_UNSET;
       // if (haveResumePosition) {
          //  mExoPlayerView.getPlayer().seekTo(mResumeWindow, mResumePosition);
      //  }

    }


    @Override
    public void onActivityTransfer() {
        
    }


}
