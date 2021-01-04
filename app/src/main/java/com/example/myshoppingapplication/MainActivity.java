package com.example.myshoppingapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    private Button myButton; //sita naudoti programai uzdaryti, nes nu tipo kad kazka darytu
    private Button secondActivityButton, thirdActivityButton;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        secondActivityButton = (Button) findViewById(R.id.secondActivityButton);
        thirdActivityButton = (Button) findViewById(R.id.profileAppButton);
        secondActivityButton.setOnClickListener(startSecondActivity);
        thirdActivityButton.setOnClickListener(startThirdActivity);
    }

    private void runSecondActivity(boolean flag) {
        Intent intent = new Intent(context, ListOfCarts.class);
        intent.putExtra("flag", flag);
        context.startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
    private void runThirdActivity(boolean flag) {
        Intent intent = new Intent(context, ProfileInformation.class);
        intent.putExtra("flag", flag);
        context.startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    View.OnClickListener startSecondActivity = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            runSecondActivity(true);
        }
    };

    View.OnClickListener startThirdActivity = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            runThirdActivity(true);
        }
    };

}