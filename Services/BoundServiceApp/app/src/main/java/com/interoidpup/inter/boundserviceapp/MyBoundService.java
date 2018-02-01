package com.interoidpup.inter.boundserviceapp;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
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
    String randomStr = "";

    //onBind için inner class oluşturalım. Local : uygulama iç, Binder' den extends etmemizin sebebi
    // IBinder interface extends edemeyiz, onu implement eden sınıf Binder. IBinder -> Binder -> MyLocalBinder
    class MyLocalBinder extends Binder {
        //Activity' den service bağlanırken servise ihtiyacımız olacak.
        public MyBoundService getService(){
            return MyBoundService.this;
        }
    }
    //Yukarıda ürettiğimiz sınıf tipinde bir değişken oluşturalım.
    private IBinder myLocalBinder = new MyLocalBinder();

    @Nullable
    @Override //IBinder artık null değil flag dönecek.
    public IBinder onBind(Intent ıntent) {
        Log.e(TAG, "onBind");
        return myLocalBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG, "onUnbind");
        return false;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        isContinue = false;
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");

        //farklı threadlarda oluşacağı için UI kitlenmeyecek.
        new Thread(new Runnable() {
            @Override
            public void run() {
                createString();
            }
        }).start();
        return START_STICKY;
    }

    void createString(){
        while (isContinue){
            try{
                Thread.sleep(1000);
                randomStr = UUID.randomUUID().toString();
                Log.e(TAG, randomStr);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public String getString(){
        return randomStr;
    }
}
