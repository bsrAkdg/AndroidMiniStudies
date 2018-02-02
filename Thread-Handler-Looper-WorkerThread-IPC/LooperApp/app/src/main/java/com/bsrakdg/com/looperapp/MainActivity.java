package com.bsrakdg.com.looperapp;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    // Bu projede amaç : Main thread' den worker thread' e veri gönderme.
    // Handler' ı nerede create edersen o thread' e bağlanır.
    // HandlerApp' te onCreate()' te create ettiğimiz için -> worker thread : sendMessage() -> main thread : handleMessage();
    // LooperApp' te worker thread' te create ettiğimiz için -> main thread : sendMessage() -> worker thread : handleMessage();
    // bunun için custom bir thread oluşturalım
    MyThread myThread;
    Button btnSendMessage;
    public static final String TAG = "MainActivity" ;

    void init(){
        btnSendMessage = (Button) findViewById(R.id.btnSendMessage);
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        myThread = new MyThread();
        myThread.start();

    }

    void sendMessage() {
        // main threadden worker thread' e mesaj gönder.
        Message message = Message.obtain(); //sürekli üretmeyip varolanı kullanmak için
        Bundle bundle = new Bundle();
        bundle.putString("message", "Ben Main Thread' den Worker thread' e gelen bir mesajım.");
        message.setData(bundle);
        myThread.handler.sendMessage(message);
    }
}

class MyThread extends Thread{
    //Main thread' de default olarak Message Queue ve Looper var. Ama kendi thread' imde bunları oluşturmam gerekli.
    //Amacım main threadden bu thread' e data göndermek olduğu için Looper kullanmamız gerekli.
    Handler handler;

    MyThread(){
        //handler = new Handler() dersek bu thread onCreate() metodunda oluşturulduğu için otomatik olarak buda main thread olacak. Amacım worker thread oluşturmak.
    }

    @Override
    public void run(){
        //Looper oluşturalım
        Looper.prepare(); // O anki thread' e bağlar ve message queue' den gelen verileri handler etmeyi sağlayan looper oluşturur.

        //handler' ı burada oluşturduğum için worker thread' e bağlanır.
        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                Log.e("MyThread", "Gelen veri : " + bundle.getString("message") + " Thread adı : " + Thread.currentThread().getName());
            }
        };

        Looper.loop(); // bu thread üzerinde message queue' nin çalışmasını sağlar. Mesaj alıp verebilir.
    }

}