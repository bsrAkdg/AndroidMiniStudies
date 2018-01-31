package com.interoidpup.inter.intentserviceandresultreceiverapp;

import android.content.Intent;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    // Uzun sürecek olan işlemler her zaman main thread dışında bir thread ile yürütülür. Yoksa UI kitlenir.
    // Uzun süren işlemleri iki şekilde yapabiliriz:
    // 1. Service içinde Async çağırmak.
    // 2 IntentService kullanmak.
    // Servisler UI ile etkileşimli olamadıkları için bunu iki şekilde sağlarız:
    // IntentService ile ResultReceiver kullanırız. Ya da IntentService ile BroadcastReceiver kullanırız.
    // **servisi manifeste eklemeyi unutma!
    Button btnStartService;
    Handler handler = new Handler(); //İşlemler bir kuyruğa alındığı için o kuyrukla main thread arasında veri alışverişi sağlar.

    void init(){
        btnStartService = (Button)findViewById(R.id.btnStartService);
        btnStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService();
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    //butona kaç kez basarsan o kadar bir kuyruk oluşur sırayla servisler gerçekleşir.
    void startService(){
        //servisi başlatıyoruz.
        Intent intent = new Intent(MainActivity.this, MyIntentService.class);
        //servise göndermek istediğimiz değerleri ve oluşturduğumuz receiver' ı gödneriyoruz.
        ResultReceiver resultReceiver = new MyResultReceiver(null);
        intent.putExtra("myResultReceiver", resultReceiver);
        //intent.putExtra("loopCount", 5);
        startService(intent);
    }

    //ResultReceiverı oluşturalım
    public class MyResultReceiver extends ResultReceiver{

        private String TAG = "MyResultReceiver";

        //Handler
        public MyResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(final int resultCode, final Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            //
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "onReceiveResult çalıştı.");
                    //Artık UI' da bir şey gösterebilir, değiştirebiliriz.
                    Toast.makeText(MainActivity.this, resultData.getString("data"), Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
