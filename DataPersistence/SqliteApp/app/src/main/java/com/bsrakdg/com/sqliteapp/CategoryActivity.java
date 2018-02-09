package com.bsrakdg.com.sqliteapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

/**
 * Created by bakdag on 5.02.2018.
 */

public class CategoryActivity extends AppCompatActivity{

    ListView lstCategories;
    EditText edtTxtCategoryName;

    void init(){
        lstCategories = (ListView) findViewById(R.id.lstCategories);
        edtTxtCategoryName = (EditText) findViewById(R.id.edtTxtCategoryName);

        lstCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                edtTxtCategoryName.setText((String) lstCategories.getItemAtPosition(i));
            }
        });
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


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
