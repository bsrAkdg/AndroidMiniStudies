package com.bsrakdg.com.filesystemapp.internal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bsrakdg.com.filesystemapp.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by bakdag on 3.02.2018.
 */

public class InternalStorageShowActivity extends AppCompatActivity {

    EditText edtTxtFileName;
    TextView txtFileContent;

    void init(){
        edtTxtFileName = (EditText) findViewById(R.id.edtTxtFileName);
        txtFileContent = (TextView) findViewById(R.id.txtFileContent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internal_storage_show);
        init();
    }

    public void readAndDisplayFile(View view) {
        txtFileContent.setText("");
        String strFileName = edtTxtFileName.getText().toString();
        //Dosyaya yazarken fileoutputstream, okurken fileinputstream

        FileInputStream fileInputStream = null;
        StringBuffer stringBuffer = new StringBuffer(); //aldığı değerleri tek tek içerisinde biriktiriyor.
        try {
            fileInputStream = openFileInput(strFileName);
            //dosyayı yazarken byte byte yazıyorduk, okurkende aynı şekilde
            int read;
            while( (read = fileInputStream.read()) != -1){ //fileInputStream.read() byte byte okuyup okuduğum her değeri read' a atıyorum -1 ise sonuna gelmiş demektir.
                stringBuffer.append((char) read);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fileInputStream != null)
            try {
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //sonucu yazalım.
        txtFileContent.setText(stringBuffer.toString());
    }
}
