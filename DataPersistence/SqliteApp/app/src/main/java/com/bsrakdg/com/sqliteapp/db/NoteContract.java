package com.bsrakdg.com.sqliteapp.db;

import android.provider.BaseColumns;

/**
 * Created by bakdag on 6.02.2018.
 */

public class NoteContract {
    //notlar tablosu : entry
    public static final class NoteEntry implements BaseColumns{ //BaseColumns' i, ID' yi kullanabilmek için implement ettik.
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
        //tablo adı
        public static final String TABLE_NAME = "Categories";
        //id tanımlıyoruz
        public static final String ID = BaseColumns._ID;
        //column isimleri
        public static final String COLUMN_CATEGORY = "category";

    }
}
