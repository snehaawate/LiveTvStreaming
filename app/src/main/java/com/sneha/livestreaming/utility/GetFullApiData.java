package com.sneha.livestreaming.utility;

import android.content.Context;

import com.sneha.livestreaming.OnSuccessFullyFetch;
import com.sneha.livestreaming.controller.ApiClient;
import com.sneha.livestreaming.controller.ApiService;
import com.sneha.livestreaming.database.RealamDatabase;
import com.sneha.livestreaming.model.AllChannedlModel;
import com.sneha.livestreaming.model.AllDataModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class GetFullApiData   {

    private OnSuccessFullyFetch onSuccessFullyFetch;
    private Context activity;
    private ApiService apiService;


    public GetFullApiData(Context activity, OnSuccessFullyFetch onSuccessFullyFetch){
        this.onSuccessFullyFetch = onSuccessFullyFetch;
        this.activity = activity;
          apiService = ApiClient.getClient(activity)
                .create(ApiService.class);
        getAppTheData();
    }

    // this method is used o get all the data once from the api
    // OnSuccessFullyFetch listener gives the callback
    // when the data is finished and nothing more to fetch data
    private void getAppTheData(){

        apiService.getAllData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<AllDataModel>() {
                    @Override
                    public void onSuccess(AllDataModel allDataModel) {
                        // Received data from the backend is token reached or not
                        RealamDatabase.getInstance().saveAllcategoryModel(allDataModel);
                        RealamDatabase.getInstance().saveAllLatestChannelModel(allDataModel);
                        getDataOfAllChannels();
                    }

                    @Override
                    public void onError(Throwable e) {
                        // Network error
                    }
                });

    }

    private void getDataOfAllChannels() {
        apiService.getAllChannelData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<AllChannedlModel>() {
                    @Override
                    public void onSuccess(AllChannedlModel allChannedlModel) {
                        // Received data from the backend is token reached or not
                        RealamDatabase.getInstance().saveAllChannelModel(allChannedlModel);
                        onSuccessFullyFetch.onSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        // Network error
                    }
                });
    }


}
