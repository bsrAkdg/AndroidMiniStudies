package com.bsrakdg.com.receiverapp;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //Amaç : ReceiverApp' te bir butona tıklanıldığı zaman SenderApp' teki servis vasıtasıyla üretilen String' i alıp ReceiverApp' te göstermek.
    //ilk olarak SenderApp' i düzenle.
    //bind olmak için serviceconnection, messenger' ını hazırla

    Button btnBindService, btnUnbindService, btnReceive;
    TextView txtReceive;

    private String TAG = "MainActivity";
    public String rsultString = "";
    private boolean isConnected = false;
    private Messenger sendRequestMessenger, receiveResultMessenger ; //SenderApp' teki messenger' a bağlanacak ve ondan cevap alıcak messengerlar.

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder ıBinder) {
            sendRequestMessenger = new Messenger(ıBinder); // iki messenger' ı bağladık. (istek yapan ve o isteği karşı tarafta karşılayan)
            receiveResultMessenger = new Messenger(new ReceiveHandler()); //istek yaptıktan sonra karşı taraftan gelen sonucu karşılayacak messenger' ı tanılmıyoruz.
            isConnected = true;
            Log.e(TAG, "onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isConnected = false;
            sendRequestMessenger = null; //messenger bağlantıyı sağladığı çin onuda null yap.
            receiveResultMessenger = null; //gelen değeri karşılayan messenger'ı null yap.
            Log.e(TAG, "onServiceDisconnected");
        }
    };
    //gelen değeri alan Messenger için üretildi
    class ReceiveHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String result = bundle.getString("randomString");
            Log.e(TAG, "SenderApp' ten gelen random string : " + result);
            txtReceive.setText("SenderApp' ten gelen random string :\n" +result);
        }
    }

    void init(){
        btnBindService = (Button) findViewById(R.id.btnBindService);
        btnUnbindService = (Button) findViewById(R.id.btnUnbindService);
        btnReceive = (Button) findViewById(R.id.btnReceive);
        txtReceive = (TextView) findViewById(R.id.txtReceive);

        btnBindService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bindRemoteService();
            }
        });
        btnUnbindService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unbindRemoteService();
            }
        });
        btnReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                receiveData();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    void bindRemoteService(){
        //öncelikle servise bind olalım
        Intent bindIntent = new Intent();
        bindIntent.setComponent(new ComponentName("com.bsrakdg.com.senderapp", "com.bsrakdg.com.senderapp.MyService")); //başka bir uygulamadı componente erişebiliriz.

        if (!isConnected){
            if(bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE)){ //bindService boolean değer döner.
                isConnected = true;
                Log.e(TAG, "Servise bağlanıldı.");
            }
        }else{
            Toast.makeText(this, "Servise zaten bağlı", Toast.LENGTH_LONG).show();
        }

    }

    void unbindRemoteService(){
        if (isConnected){
            unbindService(serviceConnection);
            isConnected = false;
        }else{
            Toast.makeText(this, "Servise zaten bağlı değil", Toast.LENGTH_LONG).show();
        }

    }

    void receiveData(){
        if (isConnected){
            //göndereceğimiz message' i oluşturalım
            Message sendMessage = Message.obtain(null, 16); //karşı process' teki handleMessage' a düşen message bu.
            sendMessage.replyTo =  receiveResultMessenger; //karşı taraftan gelen message' ı receiveResultMessenger vasıtasıya alacağımı belirttim.

            try {
                sendRequestMessenger.send(sendMessage); //karşı tarafa gönder.
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(this, "Önce servise bağlan", Toast.LENGTH_LONG).show();
        }
    }

}
