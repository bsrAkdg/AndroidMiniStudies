package com.example.bakdag.rxjavaandrxandroidapp.data;

import android.content.Context;
import android.os.SystemClock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bakdag on 29.12.2017.
 */

public class RestClient {
    //Fake REST CLIENT olduğunu varsayalım
    private Context mContext;

    public RestClient(Context context) {
        mContext = context;
    }

    public List<String> getFavoriteBooks() {
        SystemClock.sleep(8000);//bağlantı bekleme süresi
        return createBooks();
    }

    public List<String> getFavoriteBooksWithException() {
        SystemClock.sleep(8000);//bağlantı bekleme süresi
        throw new RuntimeException("Failed to load");
    }

    private List<String> createBooks() {
        List<String> books = new ArrayList<>();
        books.add("Lord of the Rings");
        books.add("The dark elf");
        books.add("Eclipse Introduction");
        books.add("History book");
        books.add("Der kleine Prinz");
        books.add("7 habits of highly effective people");
        books.add("Other book 1");
        books.add("Other book 2");
        books.add("Other book 3");
        books.add("Other book 4");
        books.add("Other book 5");
        books.add("Other book 6");
        return books;
    }
}
