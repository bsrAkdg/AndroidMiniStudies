package com.bsrakdg.com.sqliteapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.bsrakdg.com.sqliteapp.R;

/**
 * Created by bakdag on 5.02.2018.
 */

public class NoteActivity extends AppCompatActivity {

    EditText edtTxtNote;

    void init(){
        edtTxtNote = (EditText) findViewById(R.id.edtTxtNote);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        init();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

/*        if (!getIntent().getExtras().getString("noteContent","").equals("")){
            edtTxtNote.setText(getIntent().getExtras().getString("noteContent"));
        }*/

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
