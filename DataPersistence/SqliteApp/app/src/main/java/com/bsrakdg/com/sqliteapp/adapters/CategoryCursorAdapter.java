package com.bsrakdg.com.sqliteapp.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.bsrakdg.com.sqliteapp.R;
import com.bsrakdg.com.sqliteapp.db.NoteContract;

/**
 * Created by bakdag on 13.02.2018.
 */

public class CategoryCursorAdapter extends CursorAdapter {

    public CategoryCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_category, viewGroup, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor){
        TextView txtCategory = (TextView) view.findViewById(R.id.txtCategory);
        txtCategory.setText(cursor.getString(cursor.getColumnIndex(NoteContract.CategoryEntry.COLUMN_CATEGORY)));
    }
}
