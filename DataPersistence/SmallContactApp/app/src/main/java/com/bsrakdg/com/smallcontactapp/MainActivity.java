package com.bsrakdg.com.smallcontactapp;

import android.content.ContentValues;
import android.database.Cursor;
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
        mtdAllUser();
    }


    public void showAllUser(View view) {
        mtdAllUser();
    }

    public void removeUser(View view) {
        String strRemoveUser = edtTxtRemoveToUserid.getText().toString();
        String selection = "id = ? ";
        String[] selectionArgs = {strRemoveUser};

        //silmek istediğimizi content resolver' a belirtiyoruz. UserProvider' ın delete metoduna düşer.
        int removedCount = getContentResolver().delete(CONTENT_URI,selection, selectionArgs);
        if (removedCount != -1){
            edtTxtRemoveToUserid.setText("");
            Toast.makeText(this, "Kişi silindi", Toast.LENGTH_LONG).show();
            mtdAllUser();
        }else{
            Toast.makeText(this, "Kişi bulunamadı", Toast.LENGTH_LONG).show();
        }

    }

    public void addNewUser(View view) {
        // Yeni bir kişi ekleyeceğimiz zaman content resolver' a insert edeceğimizi bildiriyoruz. UserProvider' ın insert metoduna düşer.
        // isert etmek istediğimiz değeri ContentValue cinsinden ifade ediyoruz.
        String strUserName = edtTxtNewUser.getText().toString();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", strUserName);

        //data hazır artık content resolvr ile isteği iletiyoruz.
        Uri resultUri = getContentResolver().insert(CONTENT_URI, contentValues);

        if (resultUri != null){
            Toast.makeText(this, "Kişi eklendi", Toast.LENGTH_LONG).show();
            mtdAllUser();
        }else{
            Toast.makeText(this, "Kişi eklenemedi", Toast.LENGTH_LONG).show();
        }
        edtTxtNewUser.setText("");
    }

    public void showUser(View view) {
        //content resolver' a ne istediğimizi söylüyoruz. Bir query çalışıyor bu UserProvider' daki query metoduna düşecek.
        String strShowUser = edtTxtShowToUserid.getText().toString();
        String[] projection = {"id, name"};
        String selection = "id = ? ";
        String[] selectionArgs = {strShowUser};
        Cursor cursor  = getContentResolver().query(CONTENT_URI, projection, selection, selectionArgs, null);

        String strAllUser = "";

        if (cursor.moveToFirst()){
            do {
                String id = cursor.getString(cursor.getColumnIndex("id")); //sütun adı id olanın değerini getir.
                String name = cursor.getString(cursor.getColumnIndex("name")); //sütun adı name olanın değerini getir.

                strAllUser = strAllUser + id + " : " + name + " \n";
            }while (cursor.moveToNext()); //bir sonraki olana kadar
        }
        if (strAllUser.equals("")){
            Toast.makeText(this, "Kişi blunamadı", Toast.LENGTH_LONG).show();
        }else{
            txtAllUsers.setText(strAllUser);
        }
        edtTxtShowToUserid.setText("");
    }

    private void mtdAllUser(){

        //content resolver' a ne istediğimizi söylüyoruz. Bir query çalışıyor bu UserProvider' daki query metoduna düşecek.
        String[] projection = {"id, name"};
        Cursor cursor  = getContentResolver().query(CONTENT_URI, projection, null, null, null);

        String strAllUser = "";

        if (cursor.moveToFirst()){
            do {
                String id = cursor.getString(cursor.getColumnIndex("id")); //sütun adı id olanın değerini getir.
                String name = cursor.getString(cursor.getColumnIndex("name")); //sütun adı name olanın değerini getir.

                strAllUser = strAllUser + id + " : " + name + " \n";
            }while (cursor.moveToNext()); //bir sonraki olana kadar
        }
        txtAllUsers.setText(strAllUser);
    }
}
