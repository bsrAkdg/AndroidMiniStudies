package com.interoidpup.inter.boundserviceapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";
    // Bir saniye arayla random String oluşturup ekrana basalım.
    //bu ekranı kitler. Bunun için Service' in onStartCommand' ını thread içerisinde gerçekleştirilebilir.
    Button btnStartBoundService, btnStartService, btnCreateString;
    //butonların durumunu tutar.
    boolean isStartedService = true, isStartedBoundService = true;
    //Servis ile componentin bağlantılı olup olmadığını sürekli kontrol edecek sınıf
    ServiceConnection serviceConnection;
    //Bir bağlantı işlemi olduğu için işlem yapmadan önce bağlı olup olmadığımı kontrol etmek için bir değişken
    private boolean isConnected = false;
    //Servisten activity' e veri almak için
    private MyBoundService myBoundService;
    //*** bind bağlamak, başlatmak demek değil, o yüzden bağlandıktan sonra tekrar çalışması için servisi başlatmalısın !

    void init(){
        btnStartBoundService = (Button) findViewById(R.id.btnStartBoundService);
        btnStartService = (Button) findViewById(R.id.btnStartService);
        btnCreateString = (Button) findViewById(R.id.btnCreateString);

        btnStartBoundService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startBoundService();
            }
        });
        btnStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService();
            }
        });
        btnCreateString.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRandomString();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    void startService(){
        if (isStartedService){
            btnStartService.setText("Servisi Durdur");
            isStartedService  = false;
            Intent intent = new Intent(MainActivity.this, MyBoundService.class);
            startService(intent);
            Log.e(TAG, "Servis başladı");
        }else{
            btnStartService.setText("Servisi Çalıştır");
            isStartedService  = true;
            Intent intent = new Intent(MainActivity.this, MyBoundService.class);
            stopService(intent);
            Log.e(TAG, "Servis durduruldu");
        }
    }

    void startBoundService(){
        if (isStartedBoundService){
            btnStartBoundService.setText("Bound Servisi Durdur");
            isStartedBoundService= false;
            bindMyBoundService();
        }else{
            btnStartBoundService.setText("Bound Servisi Çalıştır");
            isStartedBoundService= true;
            unbindMyBoundService();
        }
    }

    void bindMyBoundService(){
        //ilk connection' u kontrol et
        if (serviceConnection == null){
            serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName componentName, IBinder ıBinder) {
                    isConnected = true;
                    //Activity servise hala bağlanmadı. Bu metodun parametreleri sayesinde bağlama işlemi gerçekleşecek.
                    // componentName : bağlı olduğu componentin adı
                    //ıBinder : serviste tanımladığımız local binder
                    MyBoundService.MyLocalBinder myLocalBinder  = (MyBoundService.MyLocalBinder) ıBinder;
                    //şimdi tanımladığımız myLocalBinder sayesinde servise erişelim.
                    myBoundService = myLocalBinder.getService();
                    Log.e(TAG, "Bound Service Connected Çağrıldı");

                }

                @Override
                public void onServiceDisconnected(ComponentName componentName) {
                    //telefon kaynakları sıkıntılı duruma düşerse o zaman bu metod çalışır.
                    isConnected = false;
                    Log.e(TAG, "Bound Service Disconnected Çağrıldı");
                }
            };
        }
        //servise bağlı değilse intent oluştur ve bindservice() metodunu çalıştır.
        if (isConnected == false){
            //bind edelim
            Intent boundIntent = new Intent(this, MyBoundService.class);
            //flag olarak BIND_AUTO_CREATE : servise bağlı component varsa oluştur.
            bindService(boundIntent, serviceConnection, Context.BIND_AUTO_CREATE );
            isConnected = true;
            Log.e(TAG, "Bound Servis bağlantısı yapıldı");
        }else{
            //zaten bağlı ise toas mesajıyla bildir.
            Toast.makeText(this, "Bound Servise Zaten Bağlı", Toast.LENGTH_LONG).show();
        }

    }


    void unbindMyBoundService(){
        if (isConnected == true){
            unbindService(serviceConnection);
            isConnected = false;
            Log.e(TAG, "Bound Servis bağlantısı kesildi");
        }else{
            Toast.makeText(this, "Bound Servise zaten bağlı değil", Toast.LENGTH_LONG).show();
        }
    }

    public void getRandomString(){
        //bu metod servisi bağlayıp başlattıktan sonra random string üretir.
        if (isConnected == true) {
            Toast.makeText(this, "Üretilen String :" + myBoundService.getString(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Önce Bound servis bağlantısını yapın", Toast.LENGTH_SHORT).show();
        }
    }

}
