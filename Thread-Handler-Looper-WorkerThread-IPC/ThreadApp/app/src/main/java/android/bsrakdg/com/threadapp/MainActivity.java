package android.bsrakdg.com.threadapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    //başlat diyince başlayan durdur diyince duran bir sn arayla çalışan bir döngü oluşturalım. Döngü başladıktan sonra UI kitlenir(main thread).
    //Uzun sürecek bir işlemim varsa UI kitlenmemesi iki türlü çözüm üretebilirim
    //1. Thread
    private String TAG = "MainActivity";
    boolean isExecute = true;
    int loopCount = 0;
    Button btnStart, btnStop;

    void init(){
        btnStart = (Button) findViewById(R.id.btnStart);
        btnStop = (Button) findViewById(R.id.btnStop);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startThread();
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopThread();
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    void startThread(){
        isExecute = true;
        while (isExecute){
            try {
                Thread.sleep(1000);
                Log.e(TAG, "Döngü sayısı : " + loopCount + " Thread adı : " + Thread.currentThread().getName());
                loopCount++;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    void stopThread(){
        isExecute = false;
    }

}
