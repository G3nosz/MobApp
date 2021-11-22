package com.example.myshoppingapplication;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class Intro_Screen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_screen);

        Thread thread = new Thread(){
          @Override
          public void run() {
              try {
                  sleep(1500);
                  Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                  startActivity(intent);
                  overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                  finish();
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }
          }
        };
        thread.start();
    }
}
