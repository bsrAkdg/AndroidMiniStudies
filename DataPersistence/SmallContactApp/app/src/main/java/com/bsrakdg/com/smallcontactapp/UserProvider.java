package com.bsrakdg.com.smallcontactapp;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by bakdag on 9.02.2018.
 */

public class UserProvider extends ContentProvider {
    // User datasına erişip onunla ilgili işlemler yapmak için Content provider ve (2) database(1) tanımlıyoruz.
    // 1. DATABASE
    // Bir database oluşturuyoruz : kullanıcı ekleme silme güncelleme için.
    private final static String DATABASE_NAME = "user.db";
    private final static Integer DATABASE_VERSION = 1;
    private final static String USER_TABLE_NAME = "users";
    private final static String CREATE_USERS_TABLE = "CREATE TABLE " + USER_TABLE_NAME
            + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "name TEXT NOT NULL);";
    SQLiteDatabase sqLiteDatabase;

    // 2. CONTENT PROVIDER
    // Manifeste provider' ı ekle
    static final String CONTENT_AUTHORITY = "com.bsrakdg.com.smallcontactapp.userprovider"; //erişilecek provider
    static final String PATH_USERS = "users"; // erişilecek tablo adı
    static final Uri BASE_CONTENT_URI = Uri.parse("content://"+ CONTENT_AUTHORITY);
    static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_USERS);
    //static final Uri CONTENT_URI = Uri.parse( BASE_CONTENT_URI + "/" + PATH_USERS) bu şekilde de tanımlanabilir.
    static final UriMatcher uriMatcher;
    //bu sınıftan herhangi bir öğe erişildiğinde ilk çalışacak metod burası
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH); //default bir değer verdik (-1)
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_USERS, 10); //matcher' a bütün content provider' da kullandığınız URI' leri belirtmemiz lazım : USER TABLOSUNA ERİŞME KODU 10 DEDİK
    }
    //ARTIK USERPROVIDER' IN INSERT, DELETE, UPDATE' INI KULLANABİLİRİZ !!!

    @Override
    public boolean onCreate() {
        //Bu sınıf oluşturulduğunda database' de oluşturulsun.
        DatabaseHelper helper = new DatabaseHelper(getContext());
        sqLiteDatabase = helper.getWritableDatabase();
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String s1) {

        switch (uriMatcher.match(uri)){
            case 10:
                Cursor cursor = sqLiteDatabase.query(USER_TABLE_NAME, projection, selection, selectionArgs, null, null, null);
                return cursor;
            default:
                throw new IllegalArgumentException("Unknow URI" + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        switch (uriMatcher.match(uri)){
            case 10:
                //users tablosuna veri yazılmak isteniyor
                long addedRowId = sqLiteDatabase.insert(USER_TABLE_NAME, null, contentValues);
                if (addedRowId > 0){ //işlem başarılı
                    //insert metodu geriye bir Uri dönüyor
                    Uri _uri = ContentUris.withAppendedId(CONTENT_URI, addedRowId);
                    return _uri;
                }
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int deletedRowCount = -1;

        switch (uriMatcher.match(uri)){
            case 10:
               deletedRowCount = sqLiteDatabase.delete(USER_TABLE_NAME, selection, selectionArgs);
               break;
        }
        return deletedRowCount;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    private class DatabaseHelper extends SQLiteOpenHelper{

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            //database' i oluşturalım.
            sqLiteDatabase.execSQL(CREATE_USERS_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            //güncelleme için varolanı sil yeniden oluştur
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
            onCreate(sqLiteDatabase);
        }
    }
}
