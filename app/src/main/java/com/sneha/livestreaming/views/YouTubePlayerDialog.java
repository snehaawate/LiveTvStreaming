package com.sneha.livestreaming.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;


import com.sneha.livestreaming.R;
import com.sneha.livestreaming.databinding.YoutubePlayerDialogBinding;
import com.sneha.livestreaming.video_integration.ExoPlayerManager;
import com.sneha.livestreaming.video_integration.YouTubeVideoInfoRetriever;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import java.io.IOException;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;


public class YouTubePlayerDialog extends DialogFragment implements
        YouTubePlayer.OnInitializedListener, OnPreparedListener {

    public static final String URL = "https://www.youtube.com/watch?v=Ba3mHfRFoXg";
    public static final String VIDEO_ID = "A2eT9iLQyoU";
    public static final String VIDEO_URL = "https://archive.org/download/Popeye_forPresident/Popeye_forPresident_512kb.mp4";
    /*private GlobalConstant mConstant;
    private UserPreferences mPref;*/
    private Context mContext;
    YoutubePlayerDialogBinding mBinding;
    YouTubePlayerSupportFragment youTubePlayerFragment;
    YouTubePlayer mPlayer=null;
    SimpleExoPlayer player;
    PlayerView mPlayerView;
    Dialog mFullScreenDialog;

    public static YouTubePlayerDialog getInstance(){
        return new YouTubePlayerDialog();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  mContext=getContext();
        mContext=getActivity();

       /* mConstant=new GlobalConstant();
        mPref=new UserPreferences();
        mPref.  preferencesInitialize(mContext);*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding=  DataBindingUtil.inflate(
                inflater, R.layout.youtube_player_dialog, container, false);
        ////////////////////////////////
       /*  youTubePlayerFragment = (YouTubePlayerSupportFragment) getActivity().getSupportFragmentManager()
                 .findFragmentById(R.id.youtubeplayerfragment);
          youTubePlayerFragment.initialize(getResources().getString(R.string.youtube_api_key), this);*/

       // mBinding.videoView.setOnPreparedListener(this);
       // mBinding.videoView.setVideoURI(Uri.parse(URL  ));
        //////////////////////////////////////

        /**
         * Use full code
         */
        try {
           // extractYoutubeUrl();


        } catch (Exception e) {
            e.printStackTrace();
        }


        return mBinding.getRoot();
    }


    private void extractYoutubeUrl() {
        @SuppressLint("StaticFieldLeak") YouTubeExtractor mExtractor = new YouTubeExtractor(mContext) {
            @Override
            protected void onExtractionComplete(SparseArray<YtFile> sparseArray, VideoMeta videoMeta) {
                if (sparseArray != null) {
                    playVideo(sparseArray.get(17).getUrl());
                }
            }
        };
        mExtractor.extract(URL, true, true);
    }

    private void playVideo(String downloadUrl) {
         mPlayerView =mBinding.mPlayerView;
        mPlayerView.setPlayer(ExoPlayerManager.getSharedInstance(getActivity()).getPlayerView().getPlayer());
        ExoPlayerManager.getSharedInstance(getActivity()).playStream(downloadUrl);
    }
  private void initializePlayer() throws IOException {
        if (player == null) {
            player = ExoPlayerFactory.newSimpleInstance(
                   new DefaultRenderersFactory(mContext),
                   new DefaultTrackSelector(),
                    new DefaultLoadControl());

            mBinding.videoView.setPlayer(player);
            player.setPlayWhenReady(true);
            //player.seekTo(0, 0);
        }
        YouTubeVideoInfoRetriever retriever = new YouTubeVideoInfoRetriever();
        retriever.retrieve(VIDEO_ID);
        player.prepare(buildMediaSource(Uri.parse(retriever.getInfo(YouTubeVideoInfoRetriever.KEY_DASH_VIDEO))), true, false);

    }
    private MediaSource buildMediaSource(Uri uri)  {

        String userAgent = "exoplayer-codelab";
        if (uri.getLastPathSegment().contains("mp3") || uri.getLastPathSegment().contains("mp4")) {
            return new ExtractorMediaSource.Factory(new DefaultHttpDataSourceFactory(userAgent))
                    .createMediaSource(uri);
        }else {
            DefaultDashChunkSource.Factory dashChunkSourceFactory = new DefaultDashChunkSource.Factory(
                    new DefaultHttpDataSourceFactory(userAgent, new DefaultBandwidthMeter()));
            DefaultHttpDataSourceFactory manifestDataSourceFactory = new DefaultHttpDataSourceFactory(userAgent);
            return new DashMediaSource.Factory(dashChunkSourceFactory, manifestDataSourceFactory).createMediaSource(uri);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog=new  Dialog(getActivity(), getTheme()){
            @Override
            public void onBackPressed() {
                //do your stuff

                closeDialog();
            }
        };


        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            //  setStyle(R.style.popup_window_animation,getTheme());
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        }

        callSimpleExoPlayer();
    }



    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        getDialog().getWindow()
                .getAttributes().windowAnimations = R.style.popup_window_animation;
    }

    public void closeDialog(){
        dismiss();
   }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
       mPlayerView.getPlayer().stop();
        if(mPlayer!=null && mPlayer.isPlaying()) {
            mPlayer.pause();
            youTubePlayerFragment.onStop();
        }
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {

            // loadVideo() will auto play video
            // Use cueVideo() method, if you don't want to play it automatically

            youTubePlayer.loadVideo(URL);

            // Hiding player controls
           //  youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
            mPlayer=youTubePlayer;
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    @Override
    public void onPrepared() {
      //  mBinding.videoView.start();
    }

    //////////////////////////////////
    private MediaSource mVideoSource;
    boolean mExoPlayerFullscreen;
    private FrameLayout mFullScreenButton;
    private ImageView mFullScreenIcon;
    private void initFullscreenDialog() {

        mFullScreenDialog = new Dialog(mContext, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (mExoPlayerFullscreen)
                    closeFullscreenDialog();
                super.onBackPressed();
            }
        };
    }
    private void openFullscreenDialog() {

        ((ViewGroup) mBinding.exoplayer.getParent()).removeView(mBinding.exoplayer);
        mFullScreenDialog.addContentView(mBinding.exoplayer, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_fullscreen_skrink));
        mExoPlayerFullscreen = true;
        mFullScreenDialog.show();
    }
    private void closeFullscreenDialog() {

        ((ViewGroup) mBinding.exoplayer.getParent()).removeView(mBinding.exoplayer);
        mBinding.mainMediaFrame.addView(mBinding.exoplayer);
        mExoPlayerFullscreen = false;
        mFullScreenDialog.dismiss();
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_fullscreen_expand));
    }
    private void initFullscreenButton() {

        PlaybackControlView controlView = mBinding.exoplayer.findViewById(R.id.exo_controller);
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
    private void callSimpleExoPlayer() {
        if (mBinding.exoplayer == null) {
            initFullscreenDialog();
            initFullscreenButton();

            String streamUrl = "http://qthttp.apple.com.edgesuite.net/1010qwoeiuryfg/sl.m3u8";
            String userAgent = Util.getUserAgent(mContext, mContext.getApplicationInfo().packageName);
            DefaultHttpDataSourceFactory httpDataSourceFactory = new DefaultHttpDataSourceFactory(userAgent, null, DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS, DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS, true);
            DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(mContext, null, httpDataSourceFactory);
            Uri daUri = Uri.parse(streamUrl);

            mVideoSource = new HlsMediaSource(daUri, dataSourceFactory, 1, null, null);
        }

       // initExoPlayer();

        if (mExoPlayerFullscreen) {
            ((ViewGroup) mBinding.exoplayer.getParent()).removeView(mBinding.exoplayer);
            mFullScreenDialog.addContentView(mBinding.exoplayer, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_fullscreen_skrink));
            mFullScreenDialog.show();
        }
    }
    /*private void initExoPlayer() {

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        LoadControl loadControl = new DefaultLoadControl();
        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(mContext), trackSelector, loadControl);
        mBinding.exoplayer.setPlayer(player);

        boolean haveResumePosition = mResumeWindow != C.INDEX_UNSET;

        if (haveResumePosition) {
            mBinding.exoplayer.getPlayer().seekTo(mResumeWindow, mResumePosition);
        }

        mBinding.exoplayer.getPlayer().prepare(mVideoSource);
        mBinding.exoplayer.getPlayer().setPlayWhenReady(true);
    }*/


}
