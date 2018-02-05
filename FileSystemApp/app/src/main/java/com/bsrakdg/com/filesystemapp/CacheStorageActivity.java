package com.bsrakdg.com.filesystemapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by bakdag on 5.02.2018.
 */

public class CacheStorageActivity extends AppCompatActivity {
    // Amaç : internal ve external cache storage' a veri yazma okuma.
    // data -> data -> package -> cache : internal cache file
    // storage -> emulated(sdcard) -> 0 -> Android -> data -> package -> cache : external cache file

    TextView txtFileContentInternal, txtFileContentExternal;
    EditText edtTxtDataInternal,edtTxtDataExternal;

    private  String strInternalFileName = "cache_internal.txt";
    private  String strExternalFileName = "cache_external.txt";

    void init(){
        txtFileContentInternal = (TextView) findViewById(R.id.txtFileContentInternal);
        txtFileContentExternal = (TextView) findViewById(R.id.txtFileContentExternal);
        edtTxtDataInternal = (EditText) findViewById(R.id.edtTxtDataInternal);
        edtTxtDataExternal = (EditText) findViewById(R.id.edtTxtDataExternal);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cache_storage);

        init();

    }

    //girilen data'yı internal cache storage' e kaydeder.
    public void saveDatatoInternalCache(View view) {

        String strInternalCacheData = edtTxtDataInternal.getText().toString(); //yazılacak data' yı al
        File dirInternalCache = getCacheDir(); //Cache dosyalarının tutulduğu dizine eriş.
        File newFile = new File(dirInternalCache, strInternalFileName ); //yeni bir dosya oluşturuyoruz.
        writeToFile(newFile,strInternalCacheData); //dosyaya data' yı yaz.
        edtTxtDataInternal.setText("");
    }

    //internal storage' dan datayı getirir.
    public void getDataFromInternalCache(View view) {

        txtFileContentInternal.setText(""); //varolan değeri temizle
        File dirInternalCache = getCacheDir(); //Cache dosyalarının tutulduğu dizine erişilir.
        File readFile = new File(dirInternalCache, strInternalFileName );  //okunulacak dosya oluşturuluyor.
        if (readToFile(readFile) != null){
            txtFileContentInternal.setText(readToFile(readFile));
        }
    }

    //girilen data'yı external cache storage' e kaydeder.
    public void saveDatatoCacheExternal(View view) {
        //external storage write ve read işlemleri için manifestte izin gerekli ama external cache storage' te izin gerekli değil.
        String strExternalCacheData = edtTxtDataExternal.getText().toString();
        File dirExternalCache = getExternalCacheDir(); //Cache dosyalarının tutulduğu dizine erişilir.
        File newFile = new File(dirExternalCache, strExternalFileName ); //yeni bir dosya oluşturuyoruz.
        writeToFile(newFile, strExternalCacheData); //dosyaya data' ı yaz.
        edtTxtDataExternal.setText("");
    }

    //external storage' dan datayı getirir.
    public void getDataFromExternalCache(View view) {
        //external storage write ve read işlemleri için manifestte izin gerekli ama external cache storage' te izin gerekli değil.
        txtFileContentExternal.setText(""); //varolan değeri temizle
        File dirExternalCache = getExternalCacheDir(); //Cache dosyalarının tutulduğu dizine erişilir.
        File readFile = new File(dirExternalCache, strExternalFileName ); //kunulacak dosya oluşturuluyor.
        if (readToFile(readFile) != null){
            txtFileContentExternal.setText(readToFile(readFile));
        }
    }

    //external cache ile internal cache' e yazma işlemi aynıdır.
    private void writeToFile(File file, String data){

        FileOutputStream fileOutputStream = null; //Dosyaya veri yazacağımız zaman FileOutputStream

        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(data.getBytes());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //finally' de memory leak' e sebep olmamak için mutlaka stream' ı kapat!
            if (fileOutputStream != null){
                try {
                    Toast.makeText(this, "Veri Cache' e Kaydedildi", Toast.LENGTH_LONG).show();
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String readToFile(File file){

        StringBuffer stringBuffer = new StringBuffer(); //byte byte okuduğumu buraya atıyorum.
        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(file);

            int read;
            while ( (read = fileInputStream.read()) != -1){ //byte byte okuyor okuduğu değeri read' e atıyor.
                stringBuffer.append((char)read); //her bir int değeri char' a çevrilir.
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //finally' de memory leak' e sebep olmamak için mutlaka stream' ı kapat!
            if (fileInputStream != null){
                try {
                    txtFileContentInternal.setText(stringBuffer.toString());
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return stringBuffer.toString();
    }
}
