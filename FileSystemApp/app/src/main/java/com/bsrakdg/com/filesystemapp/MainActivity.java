package com.bsrakdg.com.filesystemapp;

import android.Manifest;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.bsrakdg.com.filesystemapp.external.ExternalStorageActivity;
import com.bsrakdg.com.filesystemapp.external.RuntimePermissionActivity;
import com.bsrakdg.com.filesystemapp.internal.InternalStorageActivity;

public class MainActivity extends RuntimePermissionActivity {

    //Amaç : internal, external ve cache storage' a dosya kaydetmek ve okumak
    //Internal dosyalar dosya sisteminde "files" dizininin içerisinde oluşturulur.
    //External dosyalar dosya sisteminde "storage -> emulated(sd card) -> 0 (public) -> Android -> data -> package -> files -> belirlediğimiz dizin -> belirlediğimiz dosya (private)"
    //Cache dosyaları dosya sisteminde "cache" dizininin içerisinde oluşturulur.

    private static Integer EXTERNAL_STORAGE_CODE = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showInternalStorage(View view) {
        startActivity(new Intent(MainActivity.this, InternalStorageActivity.class));
    }

    public void showExternalStorage(View view) {
        // External Storage' a yazmak ve ordan okumak istiyorsak ;
        // API 23 ÖNCESİ : manifeste izin eklemeliyiz
        // API 23 VE SONRASI : runtime permission (RuntimePermissionActivity)
        String[] receivePermissions ={Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        MainActivity.super.requestPermission(receivePermissions, EXTERNAL_STORAGE_CODE);
    }

    public void showStorageCache(View view) {
        startActivity(new Intent(MainActivity.this, CacheStorageActivity.class));
    }

    @Override
    public void allowed(int requestCode) {
        //İzin alındı
        if (requestCode == EXTERNAL_STORAGE_CODE){
            externalStorageTransactions();
        }
    }

    void externalStorageTransactions(){
        startActivity(new Intent(MainActivity.this, ExternalStorageActivity.class));
    }
}
