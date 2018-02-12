package com.bsrakdg.com.sqliteapp.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by bakdag on 12.02.2018.
 */

public class NoteProvider extends ContentProvider {
    // Bununla ilgili sabitleri NoteContract' ta tut
    // Manifeste bu provider' ı ekle
    private String TAG = "NoteProvider";
    // note ve categories için sabit tanımla : bu int değerlere bakarak hangi uri' a erişileceğini anlarız.
    private static  final int URI_CODE_NOTES = 10;
    private static  final int URI_CODE_CATEGORIES = 20;

    // uri matcher ve static{} tanımla
    private static  final UriMatcher uriMatcher= new UriMatcher(UriMatcher.NO_MATCH);
    //bu classa erişildiği an çalışır: matcher' a linkleri tanımlatıyoruz.
    static{
        uriMatcher.addURI(NoteContract.CONTENT_ATHORITY, NoteContract.PATH_NOTES, URI_CODE_NOTES); //NOTES
        uriMatcher.addURI(NoteContract.CONTENT_ATHORITY, NoteContract.PATH_CATEGORIES, URI_CODE_CATEGORIES); //CATEGORIES
    }

    private SQLiteDatabase sqLiteDatabase;
    private DatabaseHelper databaseHelper;

    @Override
    public boolean onCreate() {
        //content provider ilk oluşturulduğunda çalışan metod.
        databaseHelper  = new DatabaseHelper(getContext());
        sqLiteDatabase = databaseHelper.getWritableDatabase(); //hem okuma hem yazma
        return true; //true ise başladı demek
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Cursor cursor = null;
        SQLiteQueryBuilder sqLiteQueryBuilder; //iki tabloyu join yapmak istediğimizde kullanıyoruz. çünkü query tek tablo adı alıyor.
        String strJoinTables = "Notes inner join Categories on Notes.categoryId = Categories._id";

        switch (uriMatcher.match(uri)){ //hangi tabloya insert yapılacak onu kontrol etmeliyiz
            case URI_CODE_NOTES:
                sqLiteQueryBuilder = new SQLiteQueryBuilder();
                sqLiteQueryBuilder.setTables(strJoinTables);
                cursor = sqLiteQueryBuilder.query(sqLiteDatabase, projection, selection, selectionArgs, null, null, null);
                break;

            case URI_CODE_CATEGORIES:
                cursor = sqLiteDatabase.query(NoteContract.CategoryEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
                break;

            default:
                throw new IllegalArgumentException("QUERY UNKNOW URI " + uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        switch (uriMatcher.match(uri)){ //hangi tabloya insert yapılacak onu kontrol etmeliyiz
            case URI_CODE_NOTES:
                return addNew(uri, contentValues, NoteContract.NoteEntry.TABLE_NAME);
            case URI_CODE_CATEGORIES:
                return addNew(uri, contentValues, NoteContract.CategoryEntry.TABLE_NAME);
            default:
                throw new IllegalArgumentException("INSERT UNKNOW URI : " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        switch (uriMatcher.match(uri)){ //hangi tabloya insert yapılacak onu kontrol etmeliyiz
            case URI_CODE_NOTES:

                break;

            case URI_CODE_CATEGORIES:

                break;
        }
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        switch (uriMatcher.match(uri)){ //hangi tabloya insert yapılacak onu kontrol etmeliyiz
            case URI_CODE_NOTES:

                break;

            case URI_CODE_CATEGORIES:

                break;
        }
        return 0;
    }

    private Uri addNew(Uri uri, ContentValues contentValues, String tableName){

        long id = sqLiteDatabase.insert(tableName, null, contentValues);
        if (id == -1) { //ekleme yapılmamış
            Log.e(TAG, "insert hata : " + uri );
        }
        return ContentUris.withAppendedId(uri, id);//kendisine gelen uri' nin sonuna bir şey ekle gönder
    }



}
