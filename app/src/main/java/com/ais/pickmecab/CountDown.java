package com.ais.pickmecab;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class CountDown extends CountDownTimer {
Button btn_text;
Context mainactivity;
          public CountDown(long millisInFuture, long countDownInterval, Button txt, Context cont) {
            super(millisInFuture, countDownInterval);
              btn_text = txt;
              mainactivity=cont;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long ms = millisUntilFinished;
            String text = String.format("%02d\' %02d\"",
                    TimeUnit.MILLISECONDS.toMinutes(ms) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(ms)),
                    TimeUnit.MILLISECONDS.toSeconds(ms) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(ms)));
            btn_text.setText("Job Arrived");
        }

        @Override
        public void onFinish() {
            //btn_text.setText("Waiting Jobs");
            ((MainActivity)mainactivity).resetJobSheet();


        }
}
