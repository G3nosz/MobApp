package com.example.myshoppingapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class ProfileInformation extends AppCompatActivity {
    DataBase dataBaseHelper;
    private TextView first, second, third, fourth;
    Profile laikinas;
    private Context context = this;
    private Button pirmas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        first = (TextView) findViewById(R.id.profilWievFirstValue);
        second = (TextView) findViewById(R.id.profilWievSecondValue);
        third = (TextView) findViewById(R.id.profilWievThirdValue);
        fourth = (TextView) findViewById(R.id.profilWievFourthValue);


        dataBaseHelper = new DataBase(ProfileInformation.this);

        laikinas = dataBaseHelper.getProfileInfo();
        first.setText(String.valueOf(laikinas.getSpent()));
        second.setText(String.valueOf(laikinas.getSaved()));
        third.setText(String.valueOf(laikinas.getCarts()));
        fourth.setText(String.valueOf(laikinas.getItems()));
        pirmas = (Button) findViewById(R.id.settings);

        pirmas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SettingsActivity.class);
                intent.putExtras(intent);
                context.startActivity(intent);

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        MenuItem item = menu.findItem(R.id.menu1);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();

        if(id == R.id.menu1){
            runCartActivity();
        }

        return super.onOptionsItemSelected(item);
    }
    private void runCartActivity() {
        Intent intent = new Intent(context, ListOfCarts.class);
        intent.putExtras(intent);
        context.startActivity(intent);
    }
}
