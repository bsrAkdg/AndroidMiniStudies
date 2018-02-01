package com.bsrakdg.com.threadapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    //UI' yı(main thread), uzun sürecek bir döngü ile kitliyoruz. Bu yüzden ekrandaki hiçbir viewe erişelemiyor.
    //çözüm 1 : farklı thread üzerine taşımak :  MyThread class türünde nesneyi onCreate() de oluşturup başlayacağı zaman o thread.start() diyerek çağırmak

    private String TAG = "MainActivity";
    Button btnStart, btnStop;
    int loopCount = 0;
    boolean isExecute = true;
    Thread thread;

    void init(){
        btnStart = (Button) findViewById(R.id.btnStart);
        btnStop = (Button) findViewById(R.id.btnStop);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLoop();
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopLoop();
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        //uzun sürecek işlemi başka bir threade taşıdık.
        MyThread myThread = new MyThread();
        thread = new Thread(myThread);

        /* TODO diğer yöntem
        new Thread(new Runnable() {
            @Override
            public void run() {
                isExecute= true;
                while (isExecute){
                    try {
                        Thread.sleep(1000);
                        Log.e(TAG, "Döndü sayısı : " + loopCount + " Thread : " + Thread.currentThread().getName());
                        loopCount++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        */
    }
    void startLoop (){
        thread.start(); // MyThread classında run() metodu çalışır.
    }
    void stopLoop(){
        isExecute= false;
    }
    //thread içinde direk new Runnable demek yerine inner class oluşturuyoruz.
    public class MyThread implements Runnable{
        //buraya yazdığımız kodlar artık main threadde değil worker threadde çalışır.
        @Override
        public void run() {
            isExecute= true;
            while (isExecute){
                try {
                    Thread.sleep(1000);
                    Log.e(TAG, "Döndü sayısı : " + loopCount + " Thread : " + Thread.currentThread().getName());
                    loopCount++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
