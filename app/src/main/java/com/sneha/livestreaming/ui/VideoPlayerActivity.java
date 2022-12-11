package com.sneha.livestreaming.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sneha.livestreaming.R;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.TrackSelectionView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;


import static android.view.View.VISIBLE;

public class VideoPlayerActivity extends AppCompatActivity implements  View.OnClickListener{

    LinearLayout root;
    PlayerView playerView;
    ProgressBar progress_bar;
        SimpleExoPlayer player;
    boolean shouldAutoPlay = true;
    DefaultTrackSelector trackSelector;
    TrackGroupArray lastSeenTrackGroupArray;
    DataSource.Factory mediaDataSourceFactory;
    BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
    boolean playWhenReady = false;
    int currentWindow = 0;
    long playbackPosition = 0L;
    ImageView ivHideControllerButton, ivSettings;
    MappingTrackSelector.MappedTrackInfo mappedTrackInfo;


    private static final String KEY_PLAY_WHEN_READY = "play_when_ready";
    private static final String KEY_WINDOW = "window";
    private static final String KEY_POSITION = "position";
    private Intent intent;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        initView();
        intent = getIntent();
        if(intent != null){
            if(intent.hasExtra("ID")){
                url = intent.getStringExtra("ID");
            }else{
                finish();
            }
        }

        if (savedInstanceState != null) {
            playWhenReady = savedInstanceState.getBoolean(KEY_PLAY_WHEN_READY);
            currentWindow = savedInstanceState.getInt(KEY_WINDOW);
            playbackPosition = savedInstanceState.getLong(KEY_POSITION);
        }
        shouldAutoPlay = true;
        mediaDataSourceFactory = new DefaultDataSourceFactory(this,Util.getUserAgent(this, "mediaPlayerSample"),(TransferListener<DataSource>)bandwidthMeter);
    }

    private void initView() {
        root = findViewById(R.id.root);
        playerView = findViewById(R.id.player_view);
        progress_bar = findViewById(R.id.progress_bar);
        ivHideControllerButton = findViewById(R.id.exo_controller1);
       ivSettings = findViewById(R.id.settings);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23)
            initializePlayer();
    }



    @Override
    protected void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || player == null)
            initializePlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) releasePlayer();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) releasePlayer();
    }

    @Override
    public void onClick(View v) {
      if(v.getId() == R.id.settings){
            mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
            if(mappedTrackInfo != null){

                String title = getString(R.string.video);
                int rendererIndex = (int)ivSettings.getTag();
                Pair<AlertDialog, TrackSelectionView> dialogPair = TrackSelectionView.getDialog(this, title, trackSelector, rendererIndex);
                dialogPair.second.setShowDisableOption(false);
                dialogPair.second.setAllowAdaptiveSelections(true);
                dialogPair.first.show();
            }
       }

   }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(KEY_PLAY_WHEN_READY, playWhenReady);
        savedInstanceState.putInt(KEY_WINDOW, currentWindow);
        savedInstanceState.putLong(KEY_POSITION, playbackPosition);
    }


    private void initializePlayer() {
        playerView.requestFocus();
        AdaptiveTrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        lastSeenTrackGroupArray = null;
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
        playerView.setPlayer(player);
        player.addListener((Player.EventListener) (new VideoPlayerActivity.PlayerEventListener()));
        player.setPlayWhenReady(shouldAutoPlay);
        MediaSource mediaSource = new HlsMediaSource.Factory(mediaDataSourceFactory).createMediaSource(Uri.parse(url));

        boolean haveStartPosition = currentWindow != C.INDEX_UNSET;

        if (haveStartPosition) {
            if(player != null){
                player.seekTo(currentWindow, playbackPosition);
            }
        }
        player.prepare(mediaSource, !haveStartPosition, false);
        updateButtonVisibilities();
        ivHideControllerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerView.hideController();
            }
        });
    }


    private void releasePlayer() {
        if (player != null) {
            updateStartPosition();
            shouldAutoPlay = player.getPlayWhenReady();
            player.release();
            player = null;
            trackSelector = null;
        }
    }


    private void updateStartPosition() {
        if(player != null){
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.setPlayWhenReady(player.getPlayWhenReady());
        }
    }


    private final void updateButtonVisibilities() {
        ivSettings.setVisibility(View.GONE);
        if (player == null) {
            return;
        }

        MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();

        if(mappedTrackInfo != null)
        for(int i=0;i<mappedTrackInfo.getRendererCount();i++){
            TrackGroupArray trackGroups = mappedTrackInfo.getTrackGroups(i);
            if (trackGroups.length != 0) {
                if (player.getRendererType(i) == C.TRACK_TYPE_VIDEO) {
                    ivSettings.setVisibility(VISIBLE);
                    ivSettings.setOnClickListener(this);
                    ivSettings.setTag(i);
                }
            }
        }
    }

    private class PlayerEventListener extends Player.DefaultEventListener {

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            switch (playbackState){
                case  Player.STATE_IDLE:
                    progress_bar.setVisibility(View.VISIBLE);
                    break;
                case  Player.STATE_BUFFERING:
                    progress_bar.setVisibility(View.VISIBLE);
                    break;
                case  Player.STATE_READY:
                    progress_bar.setVisibility(View.GONE);
                    break;
                case  Player.STATE_ENDED:
                    progress_bar.setVisibility(View.GONE);
                    break;


            }
            updateButtonVisibilities();
        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            updateButtonVisibilities();
            if(trackGroups != lastSeenTrackGroupArray){
                MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
                if (mappedTrackInfo != null) {
                    if (mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_VIDEO) == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                        Toast.makeText(VideoPlayerActivity.this , "Error unsupported track", Toast.LENGTH_SHORT).show();
                    }
                }
                lastSeenTrackGroupArray = trackGroups;
            }
        }
    }
}
