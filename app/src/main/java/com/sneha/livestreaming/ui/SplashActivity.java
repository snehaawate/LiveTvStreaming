package com.sneha.livestreaming.ui;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.sneha.livestreaming.OnSuccessFullyFetch;
import com.sneha.livestreaming.R;
import com.sneha.livestreaming.controller.ApiClient;
import com.sneha.livestreaming.controller.ApiService;
import com.sneha.livestreaming.database.RealamDatabase;
import com.sneha.livestreaming.model.CommonModel;
import com.sneha.livestreaming.utility.GetFullApiData;
import com.sneha.livestreaming.utility.PreferenceHandler;
import com.sneha.livestreaming.utility.Utility;
import com.google.firebase.iid.FirebaseInstanceId;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class SplashActivity extends AppCompatActivity {

    ApiService apiService;
    LinearLayout internetNotAvailable;
    Button tryAgain;
    Intent intent;
    String Id;
    private CompositeDisposable disposable = new CompositeDisposable();
    private String token;
    private Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("id")) {
                Id = intent.getStringExtra("id");
            }
        }

        internetNotAvailable = findViewById(R.id.internetNotAvailable);
        tryAgain = findViewById(R.id.tryAgain);
        apiService = ApiClient.getClient(SplashActivity.this)
                .create(ApiService.class);
        checkConnection();

        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
            }
        });

    }

    private void checkConnection() {
        if (Utility.isInternetConnection(SplashActivity.this)) {
            internetNotAvailable.setVisibility(View.GONE);
            getFireBaseTokan();

        } else {
            internetNotAvailable.setVisibility(View.VISIBLE);
        }
    }

    private void getFireBaseTokan() {

        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 2sec
                    try {
                        token = FirebaseInstanceId.getInstance().getToken();

                        if (token != null) {
                           Log.d("TOKEN1", token);
                            if(PreferenceHandler.readFCM_KEY(SplashActivity.this, PreferenceHandler.FCM_TOKEN, "") == null) {
                                PreferenceHandler.writeFCM_KEY(SplashActivity.this, PreferenceHandler.FCM_TOKEN, token);
                                updateToken(token);
                           }
                        } else {
                            token = FirebaseInstanceId.getInstance().getToken();

                            if (token != null) {
                                  Log.e("TOKEN2", token);
                                if(PreferenceHandler.readFCM_KEY(SplashActivity.this, PreferenceHandler.FCM_TOKEN, "") == null) {
                                    PreferenceHandler.writeFCM_KEY(SplashActivity.this, PreferenceHandler.FCM_TOKEN, token);
                                    updateToken(token);
                                }
                            } else {
                                PreferenceHandler.writeFCM_KEY(SplashActivity.this, PreferenceHandler.FCM_TOKEN, PreferenceHandler.readFCM_KEY(SplashActivity.this, PreferenceHandler.FCM_TOKEN, ""));
                                Log.e("TOKEN3", token);
                                updateToken(token);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }
            }, 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        handler.postDelayed(runnable, 1000);

    }

    private void updateToken(String token) {
        ApiService apiService = ApiClient.getClient(getApplicationContext())
                .create(ApiService.class);
        disposable.add(apiService.sendToken(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<CommonModel>() {
                    @Override
                    public void onSuccess(CommonModel commonModel) {
                        // Received data from the backend is token reached or not
                        Log.e("Tag","-----------" + commonModel.getMessage());
                    }

                    @Override
                    public void onError(Throwable e) {
                        // Network error
                    }
                }));
    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            RealamDatabase.getInstance().resetRealm();

            getAllData();
        }
    };
    /**
     * here we get data from api
     * and stored inside the
     */
    private void getAllData() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e("Tag","refreshedToken"+refreshedToken);
        new GetFullApiData(SplashActivity.this, new OnSuccessFullyFetch() {
            @Override
            public void onSuccess() {
                checkTheConditions();
            }
        });
    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
    }


    private void checkTheConditions() {
        if (Id != null && Id.length() > 0) {
            startActivity(new Intent(SplashActivity.this, NotificationDataActivity.class).putExtra("id", Id));
            finish();
        } else {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            handler.removeCallbacks(runnable);
        }catch (Exception e){
            e.printStackTrace();
        }

    }


}
