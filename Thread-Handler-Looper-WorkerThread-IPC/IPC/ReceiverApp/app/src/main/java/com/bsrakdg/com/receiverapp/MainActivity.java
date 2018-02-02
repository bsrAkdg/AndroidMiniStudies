package com.bsrakdg.com.receiverapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    //Amaç : ReceiverApp' te bir butona tıklanıldığı zaman SenderApp' teki servis vasıtasıyla üretilen String' i alıp ReceiverApp' te göstermek.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
