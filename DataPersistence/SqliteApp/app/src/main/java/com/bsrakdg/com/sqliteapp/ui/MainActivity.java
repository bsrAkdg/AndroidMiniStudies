package com.bsrakdg.com.sqliteapp.ui;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bsrakdg.com.sqliteapp.R;
import com.bsrakdg.com.sqliteapp.adapters.CategoryCursorAdapter;
import com.bsrakdg.com.sqliteapp.adapters.NotesCursorAdapter;
import com.bsrakdg.com.sqliteapp.db.NoteContract;
import com.bsrakdg.com.sqliteapp.db.NoteContract.NoteEntry;
import com.bsrakdg.com.sqliteapp.db.NoteContract.CategoryEntry;
import com.bsrakdg.com.sqliteapp.db.NoteQueryHandler;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{


    //Amaç : Sqlite' a veri kaydedip okuma işlemlerini gerçekleştirme
/*    custom datavase işlemleri adımlar
                 TODO -- Sqlite Örnek işlemler
                 1. Tablolarını ve ilişkiselliği belirle (foreign key)
                 2. NoteContact sınıfında tablo ve column adlarını sabit olarak oluştur.
                 3. DatabaseHelper sınıfında tablonu oluştur ve güncelle.
                 4. Nerede kullanıcaksan orada DatabaseHelper nesnesi oluştur
                 5. Bu activity' de content resolver' a content uri kullanarak isteğini gönder.

      content provider ile database adımları :
                 1. Tablolarını ve ilişkiselliği belirle (foreign key)
                 2. NoteContact sınıfında tablo ve column adlarını sabit olarak oluştur.
                 3. DatabaseHelper oluştur
                 4. NoteProvider' custom provider oluştur ve manifeste ekle.
                 5. Bu activity' de content resolver' a content uri kullanarak isteğini gönder.
*/

/*  TODO -- Sqlite Örnek işlemler
    DatabaseHelper databaseHelper;
    SQLiteDatabase sqLiteDatabase;*/

    //silme işleminde -1 hepsini sil demek
    private static final int ALL_NOTES = -1;
    private static final int ALL_CATEGORIES = -1;

    Spinner spnrCategories;
    ListView lstNotes;
    Toolbar toolbar;
    FloatingActionButton fab;
    NotesCursorAdapter notesCursorAdapter;
    CategoryCursorAdapter categoryCursorAdapter;
    Cursor cursor, categoryCursor;

    long selectedCategoryId;
    void init(){

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        spnrCategories = (Spinner) findViewById(R.id.spnrCategories);
        lstNotes = (ListView) findViewById(R.id.lstNotes);

        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NoteActivity.class));
            }
        });

        //kategorileri göster
        setCategoryAdapter();

        setNoteAdapter();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

    }

    void setNoteAdapter(){
        //main thread kitlenmesine engel olmak için cursor loader kullanımı.
        // loader initialize edilir onCreateLoader() metoduna düşer.
        getLoaderManager().initLoader(100, null, this);
        // TODO loader eklendi :
        // cursor = showAllNotesWithProvider()"böyle çekmeye gerek kalmadı
        notesCursorAdapter = new NotesCursorAdapter(this, cursor, false);
        //autoRequery mutlaka false yap main thread' i kitliyor.
        lstNotes.setAdapter(notesCursorAdapter);

        lstNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent noteIntent = new Intent(MainActivity.this, NoteActivity.class);

                Cursor cursor = (Cursor) lstNotes.getItemAtPosition(position);

                noteIntent.putExtra(NoteEntry._ID, cursor.getString(cursor.getColumnIndex(NoteEntry._ID)));
                noteIntent.putExtra(NoteEntry.COLUMN_NOTE, cursor.getString(cursor.getColumnIndex(NoteEntry.COLUMN_NOTE)));
                noteIntent.putExtra(NoteEntry.COLUMN_CREATE_DATE, cursor.getString(cursor.getColumnIndex(NoteEntry.COLUMN_CREATE_DATE)));
                noteIntent.putExtra(NoteEntry.COLUMN_FINISH_DATE, cursor.getString(cursor.getColumnIndex(NoteEntry.COLUMN_FINISH_DATE)));
                noteIntent.putExtra(NoteEntry.COLUMN_DONE, cursor.getString(cursor.getColumnIndex(NoteEntry.COLUMN_DONE)));
                noteIntent.putExtra(NoteEntry.COLUMN_CATEGORY_ID, cursor.getString(cursor.getColumnIndex(NoteEntry.COLUMN_CATEGORY_ID)));
                noteIntent.putExtra(CategoryEntry.COLUMN_CATEGORY, cursor.getString(cursor.getColumnIndex(CategoryEntry.COLUMN_CATEGORY)));

                startActivity(noteIntent);
            }
        });
    }

    void setCategoryAdapter(){

        getLoaderManager().initLoader(150, null, this);

        categoryCursorAdapter =  new CategoryCursorAdapter(this, categoryCursor, false);
        spnrCategories.setAdapter(categoryCursorAdapter);
        spnrCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                selectedCategoryId = id;
                //her seferinde categoryId değişeceği için loader' ı güncellemem gerekli
                getLoaderManager().restartLoader(100, null, MainActivity.this);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings){
            return true;
        }else if(id == R.id.action_category){
            startActivity(new Intent(MainActivity.this, CategoryActivity.class));
            return true;
        }else if(id == R.id.action_delete_all_notes){
            deleteNoteWithProvider(ALL_NOTES);
            return true;
        }else if(id == R.id.action_delete_all_categories){
            deleteCategoryWithProvider(ALL_CATEGORIES);
            return true;
        }else if(id == R.id.action_create_categories){
            createTestCategories();
            return true;
        }else if(id == R.id.action_create_notes){
            createTestNotes();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //provider ile database işlemleri - başlangıç
    void addCategory(String strCategory){

        ContentValues contentValues = new ContentValues();
        contentValues.put(NoteContract.CategoryEntry.COLUMN_CATEGORY, strCategory);
        Uri resultUri = getContentResolver().insert(NoteContract.CategoryEntry.COTNENT_URI, contentValues);
        Toast.makeText(this, "Eklendi : " + resultUri, Toast.LENGTH_LONG).show();

    }

    void showCategory(){

        String[] projection = {"_id", "category"};
        Cursor cursor = getContentResolver().query(NoteContract.CategoryEntry.COTNENT_URI, projection, null, null, null, null);
        String allCategories = "";
        while (cursor.moveToNext()){
            String id = cursor.getString(0);
            String category = cursor.getString(1);

            allCategories = allCategories + " id : " + id + " kategori : " + category + "\n";
        }
        Toast.makeText(this, allCategories, Toast.LENGTH_LONG).show();

    }

    void addNoteWithProvider(String strNote){

        ContentValues newNote = new ContentValues();
        newNote.put(NoteContract.NoteEntry.COLUMN_NOTE, strNote  );
        newNote.put(NoteContract.NoteEntry.COLUMN_CATEGORY_ID, 1);
        newNote.put(NoteContract.NoteEntry.COLUMN_CREATE_DATE, "10-02-2018");
        newNote.put(NoteContract.NoteEntry.COLUMN_DONE, 1);

        Uri resultUri = getContentResolver().insert(NoteContract.NoteEntry.COTNENT_URI, newNote);
        Toast.makeText(this, "Eklendi : " + resultUri, Toast.LENGTH_LONG).show();

    }

    void showAllNotesWithProvider(){

        //iki tabloyu joinlediğimiz için hangi alanları göstermek istiyorsak o tabloyu başına yazmalıyız.
        String[] projection = {"Notes._id", "Notes.note", "Categories._id", "Categories.category"};
        Cursor resultCursor = getContentResolver().query(NoteContract.NoteEntry.COTNENT_URI, projection, null, null, null );

        String strAllNotes = "";

        while(resultCursor.moveToNext()){
            for (int i = 0; i<=3; i++){
                strAllNotes += resultCursor.getString(i) + " - ";
            }
            strAllNotes += "\n";
        }
        Toast.makeText(this, strAllNotes, Toast.LENGTH_LONG).show();

    }

    void updateNoteWithProvider(){

        ContentValues contentValues = new ContentValues();
        contentValues.put(NoteContract.NoteEntry.COLUMN_NOTE, "yine güncellendi");
        String selection = " _id = ?";
        String[] selectionArgs = {"2"};

        int id = getContentResolver().update(NoteContract.NoteEntry.COTNENT_URI, contentValues, selection, selectionArgs);
        Toast.makeText(this, "Not güncellendi : " + id, Toast.LENGTH_SHORT).show();

    }

    void deleteNoteWithProvider(int removedId){

        String selection = " _id = ?";
        String[] selectionArgs = {String.valueOf(removedId)};

        if (removedId == ALL_NOTES){ //hepsini sil işlemi için
            selectionArgs= null;
            selection= null;
        }

        /*UI kitlenmesine sebep olur
        int id = getContentResolver().delete(NoteContract.NoteEntry.COTNENT_URI, selection, selectionArgs);
        Toast.makeText(this, "Not silindi : " + id, Toast.LENGTH_SHORT).show();
        */

        NoteQueryHandler handler = new NoteQueryHandler(this.getContentResolver());
        handler.startDelete(1, null, NoteContract.NoteEntry.COTNENT_URI, selection, selectionArgs);
    }

    void deleteCategoryWithProvider(int removedId){

        String selection = " _id = ?";
        String[] selectionArgs = {String.valueOf(removedId)};

        if (removedId == ALL_CATEGORIES){ //hepsini sil işlemi için
            selectionArgs= null;
            selection= null;
        }

        /*UI kitlenmesine sebep olur
        int id = getContentResolver().delete(NoteContract.CategoryEntry.COTNENT_URI, selection, selectionArgs);
        Toast.makeText(this, "Category silindi : " + id, Toast.LENGTH_SHORT).show();
        */

        NoteQueryHandler handler = new NoteQueryHandler(this.getContentResolver());
        handler.startDelete(1, null, NoteContract.CategoryEntry.COTNENT_URI, selection, selectionArgs);
    }

    void createTestCategories(){
        for (int i=0; i<=20; i++){
            ContentValues contentValues = new ContentValues();
            contentValues.put(NoteContract.CategoryEntry.COLUMN_CATEGORY, "New Category : "+i);

            /* UI kitlenmesine sebep olduk çünkü bu main thread' de çalışır.
            getContentResolver().insert(NoteContract.CategoryEntry.COTNENT_URI, contentValues);
            */
            NoteQueryHandler handler = new NoteQueryHandler(this.getContentResolver());
            handler.startInsert(1, null, NoteContract.CategoryEntry.COTNENT_URI, contentValues);
        }
    }

    void createTestNotes(){
        for (int i=0; i<=1000; i++){
            ContentValues contentValues = new ContentValues();
            contentValues.put(NoteEntry.COLUMN_NOTE, "New Note  : "+i);
            contentValues.put(NoteEntry.COLUMN_CATEGORY_ID, (i%20)+1);
            contentValues.put(NoteEntry.COLUMN_CREATE_DATE, "13-02-2018");
            contentValues.put(NoteEntry.COLUMN_FINISH_DATE, "01-03-2018");
            contentValues.put(NoteEntry.COLUMN_DONE, (i%2==0) ? 1 : 0);

            /* UI kitlenmesine sebep olduk çünkü bu main thread' de çalışır.
            Uri uri = getContentResolver().insert(NoteContract.NoteEntry.COTNENT_URI, contentValues);
            */

            //Content Resolver' da asenkron işlemler için yardımcı : AsyncQueryHandler
            NoteQueryHandler handler = new NoteQueryHandler(this.getContentResolver());
            handler.startInsert(1, null, NoteEntry.COTNENT_URI, contentValues);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        //loader create edilir id 100 ise notlar, 150 ise kategoriler
        if (id == 100){
            //görmek istediğim alanları projection' a gönderiyorum. İki tablo birleşimi olduğu için query' de SQLiteQueryBuilder olmalı!
            String[] projection = {NoteEntry.TABLE_NAME + "." + NoteEntry._ID,
                    NoteEntry.COLUMN_NOTE, NoteEntry.COLUMN_CREATE_DATE,
                    NoteEntry.COLUMN_FINISH_DATE, NoteEntry.COLUMN_DONE,
                    NoteEntry.COLUMN_CATEGORY_ID, CategoryEntry.COLUMN_CATEGORY};
            //hangi kategori seçildiyse ona dair notlar gelir
            String selection = NoteEntry.COLUMN_CATEGORY_ID + " = ?";
            String[] selectionArgs = {String.valueOf(selectedCategoryId)};
            //String sortOrder = "_id DESC";

            //NoteProvider' ın query metoduna düşer bu yüzden query metoduna ekleme yapmalıyız **
            return new CursorLoader(this, NoteContract.NoteEntry.COTNENT_URI, projection,
                    selection, selectionArgs, null);
            //asenkron çalışır veri eklediğimiz zaman günceller
        }else if(id == 150){

            String[] projection = {CategoryEntry._ID, CategoryEntry.COLUMN_CATEGORY};
            String sortOrder = "_id DESC";

            return new CursorLoader(this, CategoryEntry.COTNENT_URI, projection,
                    null, null, sortOrder);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        //adapteri parametre olarak gelen cursor ile değiştir.
        if (loader.getId() == 100){
            notesCursorAdapter.swapCursor(cursor);
        }else if(loader.getId() == 150){
            categoryCursorAdapter.swapCursor(cursor);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //veri erişilemez olduğu zaman
        notesCursorAdapter.swapCursor(null);
        categoryCursorAdapter.swapCursor(null);
    }

    //provider ile database işlemleri - bitiş

/*  TODO -- Sqlite Örnek işlemler
    onCreate(){

         init();

        // custom database işlemleri
        databaseHelper = new DatabaseHelper(this);
        sqLiteDatabase = databaseHelper.getReadableDatabase();

        setNotesAdapter();
    }

    private void addNote(){

        databaseHelper = new DatabaseHelper(this);
        sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues newNote = new ContentValues();
        newNote.put(NoteContract.NoteEntry.COLUMN_NOTE, "Balık al");
        newNote.put(NoteContract.NoteEntry.COLUMN_CATEGORY_ID, 1);
        newNote.put(NoteContract.NoteEntry.COLUMN_CREATE_DATE, "07-02-2018");
        newNote.put(NoteContract.NoteEntry.COLUMN_DONE, 0);

        sqLiteDatabase.insert(NoteContract.NoteEntry.TABLE_NAME, null, newNote);
    }

    private void showAllNotes(String readId){

        databaseHelper = new DatabaseHelper(this);
        sqLiteDatabase = databaseHelper.getWritableDatabase();

        // Hangi columnları görmek istiyorsam onları belirtiyorum
        String[] projection = {
                NoteContract.NoteEntry.COLUMN_NOTE,
                NoteContract.NoteEntry.COLUMN_CREATE_DATE,
                NoteContract.NoteEntry.COLUMN_FINISH_DATE,
                NoteContract.NoteEntry.COLUMN_DONE,
                NoteContract.NoteEntry.COLUMN_CATEGORY_ID
        };

        // Koşulun ne olacağını belirtiyorum.
        String selection = NoteContract.NoteEntry.COLUMN_CATEGORY_ID + " = ?";

        // Koşulu sağlayan değeri belirtiyorum.
        String[] selectionArgs = {readId};

        // Sorgu çalıştıktan sonra geriye bir cursor döndürür.
        Cursor resultCursor = sqLiteDatabase.query(NoteContract.NoteEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        String allNotes = "";
        while (resultCursor.moveToNext()) {
            for (int j = 0; j <= 5; j++) {
                allNotes += resultCursor.getString(j) + " - ";
            }
            allNotes += "\n";
        }
        Log.e("RESULT", allNotes);

        resultCursor.close();
        sqLiteDatabase.close();

    }

    private void updateNote(String updateId){

        databaseHelper = new DatabaseHelper(this);
        sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues updateValues=new ContentValues();
        updateValues.put(NoteContract.NoteEntry.COLUMN_NOTE, "BU NOT GÜNCELLENDİ");
        String[] selectionArgs={updateId};

        int changeRowCount = sqLiteDatabase.update(NoteContract.NoteEntry.TABLE_NAME,
                updateValues, NoteContract.NoteEntry._ID + " = ?", selectionArgs);

        Toast.makeText(this, "Güncellenen satır sayısı: " + changeRowCount , Toast.LENGTH_LONG).show();

    }

    private void deleteNote(String deleteId){

        databaseHelper = new DatabaseHelper(this);
        sqLiteDatabase = databaseHelper.getWritableDatabase();

        String[] selectionArgs = {deleteId};
        int changeRowCount = sqLiteDatabase.delete(NoteContract.NoteEntry.TABLE_NAME,
                NoteContract.NoteEntry._ID + " = ?", selectionArgs);

        Toast.makeText(this, "Silinen satır sayısı: "+ changeRowCount, Toast.LENGTH_LONG).show();

    }

    void setNotesAdapter(){
        String[] notes = getResources().getStringArray(R.array.notes);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.list_item_note, R.id.txtNote, notes );
        lstNotes.setAdapter(adapter);
        lstNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent noteIntent = new Intent(MainActivity.this, NoteActivity.class);
                noteIntent.putExtra("noteContent", (String) lstNotes.getItemAtPosition(i));
                startActivity(noteIntent);
            }
        });
    }
*/
}
