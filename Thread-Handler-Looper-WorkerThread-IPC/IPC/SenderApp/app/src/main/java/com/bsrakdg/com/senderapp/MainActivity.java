package com.bsrakdg.com.senderapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    //Amaç : diğer processlerin erişebileceği ve Random String üreten bir Service tanımlamak.
    // app -> create new Service (exported enable : başka process erişebilsin diye) -> MyService
    Button btnStart, btnStop;
    private String TAG = "MainActivity";
    private Intent serviceIntent;

    void init(){
        btnStart = (Button) findViewById(R.id.btnStart);
        btnStop = (Button) findViewById(R.id.btnStop);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService();
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        serviceIntent = new Intent(this, MyService.class);

    }

    void startService(){
        Log.e(TAG, "startService");
        startService(serviceIntent);
    }

    void stopService(){
        Log.e(TAG, "stopService");
        stopService(serviceIntent);
    }

}
