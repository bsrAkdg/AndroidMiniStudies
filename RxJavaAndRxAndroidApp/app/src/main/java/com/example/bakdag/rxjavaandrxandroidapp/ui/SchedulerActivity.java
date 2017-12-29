package com.example.bakdag.rxjavaandrxandroidapp.ui;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bakdag.rxjavaandrxandroidapp.R;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by bakdag on 29.12.2017.
 */

public class SchedulerActivity extends AppCompatActivity {

    // java.util.Callable aynı Runnable gibidir ama bir değer döndürüp bir hata da yakalayabilir.
    // Bu Activit' de uzun süren bir işlem boyunca UI da progress gösterilecek, işlem bittikten sonra UI update edilecek.

    private Disposable subscription;
    private ProgressBar progressBar;
    private TextView messagearea;
    private View button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureLayout();
    }

    private void configureLayout() {
        setContentView(R.layout.activity_scheduler);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        messagearea = (TextView) findViewById(R.id.messagearea);
        button  = findViewById(R.id.scheduleLongRunningOperation);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//              progressBar.setVisibility(View.VISIBLE);
                Observable.fromCallable(callable).
                        subscribeOn(Schedulers.io()).
                        observeOn(AndroidSchedulers.mainThread()).
                        doOnSubscribe(disposable ->
                                {
                                    progressBar.setVisibility(View.VISIBLE);
                                    button.setEnabled(false);
                                    messagearea.setText(messagearea.getText().toString() +"\n" +"Progressbar set visible" );
                                }
                        ).
                        subscribe(getDisposableObserver());
            }
        });
    }
    Callable<String> callable = new Callable<String>() {
        @Override
        public String call() throws Exception {
            return doSomethingLong();
        }
    };

    public String doSomethingLong(){
        SystemClock.sleep(1000);
        return "Hello";
    }
    //Observer veri akışını izler
    private DisposableObserver<String> getDisposableObserver(){
        return new DisposableObserver<String>() {
            @Override
            public void onNext(String s) {
                messagearea.setText(messagearea.getText().toString() +"\n" +"OnComplete" );
                progressBar.setVisibility(View.INVISIBLE);
                button.setEnabled(true);
                messagearea.setText(messagearea.getText().toString() +"\n" +"Hidding Progressbar" );
            }

            @Override
            public void onError(Throwable e) {
                messagearea.setText(messagearea.getText().toString() +"\n" +"OnError" );
                progressBar.setVisibility(View.INVISIBLE);
                button.setEnabled(true);
                messagearea.setText(messagearea.getText().toString() +"\n" +"Hidding Progressbar" );
            }

            @Override
            public void onComplete() {
                messagearea.setText(messagearea.getText().toString() +"\n" +"onNext " + "MESSAGE ONCOMPLETE" );
            }
        };
    }

}
