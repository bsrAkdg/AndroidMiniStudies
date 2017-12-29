package com.example.bakdag.rxjavaandrxandroidapp.ui;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bakdag.rxjavaandrxandroidapp.R;
import com.example.bakdag.rxjavaandrxandroidapp.ui.adapters.SimpleStringAdapter;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RxJavaSimpleActivity extends AppCompatActivity {

    //Uzun süren bir işlemi(10sn)  ve sonunda 5 değeri dönen bir Observable oluşturalım.
    //Aynı süre zarfında UI' da Toas mesajı bastığımızda UI da bir kitlenme görünmeyecektir.

    RecyclerView colorListView;
    SimpleStringAdapter simpleStringAdapter;
    CompositeDisposable disposable = new CompositeDisposable(); // subscription koleksiyonundan kurtulmayı sağlar.
    public int value =0;

    //Bu Observable geriye Integer bir değer dönüyor ve on sn sürüyor.
    Observable<Integer> serverDownloadObservable = Observable.create(emitter -> {
        SystemClock.sleep(10000); // simulate delay
        emitter.onNext(5); //Geriye 5 değeri döner
        emitter.onComplete(); //Başarıyla tamamlanır
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjavasimple);

        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener( v -> {
            v.setEnabled(false); //işlem bitene kadar disable
            //tek kullanımlık bir obje döndüren subscribe
            Disposable subscribe = serverDownloadObservable.observeOn(AndroidSchedulers.mainThread()) //subscriber main threadde gözler
                                                            .subscribeOn(Schedulers.io()) //Observable main thread dışında çağrılırç
                                                            .subscribe( integer -> {
                                                                updateUI(integer); //işlem tamamlanıp değer döndü
                                                                v.setEnabled(true); //artık soonuç döndü enable
                                                            });
            disposable.add(subscribe);
        });
    }

    private void updateUI(int value){
        TextView textView = (TextView) findViewById(R.id.resultView);
        textView.setText(String.valueOf(value));
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (disposable != null && !disposable.isDisposed()){
            disposable.dispose();
        }
    }

    public void onClick(View view) {
        Toast.makeText(this, "Still active " + value++, Toast.LENGTH_SHORT).show();
    }
}
