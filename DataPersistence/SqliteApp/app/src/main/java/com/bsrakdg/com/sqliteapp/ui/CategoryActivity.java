package com.bsrakdg.com.sqliteapp.ui;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.bsrakdg.com.sqliteapp.R;
import com.bsrakdg.com.sqliteapp.adapters.CategoryCursorAdapter;
import com.bsrakdg.com.sqliteapp.db.NoteContract;
import com.bsrakdg.com.sqliteapp.db.NoteContract.CategoryEntry;
import com.bsrakdg.com.sqliteapp.db.NoteQueryHandler;

/**
 * Created by bakdag on 5.02.2018.
 */

public class CategoryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    ListView lstCategories;
    EditText edtTxtCategoryName;

    NoteQueryHandler noteQueryHandler;
    CategoryCursorAdapter categoryCursorAdapter;
    Cursor cursor= null;

    long selectedCategoryId = -1; //yeni kategori mi eski mi bunun kontrolü için

    void init(){
        lstCategories = (ListView) findViewById(R.id.lstCategories);
        edtTxtCategoryName = (EditText) findViewById(R.id.edtTxtCategoryName);

        //TODO LoadManager initialization
        getLoaderManager().initLoader(5, null, this);

        //TODO AsyncQueryHandler initilization
        noteQueryHandler = new NoteQueryHandler(this.getContentResolver());

        lstCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                selectedCategoryId = id;
                Cursor c = (Cursor) lstCategories.getItemAtPosition(position);
                edtTxtCategoryName.setText(c.getString(c.getColumnIndex(CategoryEntry.COLUMN_CATEGORY)));
            }
        });

        setCategoryAdapter();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        init();

        //bir önceki acitivity' e geri dönmesi için
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    void setCategoryAdapter(){
        categoryCursorAdapter = new CategoryCursorAdapter(this, cursor, false);
        lstCategories.setAdapter(categoryCursorAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    public void addNewCategory(View view) {
        //yeni kategori eklemeden önce varlığının kontrolünü yap
        edtTxtCategoryName.setText("");
        selectedCategoryId = -1;
    }

    public void saveCategory(View view) {
        String strNewCategory = edtTxtCategoryName.getText().toString();

        ContentValues contentValues = new ContentValues();
        contentValues.put(CategoryEntry.COLUMN_CATEGORY, strNewCategory);

        //o kategori id' ye sahip bir category id var ise onu güncelle, yoksa yeni ekle
        if (selectedCategoryId == -1){
            //insert
            noteQueryHandler.startInsert(1, null, CategoryEntry.COTNENT_URI, contentValues);
            edtTxtCategoryName.setText("");
            Toast.makeText(this, "Yeni Kategori Eklendi", Toast.LENGTH_LONG).show();
        }else{
            //update
            String selection = CategoryEntry._ID + " = ?";
            String[] selectionArgs = {String.valueOf(selectedCategoryId)};
            noteQueryHandler.startUpdate(1, null, CategoryEntry.COTNENT_URI, contentValues, selection, selectionArgs);
            edtTxtCategoryName.setText("");
            Toast.makeText(this, "Kategori Güncellendi", Toast.LENGTH_LONG).show();
        }
    }

    public void deleteCategory(View view) {
        String selection = CategoryEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(selectedCategoryId)};
        noteQueryHandler.startDelete(1, null, CategoryEntry.COTNENT_URI, selection, selectionArgs);
        edtTxtCategoryName.setText("");
        Toast.makeText(this, "Kategori Silindi", Toast.LENGTH_LONG).show();
    }

    // TODO LoadManager implement methods
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        if (id == 5){
            String[] projection = { CategoryEntry._ID, CategoryEntry.COLUMN_CATEGORY };
            //sortOrder : bu sıralamayı neye göre yapacağını belirler, provider' da bu değerin işlendiğine dikkat et!
            return new CursorLoader(this, CategoryEntry.COTNENT_URI, projection, null, null, CategoryEntry._ID + " DESC"); //tüm kategorileri getir
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        categoryCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        categoryCursorAdapter.swapCursor(null);
    }
}
