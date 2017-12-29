package com.example.bakdag.rxjavaandrxandroidapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.bakdag.rxjavaandrxandroidapp.R;
import com.example.bakdag.rxjavaandrxandroidapp.ui.adapters.SimpleStringAdapter;
import com.example.bakdag.rxjavaandrxandroidapp.data.RestClient;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by bakdag on 29.12.2017.
 */

public class BooksActivity extends AppCompatActivity {

    private Disposable bookSubscription;
    private RecyclerView booksRecyclerView;
    private ProgressBar progressBar;
    private SimpleStringAdapter stringAdapter;
    private RestClient restClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restClient = new RestClient(this);
        configureLayout();
        createObservable();
    }

    private void createObservable(){
        Observable<List<String>> booksObservable = Observable.fromCallable(() -> restClient.getFavoriteBooks());
        bookSubscription = booksObservable.subscribeOn(Schedulers.io())
                                          .observeOn(AndroidSchedulers.mainThread())
                                          .subscribe( strings -> displayBooks(strings));
    }

    private void configureLayout() {
        setContentView(R.layout.activity_book);
        progressBar = (ProgressBar) findViewById(R.id.loader);
        booksRecyclerView = (RecyclerView) findViewById(R.id.books_list);
        booksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        stringAdapter = new SimpleStringAdapter(this);
        booksRecyclerView.setAdapter(stringAdapter);
    }

    private void displayBooks(List<String> books) {
        stringAdapter.setStrings(books);
        progressBar.setVisibility(View.GONE);
        booksRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(bookSubscription != null && !bookSubscription.isDisposed()){
            bookSubscription.dispose();
        }
    }
}
