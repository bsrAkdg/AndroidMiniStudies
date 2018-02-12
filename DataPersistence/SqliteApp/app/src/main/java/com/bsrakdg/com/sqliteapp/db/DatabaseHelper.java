package com.bsrakdg.com.sqliteapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// !!!!!!!! create table derken direk olarak ismini kullanmak için burda o entry' leri import ediyoruz.
import com.bsrakdg.com.sqliteapp.db.NoteContract.CategoryEntry;
import com.bsrakdg.com.sqliteapp.db.NoteContract.NoteEntry;

//veritabanının oluşturulması ve güncellenmesi için yardımcı sınıf
public class DatabaseHelper extends SQLiteOpenHelper {

    //databaseimiz ile ilgili sabitler
    public static final String DATABASE_NAME = "note.db";
    private static final int DATABASE_VERSION = 1; //Upgrade' de önemli.

    /*
    TODO -- CREATE CATEGORY TABLE
    CREATE TABLE CATEGORIES (_ID INTEGER PRIMARY KEY, CATEGORY TEXT)
    */
    private static final String TABLE_CATEGORİES_CREATE =
            "CREATE TABLE " + CategoryEntry.TABLE_NAME + " (" +
            CategoryEntry.ID + " INTEGER PRIMARY KEY," +
            CategoryEntry.COLUMN_CATEGORY + " TEXT) ";

    /*
    TODO -- CREATE NOTE TABLE
    CREATE TABLE NOTES (_ID INTEGER PRIMARY KEY, COLUMN_NOTE TEXT, COLUMN_CREATE_DATE TEXT,
    COLUMN_FINISH_DATE TEXT, COLUMN_DONE TEXT, COLUMN_CATEGORY_ID INTEGER, FOREIGN_KEY(COLUMN_CATEGORY_ID) REFERENCES CATEGORIES(_ID))
    */
    private static final String TABLE_NOTES_CREATE =
            "CREATE TABLE " + NoteEntry.TABLE_NAME + " (" +
            NoteEntry.ID + " INTEGER PRIMARY KEY, " +
            NoteEntry.COLUMN_NOTE + " TEXT, "+
            NoteEntry.COLUMN_CREATE_DATE + " TEXT DEFAULT CURRENT_TIMESTAMP, " +
            NoteEntry.COLUMN_FINISH_DATE + " TEXT, " +
            NoteEntry.COLUMN_DONE + " INTEGER, " +
            NoteEntry.COLUMN_CATEGORY_ID + " INTEGER, " +
            "FOREIGN KEY ( "+ NoteEntry.COLUMN_CATEGORY_ID +")  " + "REFERENCES "+ CategoryEntry.TABLE_NAME + " (" + CategoryEntry.ID +"))";
    // FOREIGN KEY' i onConfigure()' de aktifleştirmemiz lazım.

    public DatabaseHelper(Context context) {
        //context, database adı, geri değer dönüyorsa cursor, database versiyon kodu
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //diskinizde herhangi bir database yoksa ve ilk defa açılıyorsa burası çalışır.

        //execSQL() : geriye hiçbir parametre yollamadan verilen sorguyu çalıştırır.
        //yukarıda tablo oluşturma sorgularını çalıştır.
        sqLiteDatabase.execSQL(TABLE_CATEGORİES_CREATE);
        sqLiteDatabase.execSQL(TABLE_NOTES_CREATE);

    }

    @Override
    public void onConfigure(SQLiteDatabase sqLiteDatabase) {
        //veri bütünlüğünü sağlaması için.
        sqLiteDatabase.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // tablo adı değiştiğinde veya column eklendiğinde burada güncellemek gerekiyor.
        // önce varolanları sil, sonra tekrar oluştur.
        // Eğer tablolardaki dataları kaybetmek istemiyorsan içeriğini kaydet, güncellemeden sonra dataları al insert et.
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + CategoryEntry.TABLE_NAME);
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + NoteEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
