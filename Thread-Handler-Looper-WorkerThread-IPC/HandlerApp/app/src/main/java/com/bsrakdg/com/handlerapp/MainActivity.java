package com.bsrakdg.com.handlerapp;

import android.os.Handler;
import android.os.Message;
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

        //custom thread' i create ediyorum.
        thread = new Thread(new MyThread());

        //main threadde handler' ı tanımlıyoruz böylelikle main threade ve onun mesaj kuyruğuna erişebilir.
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                //custom threadde sendMessage() dedikten sonra bu metoda düşer.Artık worker thread ile main thread arası iletişim sağlandı.
                txtLoop.setText("Döngü sayısı : " + msg.arg1 );
            }
        };
    }
    void startLoop (){

    /* TODO ilk çözüm : default bir thread kullanmak : onCreate' de bir handle oluştur sonra buraya yönlendir.
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
        }).start();*/

    // TODO ikinci çözüm : custom thread oluşturmak
        thread.start();
    }
    void stopLoop(){
        isExecute= false;
    }

    private class MyThread implements Runnable{

        @Override
        public void run() {
            isExecute= true;
            while (isExecute){
                try {
                    Thread.sleep(1000);
                    Log.e(TAG, "Döngü sayısı : " + loopCount + " Thread : " + Thread.currentThread().getName());

                    //ikinci olarak message gönderelim.
                    Message message = Message.obtain(); //new message yerine message.obtain() diyoruz çünkü tekrar tekrar oluşturmanın önüne geçer
                    //Bundle yada arg1,arg2 gibi bir iki elemanlı değer gönderebiliriz.
                    message.arg1 = loopCount;
                    message.arg2 = loopCount;
                    //artık ilgili handler' ın handleMessage() metoduna gelir.
                    handler.sendMessage(message);
                    //handler.sendMessageDelayed(message, 5000); 5 sn sonra iletir.
                    loopCount++;

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
