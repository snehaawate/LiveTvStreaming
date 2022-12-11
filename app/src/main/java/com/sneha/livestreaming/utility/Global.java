package com.sneha.livestreaming.utility;

import android.content.Context;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class Global extends MultiDexApplication {
    private static Context mContext;
    private static Context context;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Global.context = getApplicationContext();
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("database_Live_Tv_Streaming")
                .schemaVersion(0)
                .build();
        Realm.setDefaultConfiguration(realmConfig);



    }

    public Context getApplicationClass() {
        if(mContext == null){
            mContext = getApplicationContext();
            return mContext;
        }
        return mContext;
    }
    public static Context getAppContext() {
        return Global.context;
    }
}
