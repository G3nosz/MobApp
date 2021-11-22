package com.example.myshoppingapplication;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.os.Bundle;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class ListOfCarts extends AppCompatActivity {
    private ListView myListView;
    private CartListAdapter cartAdapter;
    private Context context = this;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog, dialog1, dialog2, dialog3;
    private EditText newcartName, newcartCost;
    private Button newcartSave, newcartCancel, editCart, deleteCart, _cancel;
    private Button newcartDatePicker, newcartTimePicker;
    private TextView wholeCost, dateView, timeView;

    DataBase dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cartlist);
        createNotificationChannel();
        myListView = (ListView) findViewById(R.id.cartListView); //cartListView
        wholeCost = (TextView) findViewById(R.id.sumo_pozicija);
        dataBaseHelper = new DataBase(ListOfCarts.this);
        ShowCartsOnListView(dataBaseHelper);

        double cost = dataBaseHelper.getAllCartsCost();
        wholeCost.setText(String.valueOf(cost));

        myListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                NewCart value = cartAdapter.getItem(position);
                selectAction(value);

                return true;
            }
        });
        //on simple click go to cart
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                NewCart value = cartAdapter.getItem(position);
                runSecondActivity(true, value);
            }
        });
    }
    private void updateCartsCost(){
        dataBaseHelper = new DataBase(ListOfCarts.this);
        ShowCartsOnListView(dataBaseHelper);
        double cost = dataBaseHelper.getAllCartsCost();
        wholeCost.setText(String.valueOf(cost));
    }
    private void selectAction(NewCart value){
        final  View popUpView = getLayoutInflater().inflate(R.layout.list_of_items_action_layout, null);
        final PopupWindow optionspu = new PopupWindow(popUpView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        optionspu.setAnimationStyle(R.style.popup_window_animation);
        optionspu.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        optionspu.setFocusable(true);
        optionspu.setOutsideTouchable(true);
        optionspu.update(0, 150, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        optionspu.showAtLocation(popUpView, Gravity.BOTTOM, 0, 300);

        deleteCart = (Button) popUpView.findViewById(R.id.deleteItem);
        editCart = (Button) popUpView.findViewById(R.id.editItem);
        deleteCart.setBackgroundColor(getResources().getColor(R.color.Buttonred));

        final NewCart value1 = value;
        editCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editCart(value1);
                ShowCartsOnListView(dataBaseHelper);
                optionspu.dismiss();
            }
        });
        deleteCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCart(value1);
                ShowCartsOnListView(dataBaseHelper);
                optionspu.dismiss();
            }
        });
    }
    public void deleteCart(NewCart value){
        dialogBuilder = new AlertDialog.Builder(this);
        final  View popUpView = getLayoutInflater().inflate(R.layout.confirmation_popup, null);
        deleteCart = (Button) popUpView.findViewById(R.id.deleteYes);
        _cancel = (Button) popUpView.findViewById(R.id.deleteNo);
        dialogBuilder.setView(popUpView);
        dialog2 = dialogBuilder.create();
        dialog2.show();

        final NewCart value1 = value;
        deleteCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataBaseHelper.deleteCart(value1);
                ShowCartsOnListView(dataBaseHelper);
                updateCartsCost();
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

    private void ShowCartsOnListView(DataBase dataBaseHelper) {
        cartAdapter = new CartListAdapter(ListOfCarts.this, dataBaseHelper.getAllCarts());
        myListView.setAdapter(cartAdapter);
    }
    private void ShowCartsOnListView(DataBase dataBaseHelper, String name) {
        cartAdapter = new CartListAdapter(ListOfCarts.this, dataBaseHelper.getAllCarts(name));
        myListView.setAdapter(cartAdapter);
    }

    private void runSecondActivity(boolean flag, NewCart temp) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(context, ListOfItems.class);
        bundle.putInt("cartID", temp.getID());
        intent.putExtras(bundle);
        context.startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right); //Animation
    }
    private void runThirdActivity(boolean flag) {
        Intent intent = new Intent(context, ProfileInformation.class);
        intent.putExtras(intent);
        context.startActivity(intent);
        overridePendingTransition(R.anim.from_middle, R.anim.to_middle); //Animation
    }


    @Override
    public void onBackPressed(){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.first_menu, menu);
        MenuItem item = menu.findItem(R.id.cart_search);

        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
               return false;
           }

            @Override
            public boolean onQueryTextChange(String newText) {
                dataBaseHelper = new DataBase(ListOfCarts.this);
                ShowCartsOnListView(dataBaseHelper, newText);
                return false;
            }
        });
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();

        if(id == R.id.menu1){
            runThirdActivity(true);
            //Toast.makeText(getApplicationContext(), "Pressed icon", Toast.LENGTH_LONG).show();
        }
        if(id == R.id.menu2){
            createNewCart();
        }

        return super.onOptionsItemSelected(item);
    }
    public void dateButton(final NewCart temp){
        int YEAR = Calendar.getInstance().get(Calendar.YEAR);
        int MONTH = Calendar.getInstance().get(Calendar.MONTH);
        int DAY = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                temp.setYear(year);
                int m = month + 1;//months is from 0
                temp.setMonth(m);
                temp.setDay(day);
                String text = year + "/" + m + "/" + day;
                dateView.setText(text);
            }
        }, YEAR, MONTH, DAY);
        datePickerDialog.show();
    }

    public void timeButton(final NewCart temp) {
        Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(Calendar.HOUR);
        int MIN = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                temp.setHour(i);
                temp.setMinute(i1);
                String time = i + ":" + i1;
                timeView.setText(time);
            }
        }, HOUR, MIN, true);
        timePickerDialog.show();
    }

    public void createNewCart(){
        dialogBuilder = new AlertDialog.Builder(this);
        final  View popUpView = getLayoutInflater().inflate(R.layout.popup, null);
        newcartName = (EditText) popUpView.findViewById(R.id.newCartName);
        newcartCost = (EditText) popUpView.findViewById(R.id.newCartLimit);
        newcartDatePicker = (Button) popUpView.findViewById(R.id.dateReminder);
        newcartTimePicker = (Button) popUpView.findViewById(R.id.timeReminder);
        dateView = (TextView)  popUpView.findViewById(R.id.dateText);
        timeView = (TextView)  popUpView.findViewById(R.id.timeText);

        newcartSave = (Button) popUpView.findViewById(R.id.saveButton);
        newcartCancel = (Button) popUpView.findViewById(R.id.cancelButton);

        dialogBuilder.setView(popUpView);
        dialog = dialogBuilder.create();
        dialog.show();
        final NewCart tmp = new NewCart();
        newcartDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateButton(tmp);
            }
        });

        newcartTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeButton(tmp);
            }
        });
        newcartSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NewCart temp = new NewCart();
                try{

                    if(newcartName.getText().toString() == ""){
                        Toast.makeText(getApplicationContext(), "Missing name", Toast.LENGTH_LONG).show();
                    }
                    else{
                        temp.setName(newcartName.getText().toString());
                        temp.setLimit(Double.parseDouble(newcartCost.getText().toString()));
                        temp.setYear(tmp.getYear());
                        temp.setMonth(tmp.getMonth());
                        temp.setDay(tmp.getDay());
                        temp.setHour(tmp.getHour());
                        temp.setMinute(tmp.getMinute());

                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Cart created", Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e){
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Failed to create", Toast.LENGTH_LONG).show();
                    temp = new NewCart();
                    temp.setName("??#error#??");
                }

                DataBase dataBaseHelper = new DataBase(ListOfCarts.this);
                boolean success = dataBaseHelper.addCart(temp);
                if(success){ }
                else{
                    Toast.makeText(getApplicationContext(), "Failed to add to Data Base", Toast.LENGTH_LONG).show();
                }
                notificationStart(tmp); //<----------------start notification timer
                ShowCartsOnListView(dataBaseHelper);
            }
        });
        newcartCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Canceled", Toast.LENGTH_LONG).show();
            }
        });
    }
    public void notificationStart(NewCart cart){
        DataBase dataBaseHelper = new DataBase(ListOfCarts.this);
        int success = dataBaseHelper.loadCartData(cart);
        if(success == 0){
        }
        else{
            Toast.makeText(getApplicationContext(), "Failed to load info", Toast.LENGTH_LONG).show();
        }
        Intent intent = new Intent(this, ReminderBroadcast.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,intent,0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        Log.i("Current time", String.valueOf(Calendar.getInstance().getTimeInMillis()));
        calendar.set(Calendar.YEAR, cart.getYear());
        calendar.set(Calendar.MONTH, (cart.getMonth() - 1));
        calendar.set(Calendar.DAY_OF_MONTH, cart.getDay());
        calendar.set(Calendar.HOUR_OF_DAY, cart.getHour());
        calendar.set(Calendar.MINUTE, cart.getMinute());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Log.i("Current time", String.valueOf(calendar.getTimeInMillis()));


        alarmManager.set(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), pendingIntent);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("ShoppingApp", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    public void editCart(final NewCart value){
        dialogBuilder = new AlertDialog.Builder(this);
        final  View popUpView = getLayoutInflater().inflate(R.layout.popup, null);
        newcartName = (EditText) popUpView.findViewById(R.id.newCartName);
        newcartCost = (EditText) popUpView.findViewById(R.id.newCartLimit);
        newcartDatePicker = (Button) popUpView.findViewById(R.id.dateReminder);
        newcartTimePicker = (Button) popUpView.findViewById(R.id.timeReminder);
        dateView = (TextView)  popUpView.findViewById(R.id.dateText);
        timeView = (TextView)  popUpView.findViewById(R.id.timeText);

        final String pirmas = String.valueOf(value.getName());
        final String antras = String.valueOf(value.getLimit());

        DataBase dataBaseHelper = new DataBase(ListOfCarts.this);
        int success = dataBaseHelper.loadCartData(value);
        if(success == 0){
        }
        else{
            Toast.makeText(getApplicationContext(), "Failed to load info", Toast.LENGTH_LONG).show();
        }
        newcartName.setText(String.valueOf(value.getName()));
        newcartCost.setText(String.valueOf(value.getLimit()));
        Log.i("Name", pirmas);

        final String y = String.valueOf(value.getYear());
        final String m = String.valueOf(value.getMonth());
        final String d = String.valueOf(value.getDay());
        final String h = String.valueOf(value.getHour());
        final String min = String.valueOf(value.getMinute());
        final String date = y + "/" + m + "/" + d;
        final String time = h + ":" + min;
        dateView.setText(date);
        timeView.setText(time);

        newcartSave = (Button) popUpView.findViewById(R.id.saveButton);
        newcartCancel = (Button) popUpView.findViewById(R.id.cancelButton);

        dialogBuilder.setView(popUpView);
        dialog3 = dialogBuilder.create();
        dialog3.show();
        final NewCart tmp = new NewCart();
        newcartDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateButton(tmp);
            }
        });
        newcartTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeButton(tmp);
            }
        });
        newcartSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NewCart temp = new NewCart();
                try{

                    temp.setName(newcartName.getText().toString());
                    temp.setLimit(Double.parseDouble(newcartCost.getText().toString()));
                    if(tmp.getYear() == 0 && tmp.getMinute() == 0 && tmp.getDay() == 0){ //if time is not set
                        temp.setYear(value.getYear());
                        temp.setMonth(value.getMonth());
                        temp.setDay(value.getDay());
                    }
                    else{
                        temp.setYear(tmp.getYear());
                        temp.setMonth(tmp.getMonth());
                        temp.setDay(tmp.getDay());
                    }
                    if(tmp.getHour() == 0 && tmp.getMinute() == 0){
                        temp.setHour(value.getHour());
                        temp.setMinute(value.getMinute());
                    }
                    else{
                        temp.setHour(tmp.getHour());
                        temp.setMinute(tmp.getMinute());
                    }
                    dialog3.dismiss();
                } catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Fill all fields", Toast.LENGTH_LONG).show();
                }

                DataBase dataBaseHelper = new DataBase(ListOfCarts.this);
                boolean success = dataBaseHelper.updateCart(temp, pirmas);
                if(success){
                    Toast.makeText(getApplicationContext(), "Cart updated", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Failed to update", Toast.LENGTH_LONG).show();
                }
                notificationStart(temp);
                ShowCartsOnListView(dataBaseHelper);
            }
        });

        newcartCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog3.dismiss();
                Toast.makeText(getApplicationContext(), "Canceled", Toast.LENGTH_LONG).show();
            }
        });
    }
}