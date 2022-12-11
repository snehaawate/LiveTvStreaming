package com.sneha.livestreaming.video_integration;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;

public class StatusBarUtil {

    private StatusBarUtil() {}

    public static void hide(Activity activity) {
        if (Build.VERSION.SDK_INT < 16) {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = activity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
}
