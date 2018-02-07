package com.bsrakdg.com.sqliteapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bsrakdg.com.sqliteapp.db.DatabaseHelper;
import com.bsrakdg.com.sqliteapp.db.NoteContract;

public class MainActivity extends AppCompatActivity {
    // Amaç : Sqlite' a veri kaydedip okuma işlemlerini gerçekleştirme
    /* Adımlar : 1. Tablolarını ve ilişkiselliği belirle (foreign key)
                 2. NoteContact sınıfında tablo ve column adlarını sabit olarak oluştur.
                 3. DatabaseHelper sınıfında tablonu oluştur ve güncelle.
                 4. Nerede kullanıcaksan orada DatabaseHelper nesnesi oluştur
                 5.

    */
    DatabaseHelper databaseHelper;
    SQLiteDatabase sqLiteDatabase;

    Spinner spnrCategories;
    ListView lstNotes;
    Toolbar toolbar;
    FloatingActionButton fab;

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

        setNotesAdapter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

/*      //Yukarıdaki database maddelerini tamamladıktan sonra helper' ı çalıştırdıktan sonra contract çalışacak.
        databaseHelper = new DatabaseHelper(this);

        //Database' de okuma işlemini aktifleştirmek için :
        sqLiteDatabase = databaseHelper.getReadableDatabase();  */

    }

    void setNotesAdapter(){
        String[] notes = getResources().getStringArray(R.array.notes);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.list_item, R.id.txtNote, notes );
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
        }


        return super.onOptionsItemSelected(item);
    }

    private void createNote(){

        databaseHelper = new DatabaseHelper(this);
        sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues newRecord = new ContentValues();
        newRecord.put(NoteContract.NoteEntry.COLUMN_NOTE, "Balık al");
        newRecord.put(NoteContract.NoteEntry.COLUMN_CATEGORY_ID, 1);
        newRecord.put(NoteContract.NoteEntry.COLUMN_CREATE_DATE, "07-02-2018");
        newRecord.put(NoteContract.NoteEntry.COLUMN_DONE, 0);

        sqLiteDatabase.insert(NoteContract.NoteEntry.TABLE_NAME, null, newRecord);
    }

    private void readNote(String readId){

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
        Cursor cursor = sqLiteDatabase.query(NoteContract.NoteEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        String allNotes = "";
        while (cursor.moveToNext()) {
            for (int j = 0; j <= 5; j++) {
                allNotes += cursor.getString(j) + " - ";
            }
            allNotes += "\n";
        }
        Log.e("RESULT", allNotes);

        cursor.close();
        sqLiteDatabase.close();

    }

    private void updateNote(String updateId){

        databaseHelper = new DatabaseHelper(this);
        sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues updateValues=new ContentValues();
        updateValues.put(NoteContract.NoteEntry.COLUMN_NOTE, "BU NOT GÜNCELLENDİ");
        String[] selectionArgs={updateId};

        int changeRowCount = sqLiteDatabase.update(NoteContract.NoteEntry.TABLE_NAME, updateValues, NoteContract.NoteEntry._ID + " = ?", selectionArgs);

        Toast.makeText(this, "Güncellenen satır sayısı: " + changeRowCount , Toast.LENGTH_LONG).show();

    }

    private void deleteNote(String deleteId){

        databaseHelper = new DatabaseHelper(this);
        sqLiteDatabase = databaseHelper.getWritableDatabase();

        String[] selectionArgs = {deleteId};
        int changeRowCount = sqLiteDatabase.delete(NoteContract.NoteEntry.TABLE_NAME, NoteContract.NoteEntry._ID + " = ?", selectionArgs);

        Toast.makeText(this, "Silinen satır sayısı: "+ changeRowCount, Toast.LENGTH_LONG).show();

    }
}
