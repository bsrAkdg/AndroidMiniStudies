package com.bsrakdg.com.smallcontactapp;

import android.content.ContentValues;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Contact Provider : bir data kümesine erişmek için kullanılan abstract bir sınıf.
    // Önce UserProvider sınıfını yaz. Sonra aşağıdaki metodları

    static final Uri CONTENT_URI = UserProvider.CONTENT_URI;

    EditText edtTxtRemoveToUserid, edtTxtShowToUserid, edtTxtNewUser;
    TextView txtAllUsers;

    void init(){
        edtTxtRemoveToUserid = (EditText) findViewById(R.id.edtTxtRemoveToUserid);
        edtTxtShowToUserid = (EditText) findViewById(R.id.edtTxtShowToUserid);
        edtTxtNewUser = (EditText) findViewById(R.id.edtTxtNewUser);
        txtAllUsers = (TextView) findViewById(R.id.txtAllUsers);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }


    public void showAllUser(View view) {

    }

    public void removeUser(View view) {

    }

    public void addNewUser(View view) {
        String strUserName = edtTxtNewUser.getText().toString();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", strUserName);

        //data hazır artık content resolvr ile isteği iletiyoruz.
        Uri resultUri = getContentResolver().insert(CONTENT_URI, contentValues);
        Toast.makeText(this, ""+resultUri, Toast.LENGTH_LONG).show();
    }

    public void showUser(View view) {

    }
}
