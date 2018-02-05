package com.bsrakdg.com.filesystemapp.external;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bsrakdg.com.filesystemapp.R;

import org.w3c.dom.Text;

/**
 * Created by bakdag on 5.02.2018.
 */

public class ExternalStorageActivity extends AppCompatActivity {

    // Amaç : belirttiğimiz dosya adıyla oluşturulan dosyaya bir şeyler yazıp kaydedeceğiz.
    // burdaki dosyalar private ya da public olabilir.
    // getExternalFilesDir() : çok büyük ve internal storage' a sığmayan dosyaları external storage' e kaydedebiliriz. Private, uygulama silindiği zaman silinir.
    // getExternalStoragePublicDirectory() : tüm uygulamalar ve kullanıcılar tarafından erişilebilir(Kamera uygulaması). Public, uygulama silindiği zaman silinmez.
    // Her zaman kullanıma uygun değildir. (SD card değişebilir. Kullanmadan erişilebilir olduğunu kontrol etmek gerekli).
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

    }

    public void getDataFromExternalPrivate(View view) {

    }

    public void saveDataToExternalPublic(View view) {

    }

    public void getDataFromExternalPublic(View view) {

    }

}
