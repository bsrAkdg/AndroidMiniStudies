package com.bsrakdg.com.threadapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //UI' yı(main thread), uzun sürecek bir döngü ile kitliyoruz. Bu yüzden ekrandaki hiçbir viewe erişelemiyor.
    //çözüm 1 : custom thread ile farklı thread üzerine taşımak :  MyThread class türünde nesneyi onCreate() de oluşturup başlayacağı zaman o thread.start() diyerek çağırmak
    //çözüm 2 new thread ile farklı thread üzerine taşımak
    private String TAG = "MainActivity";
    Button btnStart, btnStop;
    TextView txtLoop;
    int loopCount = 0;
    boolean isExecute = true;
    Thread thread;

    void init(){
        btnStart = (Button) findViewById(R.id.btnStart);
        btnStop = (Button) findViewById(R.id.btnStop);
        txtLoop = (TextView)findViewById(R.id.txtLoop);

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

        /* TODO çözüm 1
        MyThread myThread = new MyThread();
        thread = new Thread(myThread);*/

    }
    void startLoop (){
        /* TODO çözüm 1
        thread.start(); // MyThread classında run() metodu çalışır.*/

        // TODO çözüm 2
        new Thread(new Runnable() {
            @Override
            public void run() {
                isExecute= true;
                while (isExecute){
                    try {
                        Thread.sleep(1000);
                        Log.e(TAG, "Döndü sayısı : " + loopCount + " Thread : " + Thread.currentThread().getName());
                        loopCount++;
                        //todo bir worker threadden main thread' e erişebilmek için üç yöntem var :
                        //bu threadden Toast mesajı basmak istersek UI thread' e erişmek için aşağıdakilerden bir tanesini kullanmak yeterli.
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, ""+ loopCount, Toast.LENGTH_LONG).show();
                            }
                        });
                        //bu threadden UI daki textviewi güncellemek istersek
                        txtLoop.post(new Runnable() {
                            @Override
                            public void run() {
                                //runnable nesnesiyle main threade erişebilirsin.
                                txtLoop.setText(""+loopCount);
                            }
                        });
                        //ilgili view öğesinin postDelay metodunuda kullanabiliriz:
                        txtLoop.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                            }
                        }, 5000); //5 sn sonra bu işlemleri yapacak.
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    void stopLoop(){
        isExecute= false;
    }

    //TODO çözüm 1 : thread içinde direk new Runnable demek yerine inner class oluşturuyoruz.
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
