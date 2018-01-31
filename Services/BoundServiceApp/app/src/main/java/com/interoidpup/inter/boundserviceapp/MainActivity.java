package com.interoidpup.inter.boundserviceapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    // Bir saniye arayla random String oluşturup ekrana basalım.
    //bu ekranı kitler. Bunun için Service' in onStartCommand' ını thread içerisinde gerçekleştirilebilir.

    Button btnStartBoundService;
    boolean isStarted = false;

    void init(){
        btnStartBoundService = (Button) findViewById(R.id.btnStartBoundService);
        btnStartBoundService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startBoundService();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    void startBoundService(){
        if (isStarted){
            btnStartBoundService.setText("Bound Servisi Durdur");
            isStarted  = true;
            Intent intent = new Intent(MainActivity.this, MyBoundService.class);
            startService(intent);
        }else{
            btnStartBoundService.setText("Bound Servisi Çalıştır");
            isStarted  = false;
            Intent intent = new Intent(MainActivity.this, MyBoundService.class);
            stopService(intent);
        }
    }

}
