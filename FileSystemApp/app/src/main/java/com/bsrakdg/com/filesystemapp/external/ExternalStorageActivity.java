package com.bsrakdg.com.filesystemapp.external;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bsrakdg.com.filesystemapp.R;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by bakdag on 5.02.2018.
 */

public class ExternalStorageActivity extends AppCompatActivity {

    // Amaç : belirttiğimiz dosya adıyla oluşturulan dosyaya bir şeyler yazıp kaydedeceğiz.
    // private dosyalar :  storage -> emulated(sdcard) -> 0 -> Android -> data -> package -> files -> belirlediğimiz dizin -> belirlediğimiz dosya
    // External Storage' a yazmak ve ordan okumak istiyorsak API 23 öncesi : manifeste izin eklemeliyiz, API 23 VE SONRASI : runtime permission
    TextView txtFileContentPrivate, txtFileContentPublic;
    EditText edtTxtDataPrivate, edtTxtDataPublic;

    private  String strPrivateFileName = "cache_private.txt";
    private  String strPublicFileName = "cache_public.txt";

    void init(){
        txtFileContentPrivate = (TextView) findViewById(R.id.txtFileContentPrivate);
        txtFileContentPublic = (TextView) findViewById(R.id.txtFileContentPublic);
        edtTxtDataPrivate = (EditText) findViewById(R.id.edtTxtDataPrivate);
        edtTxtDataPublic = (EditText) findViewById(R.id.edtTxtDataPublic);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_external_storage);

        init();
    }


    public void saveDataToExternalPrivate(View view) {
        String strExternalPrivateData = edtTxtDataPrivate.getText().toString(); //yazılacak data' yı al
        File filePath = getExternalFilesDir("bsrDirectory"); //null olursa direk o dizine, değer verirsek o dizine yazar.
        File newFile = new File(filePath, strPrivateFileName);
        writeToFile(newFile, strExternalPrivateData);
    }

    public void getDataFromExternalPrivate(View view) {
        File fileDir = getExternalFilesDir("bsrDirectory");
        File readFile = new File(fileDir, strPrivateFileName);
        if (readToFile(readFile) != null){
            txtFileContentPrivate.setText(readToFile(readFile));
        }
    }

    public void saveDataToExternalPublic(View view) {
        String strExternalPublicData = edtTxtDataPublic.getText().toString(); //yazılacak data' yı al
        //File fileDir = Environment.getExternalStoragePublicDirectory("bsrFolder"); //kendi belirldiğimiz dizin
        File filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS); //public dosyaların hepsine böyle erişebiliriz.
        File newFile = new File(filePath, strPublicFileName);
        writeToFile(newFile, strExternalPublicData);

    }

    public void getDataFromExternalPublic(View view) {
        File filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File readFile = new File(filePath, strPublicFileName);
        if (readToFile(readFile) != null){
            txtFileContentPublic.setText(readToFile(readFile));
        }
    }


    //external cache ile external' a yazma işlemi aynıdır.
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
                    Toast.makeText(this, "Veri External' a Kaydedildi", Toast.LENGTH_LONG).show();
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
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return stringBuffer.toString();
    }

}
