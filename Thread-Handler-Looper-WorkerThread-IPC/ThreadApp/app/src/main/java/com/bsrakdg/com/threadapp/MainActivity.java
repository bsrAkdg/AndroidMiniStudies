package com.bsrakdg.com.threadapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    //UI' yı(main thread), uzun sürecek bir döngü ile kitliyoruz. Bu yüzden ekrandaki hiçbir viewe erişelemiyor.
    //çözüm :

    private String TAG = "MainActivity";
    Button btnStart, btnStop;
    int loopCount = 0;
    boolean isExecute = true;

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
    }

    void startLoop (){
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

    void stopLoop(){
        isExecute= false;
    }
}
