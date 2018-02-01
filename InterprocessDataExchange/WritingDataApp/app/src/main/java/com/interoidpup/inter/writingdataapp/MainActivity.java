package com.interoidpup.inter.writingdataapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button btnSave;
    TextView txtResult;
    EditText edtTxtData;

    void init(){
        btnSave = (Button) findViewById(R.id.btnSave);
        txtResult = (TextView) findViewById(R.id.txtResult);
        edtTxtData = (EditText) findViewById(R.id.edtTxtData);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToFile();
            }
        });

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    void saveToFile(){
        File file = null;
        String writeData = edtTxtData.getText().toString();
        FileOutputStream fileOutputStream=null;
        try {
            file=getFilesDir();
            fileOutputStream=openFileOutput("bsrakdg.txt", Context.MODE_PRIVATE);
            fileOutputStream.write(writeData.getBytes());
            txtResult.setText(writeData + " Dosyaya Yazıldı "+ file.getAbsolutePath()+"/bsrakdg.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
