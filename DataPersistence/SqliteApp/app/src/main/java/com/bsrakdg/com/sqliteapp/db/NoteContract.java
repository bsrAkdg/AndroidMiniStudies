package com.bsrakdg.com.sqliteapp.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by bakdag on 6.02.2018.
 */

public class NoteContract {
    // NoteProvider
    public static  final String CONTENT_ATHORITY = "com.bsrakdg.com.sqliteapp.db.NoteProvider"; //(manifest ile aynı olmalı)
    public static  final String PATH_NOTES = "Notes"; //hangi tabloya erişecekse
    public static  final String PATH_CATEGORIES = "Categories"; //hangi tabloya erişecekse
    //Base content URI oluşturuyoruz(tipi uri olmalı)
    public static  final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_ATHORITY);
    //her bir tablonun içerisine ** ekle

    //notlar tablosu : entry
    public static final class NoteEntry implements BaseColumns{ //BaseColumns' i, ID' yi kullanabilmek için implement ettik.
        //content provider için content uri **
        public static final Uri COTNENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_NOTES);
        //content://com.bsrakdg.com.sqliteapp.db.NoteProvider/Notes

        //tablo adı
        public static final String TABLE_NAME = "Notes";
        //id tanımlıyoruz
        public static final String ID = BaseColumns._ID;
        //column isimleri
        public static final String COLUMN_NOTE = "note";
        public static final String COLUMN_CREATE_DATE = "createDate";
        public static final String COLUMN_FINISH_DATE = "finishDate";
        public static final String COLUMN_DONE = "done";
        public static final String COLUMN_CATEGORY_ID = "categoryId";

    }
    //category tablosu
    public static final class CategoryEntry implements BaseColumns{
        //content provider için content uri **
        public static final Uri COTNENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_CATEGORIES);
        //content://com.bsrakdg.com.sqliteapp.db.NoteProvider/Categories

        //tablo adı
        public static final String TABLE_NAME = "Categories";
        //id tanımlıyoruz
        public static final String ID = BaseColumns._ID;
        //column isimleri
        public static final String COLUMN_CATEGORY = "category";

    }
}
