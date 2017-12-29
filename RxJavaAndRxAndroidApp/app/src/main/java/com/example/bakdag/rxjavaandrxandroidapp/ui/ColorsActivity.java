package com.example.bakdag.rxjavaandrxandroidapp.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.bakdag.rxjavaandrxandroidapp.R;
import com.example.bakdag.rxjavaandrxandroidapp.ui.adapters.SimpleStringAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * Created by bakdag on 29.12.2017.
 */

public class ColorsActivity extends AppCompatActivity {

    //Bir renk listesi almak için Observable bir yöntem kullanılır.

    RecyclerView colorListView;
    SimpleStringAdapter simpleStringAdapter;
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureLayout();
        createObservable();
    }

    private void configureLayout() {
        setContentView(R.layout.activity_colors);
        colorListView = (RecyclerView) findViewById(R.id.color_list);
        colorListView.setLayoutManager(new LinearLayoutManager(this));
        simpleStringAdapter = new SimpleStringAdapter(this);
        colorListView.setAdapter(simpleStringAdapter);
    }

    private void createObservable(){
        Observable<List<String>> listObservable = Observable.just(getColorList());
        disposable = listObservable.subscribe( colors -> simpleStringAdapter.setStrings(colors));
    }

    private static List<String> getColorList(){
        ArrayList<String> colors = new ArrayList<>();
        colors.add("red");
        colors.add("green");
        colors.add("blue");
        colors.add("pink");
        colors.add("brown");
        return colors;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (disposable!=null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}

