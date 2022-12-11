package com.sneha.livestreaming.utility.fcm;

import android.util.Log;

import com.sneha.livestreaming.controller.ApiClient;
import com.sneha.livestreaming.controller.ApiService;
import com.sneha.livestreaming.model.CommonModel;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

//Class extending FirebaseInstanceIdService
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    private CompositeDisposable disposable = new CompositeDisposable();
    @Override
    public void onTokenRefresh() {

        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        updateToken(refreshedToken);
    }

    private void updateToken(String refreshedToken) {
        ApiService apiService = ApiClient.getClient(getApplicationContext())
                .create(ApiService.class);
        disposable.add(apiService.sendToken(refreshedToken)
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

    @Override
    public void onDestroy() {
        disposable.dispose();
        super.onDestroy();

    }
}