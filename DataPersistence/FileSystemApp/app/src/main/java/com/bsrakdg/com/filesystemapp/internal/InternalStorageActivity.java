package com.bsrakdg.com.filesystemapp.internal;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bsrakdg.com.filesystemapp.R;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by bakdag on 3.02.2018.
 */

public class InternalStorageActivity extends AppCompatActivity {

    //Amaç : belirttiğimiz dosya adıyla oluşturulan dosyaya bir şeyler yazıp kaydedeceğiz.
    // Daha sonra dosyanın içeriğini, path' ni gösterip o dosyayı silebileceğiz.
    private EditText edtTxtFileName, edtTxtMessage, edtTxtDeleteFile;
    private TextView txtInternalStoragePath, txtFilesList;

    void init(){
        edtTxtFileName 		= (EditText) findViewById(R.id.edtTxtFileName);
        edtTxtMessage 		= (EditText) findViewById(R.id.edtTxtMessage);
        edtTxtDeleteFile 	= (EditText) findViewById(R.id.edtTxtDeleteFile);
        txtInternalStoragePath 	= (TextView) findViewById(R.id.txtInternalStoragePath);
        txtFilesList 			= (TextView) findViewById(R.id.txtFilesList);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internal_storage);
        init();
    }

    public void saveToInternalStorage(View view) {
        String strFileName = edtTxtFileName.getText().toString();
        String strMessage = edtTxtMessage.getText().toString();

        FileOutputStream fileOutputStream = null;
        try {
            //MODE_PRIVATE : o dosya o uygulamaya özgü , MODE_APPEND : dosya daha önceden var ise o dosyaya ekleme yapar.
            fileOutputStream = openFileOutput(strFileName, MODE_PRIVATE); //internal storage' ta o dosya adı yoksa otomatik olarak oluşturulur.
            fileOutputStream.write(strMessage.getBytes()); //dosyaya yazdırılmak istenen datayı byte dizisine çevirerek işliyorum.
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fileOutputStream!=null) {
                try {
                    Toast.makeText(this, "Dosya ve veri eklendi", Toast.LENGTH_LONG).show();
                    edtTxtFileName.setText("");
                    edtTxtMessage.setText("");
                    fileOutputStream.close(); //yazmak için açılan FileOutputStorage mutlaka kapatılmalı! Memory Leak !
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void moveToDisplayScreen(View view) {
        //dosyaların içeriğini gösterecek activity' e yönlendirir.
        startActivity(new Intent(InternalStorageActivity.this, InternalStorageShowActivity.class));
    }

    public void showInternalStoragePath(View view) {
        //dosyaları nereye kaydettiği ile ilgili bilgi
        String strFilePath = getFilesDir().getPath(); //getFilesDir() file nesnesi geri döndürür.
        txtInternalStoragePath.setText(strFilePath);
    }

    public void showFilesList(View view) {
        //birden fazla dosya olacağı için array' e atalım
        String[] arrFileList = fileList();
        StringBuilder stringBuilder = new StringBuilder();
        for (String strFilename : arrFileList) {
            stringBuilder.append(strFilename).append(",");
        }
        if (stringBuilder != null){
            txtFilesList.setText(stringBuilder);
        }
    }

    public void deleteFile(View view) {
        String strRemoveFile = edtTxtDeleteFile.getText().toString();
        if (deleteFile(strRemoveFile)){
            Toast.makeText(this, strRemoveFile + " dosyası silindi", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, strRemoveFile + " dosyası silinemedi", Toast.LENGTH_LONG).show();
        }
    }
}
