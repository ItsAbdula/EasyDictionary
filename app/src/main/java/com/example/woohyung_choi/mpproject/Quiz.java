package com.example.woohyung_choi.mpproject;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;

import static com.example.woohyung_choi.mpproject.MainActivity.dbManager;

public class Quiz extends AppCompatActivity {

    TextView tv_x;
    TextView tv_o;
    TextView tv_countDown;
    TextView tv_word;
    ProgressBar progressBar;
    CountDownTimer countDownTimer;
    Button btn_mean1;
    Button btn_mean2;
    Button btn_mean3;
    Button btn_mean4;

    int i = 5;
    String[] words;
    String[] meanings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_activity);

        tv_x = (TextView)findViewById(R.id.X);
        tv_o = (TextView)findViewById(R.id.O);
        tv_countDown = (TextView)findViewById(R.id.countdown);
        tv_word = (TextView)findViewById(R.id.word);
        btn_mean1 = (Button)findViewById(R.id.mean1);
        btn_mean2 = (Button)findViewById(R.id.mean2);
        btn_mean3 = (Button)findViewById(R.id.mean3);
        btn_mean4 = (Button)findViewById(R.id.mean4);

        Log.d(this.getClass().getName(), String.valueOf(dbManager.getSize()));

        Intent intent = getIntent();
        words = intent.getStringArrayExtra("words");
        meanings = intent.getStringArrayExtra("meanings");

        tv_word.setText(words[0]);
        btn_mean1.setText(meanings[0]);
        btn_mean2.setText(meanings[1]);
        btn_mean3.setText(meanings[2]);
        btn_mean4.setText(meanings[3]);

        /*
        tv_word.setText(dbManager.getWord(new Random().nextInt(dbManager.getSize()- 1)));
        btn_mean1.setText(dbManager.getMeaning(new Random().nextInt(dbManager.getSize()- 1)));
        btn_mean2.setText(dbManager.getMeaning(new Random().nextInt(dbManager.getSize()- 1)));
        btn_mean3.setText(dbManager.getMeaning(new Random().nextInt(dbManager.getSize()- 1)));
        btn_mean4.setText(dbManager.getMeaning(new Random().nextInt(dbManager.getSize()- 1)));*/

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setProgress(i);
        countDownTimer = new CountDownTimer(5000, 1000){
            @Override
            public void onFinish() {
                i = 0;

                tv_x.setCursorVisible(true);
            }

            @Override
            public void onTick(long millisUntilFinished) {
                i--;
                progressBar.setProgress(i);
                tv_countDown.setText(progressBar.getProgress());
            }
        };
    }
}
