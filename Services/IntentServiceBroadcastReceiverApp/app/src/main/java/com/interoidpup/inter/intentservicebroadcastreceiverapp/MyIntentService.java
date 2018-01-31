package com.interoidpup.inter.intentservicebroadcastreceiverapp;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by bakdag on 31.01.2018.
 */

public class MyIntentService extends IntentService {

    private String TAG = "MyIntentService";

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate çalıştı");
        super.onCreate();
    }

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent ıntent) {
        Log.i(TAG, "onHandleIntent çalıştı.");
        //Bu metod main thread dışında bir thread tarafından çalıştırılır.

        //15 sn lik bir işlem gerçekleştirip bitiminde Activity' de bittiğine dair toas mesajı basalım.
        try {
            Thread.sleep(15*1000);
            Intent broadcastIntent = new Intent("show.message.action");
            broadcastIntent.putExtra("data", "Tamamlandı");
            // Bu action' a sahip broadcastreceiver' ın onReceive() metoduna düşer.
            sendBroadcast(broadcastIntent);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy çalıştı");
        super.onDestroy();
    }
}
