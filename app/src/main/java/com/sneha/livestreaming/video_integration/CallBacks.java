package com.sneha.livestreaming.video_integration;

public interface CallBacks {

    void callbackObserver(Object obj);

    public interface playerCallBack {
        void onItemClickOnItem(Integer albumId);

        void onPlayingEnd();
    }
}
