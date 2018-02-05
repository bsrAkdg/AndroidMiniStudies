package com.bsrakdg.com.sqliteapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    Spinner spnrCategories;
    ListView lstNotes;
    Toolbar toolbar;
    FloatingActionButton fab;

    void init(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        spnrCategories = (Spinner) findViewById(R.id.spnrCategories);
        lstNotes = (ListView) findViewById(R.id.lstNotes);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NoteActivity.class));
            }
        });

        setNotesAdapter();
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
}
