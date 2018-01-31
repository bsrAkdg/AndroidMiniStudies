package com.interoidpup.inter.intentserviceandresultreceiverapp;

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
        //Servisi başlatan activity bize UI ile iletişimi sağlayacak, Parcable' i implement eden ResultReceiver nesnesini gönderir.
        ResultReceiver resultReceiver = ıntent.getParcelableExtra("myResultReceiver");
        //15 sn lik bir işlem gerçekleştirip bitiminde Activity' de bittiğine dair toas mesajı basalım.
        try {
            Thread.sleep(15*1000);
            //ResultReceiver ile Activity arasında iletişimi bundle ile sağlarız.
            Bundle bundle = new Bundle();
            bundle.putString("data", "Tamamlandı");
            //resultCode ve bundle' ı gönderdik.
            resultReceiver.send(100, bundle);
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
