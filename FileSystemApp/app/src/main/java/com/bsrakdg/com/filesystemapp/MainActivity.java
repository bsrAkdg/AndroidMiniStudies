package com.bsrakdg.com.filesystemapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.bsrakdg.com.filesystemapp.external.ExternalStorageActivity;
import com.bsrakdg.com.filesystemapp.internal.InternalStorageActivity;

public class MainActivity extends AppCompatActivity {

    //Amaç : internal, external ve cache storage' a dosya kaydetmek ve okumak
    //Internal dosyalar dosya sisteminde "files" dizininin içerisinde oluşturulur.
    //External dosyalar
    //Cache dosyaları dosya sisteminde "cache" dizininin içerisinde oluşturulur.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showInternalStorage(View view) {
        startActivity(new Intent(MainActivity.this, InternalStorageActivity.class));
    }

    public void showExternalStorage(View view) {
        startActivity(new Intent(MainActivity.this, ExternalStorageActivity.class));
    }

    public void showStorageCache(View view) {
        startActivity(new Intent(MainActivity.this, CacheStorageActivity.class));
    }
}
