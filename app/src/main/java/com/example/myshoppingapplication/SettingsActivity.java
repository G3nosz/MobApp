package com.example.myshoppingapplication;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Context;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    private Button firstButton, secondButton;
    private Context context = this;
    private Button deleteItem, _cancel;
    DataBase dataBaseHelper;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);

        firstButton = (Button) findViewById(R.id.pirmoB);
        secondButton = (Button) findViewById(R.id.antroB);

        firstButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                deleteProfile();
            }
        });
        secondButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                deleteDB();
            }
        });

    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(context, ProfileInformation.class);
        context.startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
    public void deleteProfile(){
        dialogBuilder = new AlertDialog.Builder(this);
        final  View popUpView = getLayoutInflater().inflate(R.layout.confirmation_popup, null);

        deleteItem = (Button) popUpView.findViewById(R.id.deleteYes);
        _cancel = (Button) popUpView.findViewById(R.id.deleteNo);

        dialogBuilder.setView(popUpView);
        dialog2 = dialogBuilder.create();
        dialog2.show();

        dataBaseHelper = new DataBase(SettingsActivity.this);

        deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataBaseHelper.ClearProfile();
                dialog2.dismiss();
            }
        });
        _cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog2.dismiss();
            }
        });
    }
    public void deleteDB(){
        dialogBuilder = new AlertDialog.Builder(this);
        final  View popUpView = getLayoutInflater().inflate(R.layout.confirmation_popup, null);

        deleteItem = (Button) popUpView.findViewById(R.id.deleteYes);
        _cancel = (Button) popUpView.findViewById(R.id.deleteNo);

        dialogBuilder.setView(popUpView);
        dialog2 = dialogBuilder.create();
        dialog2.show();

        dataBaseHelper = new DataBase(SettingsActivity.this);
        deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataBaseHelper.ClearDB();
                dialog2.dismiss();
            }
        });
        _cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog2.dismiss();
            }
        });
    }
}
