package com.interoidpup.inter.intentservicebroadcastreceiverapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Broadcastreceiver, resultreceiver' a göre uygulması daha basit.
    // Broadcastreceiver uygulama içi yada uygulamalar arası iletişimi sağlar.
    // dinamik bir broadcastreceiver oluşturacağımız için onResume()' da register edip, onPause()' da unregister edeceğiz.
    //servisi manifeste eklemeyi unutma!

    Button btnStartService;
    private String TAG = "MainActivity";

    void init(){
        btnStartService = (Button) findViewById(R.id.btnStartService);
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
        //intent.putExtra("loopCount", 5); gibi servise göndermek istediğiniz değerleri gönderebilirsiniz.
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"onResume çalıştı");

        //bir broadcasti dinamik olarak ekleyeceğimiz zaman intentfilter ile action ekleriz.
        IntentFilter intentFilter = new IntentFilter("show.message.action");
        registerReceiver(myBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG,"onPause çalıştı");

        unregisterReceiver(myBroadcastReceiver);
    }

    private BroadcastReceiver myBroadcastReceiver = new BroadcastReceiver() {
        private String TAG = "myBroadcastReceiver";
        @Override
        public void onReceive(Context context, Intent ıntent) {
            Log.i(TAG,"onReceive çalıştı");

            //servistek tetiklendiği an buraya düşecek!
            Toast.makeText(MainActivity.this, ıntent.getStringExtra("data"), Toast.LENGTH_LONG).show();
        }
    };
}
