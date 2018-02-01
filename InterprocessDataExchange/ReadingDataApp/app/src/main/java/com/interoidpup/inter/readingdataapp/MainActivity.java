package com.interoidpup.inter.readingdataapp;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {


    String packageName= "com.interoidpup.inter.writingdataapp";

    Button btnRead;
    TextView txtRead;

    void init(){
        btnRead = (Button) findViewById(R.id.btnRead);
        txtRead = (TextView) findViewById(R.id.txtRead);

        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadToFile();
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    public void downloadToFile(){
        PackageManager packageManager=getPackageManager();
        try {
            ApplicationInfo info= packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            Toast.makeText(this, info.dataDir+"/files/bsrakdg.txt", Toast.LENGTH_LONG).show();
            String dosyaYolu=info.dataDir+"/files/bsrakdg.txt";
            readtoFile(dosyaYolu);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readtoFile(String path) throws IOException {
        FileInputStream fileInputStream=null;
        try {
            fileInputStream=new FileInputStream(new File(path));
            int readCharData = -1; //dosya sonuna gelir.
            StringBuffer buffer=new StringBuffer();
            while((readCharData  = fileInputStream.read()) != -1){
                buffer.append((char) readCharData);
            }
            txtRead.setText(buffer);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
