package com.bsrakdg.com.senderapp;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import java.util.UUID;

public class MyService extends Service {

    private String TAG = "MyService";
    private boolean isContinue = true;
    private String randomStr = "";

    public MyService() {
    }

    // processler arası iletişimi Messanger sağlar. Messenger parametre olarak, ya handler ya da ibender alır.
    // İsteği karşılayan da Handler. Custom Handler classı oluşturup parametre olarak Messenger' a verdik.
    private Messenger messenger = new Messenger(new SupplyClaimHandler());

    @Override
    public IBinder onBind(Intent intent) {
        //Messenger, IBinder' ı sarmalayan bir yapıdır.
        return messenger.getBinder();
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

    public String getRandomStr(){
        return randomStr;
    }

    @Override
    public void onDestroy() {
        isContinue = false;
        Log.e(TAG, "onDestroy");
        super.onDestroy();
    }

    // farklı processten gelen isteği alıp cevaplayacak.
    class SupplyClaimHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {

            //gelen msg' yi cevap vermek için kullanıyorum
            //öncelikle göndereceğim message' ı hazırlıyorum
            Message message = Message.obtain(null, 1); //1, resultCode gibi düşün.
            Bundle bundle = new Bundle();
            bundle.putString("randomString", getRandomStr());
            message.setData(bundle);
            
            //gelene cevap ver
            try {
                msg.replyTo.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
