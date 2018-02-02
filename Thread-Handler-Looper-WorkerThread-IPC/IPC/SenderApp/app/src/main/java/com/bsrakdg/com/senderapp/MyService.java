package com.bsrakdg.com.senderapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.UUID;

public class MyService extends Service {

    private String TAG = "MyService";
    private boolean isContinue = true;
    String randomStr = "";

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        isContinue = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                //durdurana kadar random string üretsin
                while (isContinue){
                    createRandomString(); //main threadde çalışır. UI etkileşimini engellememek için new thread içine al.
                }
            }
        }).start();

        return START_REDELIVER_INTENT; //Servis kapandığında onu çağıran bir intent ile beraber onu tekrar tekrar çağırran bir flag
    }

    void createRandomString(){
        try{
            Thread.sleep(1000); //sonucu daha keskin görmek için
            randomStr = UUID.randomUUID().toString();
            Log.e(TAG, "Üretilen String : " + randomStr + " Thread adı : " + Thread.currentThread().getName());
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        isContinue = false;
        Log.e(TAG, "onDestroy");
        super.onDestroy();
    }

}
