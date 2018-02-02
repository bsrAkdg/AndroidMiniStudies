package com.bsrakdg.com.handlerapp;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Handler, worker thread ile main thread arasında iletişimin bir başka yoludur.
    // Handler : handleMessage() -> main thread, sendMesssage() -> worker thread
    // önce sendMessage() ile MessageQueue' ya bir Runnable ya da bir Message gönderir.
    // MessageQueue' de Sırası gelince looperdan geçer ve handler' ın main thread' deki metodu handleMessage()' a gelir.

    private String TAG = "MainActivity";
    Button btnStart, btnStop;
    TextView txtLoop;
    int loopCount = 0;
    boolean isExecute = true;
    Handler handler; //mainactivity' nin onCreate()' ında tanımlanır.

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

        //main threadde handler' ı tanımlıyoruz böylelikle main threade ve onun mesaj kuyruğuna erişebilir.
        handler = new Handler();
    }
    void startLoop (){

        new Thread(new Runnable() {
            @Override
            public void run() {
                isExecute= true;
                while (isExecute){
                    try {
                        Thread.sleep(1000);
                        Log.e(TAG, "Döngü sayısı : " + loopCount + " Thread : " + Thread.currentThread().getName());
                        //ilk olarak runnable gönderelim
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                //mainactivity' deki messageQueue' ya gönderilir.
                                txtLoop.setText(""+ loopCount);
                            }
                        });
                        loopCount++;

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

}
