package com.bsrakdg.com.contentproviderapp;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //Amaç : rehberdeki kişiler çekip listelemek
    //bunun için isteğimizi belirten bir content resolver gerekli
    //content resolver' ın query metodu ile koşullar ne ne istediğimiz belirtirilerek geriye bir cursor elde edilir.
    
    List<User> userList = new ArrayList<>();
    RecyclerView rcyclerUser;

    void init(){
        rcyclerUser = (RecyclerView) findViewById(R.id.rcyclerUser);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        showAllPerson();
    }

    void showAllPerson(){
        //uygulamanın package' ındaki content resolver nesnesini (karşı taraftan veri istemek için) alalım.
        ContentResolver contentResolver = getContentResolver();

        //bir query ile ne istediğimizi belirtip geriye bir Cursor değer alıyoruz. Query' nin parametreleri:

        //Uri : istek nereye iletilecekse
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        //karşı taraftan istediğimiz veriler (select kısmı)
        String[] projection = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                               ContactsContract.CommonDataKinds.Phone.NUMBER};
        //koşullar
        String selection = null; //ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME' e göre getir diyebiliriz.
        //koşulu sağlayan argümanlar
        String[] selectionArgs = null; //"Büşra" diyerek selection'u büşra olanları getir deriz
        //Veriyi sıralı almak istiyorsak
        String sortOrder = null;

        Cursor cursor = contentResolver.query(uri, projection, selection, selectionArgs, sortOrder);

        if ( cursor != null && cursor.getCount()>0){
            //moveToNext : baştan başlayıp liste sonuna kadar gezer
            while (cursor.moveToNext()){
                //karşı tarafta ne olduğunu bilmediğim için direk column indexten gitmek daha mantıklı
                String strName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String strNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                userList.add(new User(strName, strNumber));
            }
            if (userList.size() > 0){
                setAdapter();
            }
        }
    }

    void setAdapter(){
        UserAdapter userAdapter = new UserAdapter(getApplicationContext(), userList);
        rcyclerUser.setLayoutManager(new LinearLayoutManager(this));
        rcyclerUser.setAdapter(userAdapter);
    }


}
