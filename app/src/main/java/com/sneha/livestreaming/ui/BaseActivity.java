package com.sneha.livestreaming.ui;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;
import com.sneha.livestreaming.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class BaseActivity extends AppCompatActivity {

    int backCount = 4;
    static int backPressed = 0;
    static InterstitialAd interstitialAd;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void showInterstitialAds() {
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        } else {
            Toast.makeText(getApplicationContext(),"The interstitial wasn't loaded yet.",Toast.LENGTH_LONG).show();
        }
    }


    public void getBackEvet() {
//        backPressed++;
//        if (backPressed == backCount) {
//            backPressed = 0;
//            showInterstitialAds();
//        }else if(backPressed == 1){
//            prepareAd();
//        }
    }

    private void prepareAd() {

        interstitialAd = new InterstitialAd(getApplicationContext());
        interstitialAd.setAdUnitId(getApplicationContext().getResources().getString(R.string.Interstitial_unit_id));

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("143EE1BB7FC33C095EAC4458950DB8E5").build();

        // Load the interstitial ad.
        interstitialAd.loadAd(adRequest);

    }


}
