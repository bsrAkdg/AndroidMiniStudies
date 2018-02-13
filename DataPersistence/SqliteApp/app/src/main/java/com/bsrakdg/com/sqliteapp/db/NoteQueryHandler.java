package com.bsrakdg.com.sqliteapp.db;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;

/**
 * Created by bakdag on 13.02.2018.
 */

public class NoteQueryHandler extends AsyncQueryHandler {

    public NoteQueryHandler(ContentResolver cr) {
        super(cr);
    }
}
