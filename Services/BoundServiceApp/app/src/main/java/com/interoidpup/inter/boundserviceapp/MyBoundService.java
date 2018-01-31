package com.interoidpup.inter.boundserviceapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.UUID;

/**
 * Created by bakdag on 31.01.2018.
 */

public class MyBoundService extends Service {
    // Manifeste eklemeyi unutma.
    // UI kitlenir. Çünkü Main Threadde çalışır.
    boolean isContinue = true;
    private String TAG = "MyBoundService";

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        isContinue = false;
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");

        new Thread(new Runnable() {
            @Override
            public void run() {
                //farklı bir thread' e taşıdık.
                while (isContinue){
                    String randomStr = UUID.randomUUID().toString();
                    Log.i(TAG, randomStr);
                }
            }
        });
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent ıntent) {
        Log.i(TAG, "onBind");
        return null;
    }
}
