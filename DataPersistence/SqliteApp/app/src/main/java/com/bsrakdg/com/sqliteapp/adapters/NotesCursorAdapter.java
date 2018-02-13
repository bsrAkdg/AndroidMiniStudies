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
 * Created by bakdag on 12.02.2018.
 */

public class NotesCursorAdapter extends CursorAdapter {

    public NotesCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_note, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView txtNote = (TextView) view.findViewById(R.id.txtNote);
        txtNote.setText(cursor.getString(cursor.getColumnIndex(NoteContract.NoteEntry.COLUMN_NOTE)));
    }
}
