package com.sneha.livestreaming.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sneha.livestreaming.callback.OnActivityTransferCallback;
import com.sneha.livestreaming.constant.GlobalConstant;
import com.google.android.gms.common.api.Api;


public abstract class ParentActivity extends AppCompatActivity implements OnActivityTransferCallback {

     protected Context mContext;
    protected GlobalConstant mConstant;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getApplicationContext();
        mConstant=new GlobalConstant();

    }



    /**
     * #Move the controll from between activities by passing destination activity name and bundle(optional) .
     * @param destination_address
     * @param bundle
     */
   public void moveTo(Class destination_address,Bundle bundle){
       Intent intent = new Intent(this,destination_address);
       intent.putExtra(mConstant.BUNDLE,bundle);
       startActivity(intent);
       onActivityTransfer();
   }

    public void moveTo(Intent intent,Bundle bundle){
        intent.putExtra(mConstant.BUNDLE,bundle);
        startActivity(intent);
        onActivityTransfer();
    }

}
