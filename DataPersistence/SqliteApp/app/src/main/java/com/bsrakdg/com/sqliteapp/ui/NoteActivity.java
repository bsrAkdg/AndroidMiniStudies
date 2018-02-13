package com.bsrakdg.com.sqliteapp.ui;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.bsrakdg.com.sqliteapp.R;
import com.bsrakdg.com.sqliteapp.db.NoteContract;
import com.bsrakdg.com.sqliteapp.db.NoteContract.NoteEntry;
import com.bsrakdg.com.sqliteapp.db.NoteContract.CategoryEntry;
import com.bsrakdg.com.sqliteapp.db.NoteQueryHandler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by bakdag on 5.02.2018.
 */

public class NoteActivity extends AppCompatActivity {

    EditText edtTxtNote, edtTxtCreateDate, edtTxtFinishDate;
    CheckBox chxDone;
    Spinner spnrCategory;
    Button btnDeleteNote;

    NoteQueryHandler noteQueryHandler;

    //veritabanındann kategorileri okuyup spinnerda gösterecek
    SimpleCursorAdapter categoryAdapter;
    Cursor categoryCursor;
    Calendar calendar;

    String strNewNote= "";
    String noteId, note, noteCreateDate, noteFinishDate, noteDone, noteCategoryId, noteCategory;
    int year, month, day;

    void init(){
        edtTxtNote = (EditText) findViewById(R.id.edtTxtNote);
        edtTxtCreateDate = (EditText) findViewById(R.id.edtTxtCreateDate);
        edtTxtFinishDate = (EditText) findViewById(R.id.edtTxtFinishDate);
        chxDone = (CheckBox) findViewById(R.id.chxDone);
        spnrCategory = (Spinner) findViewById(R.id.spnrCategory);
        btnDeleteNote = (Button) findViewById(R.id.btnDeleteNote);
        setCategoryAdapter();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        init();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //ekleme güncelleme işlemleri için
        noteQueryHandler = new NoteQueryHandler(getContentResolver());

        Intent incomingData = getIntent();
        strNewNote = incomingData.getStringExtra(NoteEntry._ID);

        if (strNewNote != null){
            //varolan notun güncelleme işlemi
            noteId = incomingData.getStringExtra(NoteEntry._ID);
            note = incomingData.getStringExtra(NoteEntry.COLUMN_NOTE);
            noteCreateDate = incomingData.getStringExtra(NoteEntry.COLUMN_CREATE_DATE);
            noteFinishDate = incomingData.getStringExtra(NoteEntry.COLUMN_FINISH_DATE);
            noteDone = incomingData.getStringExtra(NoteEntry.COLUMN_DONE);
            noteCategoryId = incomingData.getStringExtra(NoteEntry.COLUMN_CATEGORY_ID);
            noteCategory = incomingData.getStringExtra(CategoryEntry.COLUMN_CATEGORY);

            if (note!= null){
                edtTxtNote.setText(note);
            }
            if (noteCreateDate!= null){
                edtTxtCreateDate.setText(noteCreateDate);
            }
            if (noteFinishDate!= null){
                edtTxtFinishDate.setText(noteFinishDate);
            }
            if (noteDone != null && noteDone.equals("1")){
                chxDone.setChecked(true);
            }else{
                chxDone.setChecked(false);
            }
            if(noteCategoryId != null){
                spnrCategory.setSelection(Integer.parseInt(noteCategoryId)-1);
            }
        }else{
            //yeni notun güncelleme işlemi
            btnDeleteNote.setVisibility(View.GONE);
            edtTxtNote.setHint("Yeni notunuzu giriniz.");
        }

        //değişiklikleri kaydetme
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabSave);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ekleme ya da güncelleme yapılacak (handler ile)
                ContentValues contentValues = new ContentValues();
                contentValues.put(NoteEntry.COLUMN_NOTE, edtTxtNote.getText().toString());
                contentValues.put(NoteEntry.COLUMN_CATEGORY_ID, spnrCategory.getSelectedItemId());
                contentValues.put(NoteEntry.COLUMN_CREATE_DATE, edtTxtCreateDate.getText().toString());
                contentValues.put(NoteEntry.COLUMN_FINISH_DATE, edtTxtFinishDate.getText().toString());
                contentValues.put(NoteEntry.COLUMN_DONE, chxDone.isChecked()? 1 : 0);

                if (strNewNote == null){
                    //yeni not ekleniyor.
                    noteQueryHandler.startInsert(1, null, NoteEntry.COTNENT_URI, contentValues);
                    Toast.makeText(NoteActivity.this, "Not eklendi", Toast.LENGTH_LONG).show();
                }else{
                    //varolan not güncelleniyor.
                    String selection = NoteEntry._ID + "= ?";
                    String[] selectionArgs = {noteId};
                    noteQueryHandler.startUpdate(1, null, NoteEntry.COTNENT_URI, contentValues, selection, selectionArgs);
                    Toast.makeText(NoteActivity.this, "Not güncellendi", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    void setCategoryAdapter(){

        //kategorileri database' den alıp gösterir
        String[] projection = {CategoryEntry._ID, CategoryEntry.COLUMN_CATEGORY};
        String sortOrder = "_id DESC";

        categoryCursor = getContentResolver().query(CategoryEntry.COTNENT_URI, projection, null, null, sortOrder);

        categoryAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, categoryCursor,
                new String[]{"category"}, new int[]{android.R.id.text1}, 0);

        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //spinner' ın ok tuşunun olduğu alan

        spnrCategory.setAdapter(categoryAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    public void chooseDate(final View view) {
        calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                calendar.set(y, m, d);
                SimpleDateFormat format=new SimpleDateFormat("dd-MM-yyyy");
                String date = format.format(calendar.getTime());

                if (view.getId() == R.id.btnCreateDate){
                    edtTxtCreateDate.setText(date);
                }else{
                    edtTxtFinishDate.setText(date);
                }
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void deleteNote(View view) {
        String selection = NoteEntry._ID + "= ?";
        String[] selectionArgs = {noteId};
        noteQueryHandler.startDelete(1, null, NoteEntry.COTNENT_URI, selection, selectionArgs );
        Toast.makeText(NoteActivity.this, "Not silindi", Toast.LENGTH_LONG).show();
    }
}
