package com.example.myshoppingapplication;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.os.Bundle;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ListOfItems extends AppCompatActivity {
    private ListView myListView;
    private ItemListAdapter itemAdapter;
    private Context context = this;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog2, dialog3, dialog4, dialog9;
    private EditText newitem_name, newitem_cost, newitem_count;
    private Button newitem_save, newitem_cancel, newitem_photo, take_image, upload_image;
    private Button delete_item, edit_item, _cancel;
    private ImageView newitemView;
    private ProgressBar bar1;
    private int proges_bar_proc = 0;

    DataBase dataBaseHelper;
    int cartID;

    private Uri imageFilePath;
    private Bitmap imageToStore;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int PICK_IMAGE_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.itemlist);
        myListView = (ListView) findViewById(R.id.itemListView);

        try {
            Bundle bundle = getIntent().getExtras();
            cartID = bundle.getInt("cartID");
        } catch (Exception e){
            cartID = -1;
        }

        dataBaseHelper = new DataBase(ListOfItems.this);
        ShowCartsOnListView(dataBaseHelper);

        myListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                NewItem value = itemAdapter.getItem(position);
                actionSelect(value);
                return true;
            }
        });
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                NewItem value = itemAdapter.getItem(i);
                changeItemState(value, cartID);

                dataBaseHelper = new DataBase(ListOfItems.this);
                bar1 = (ProgressBar) findViewById(R.id.determinateBar);
                int all_items_count = 0;
                int true_count = 0;
                List<NewItem> temp = dataBaseHelper.getAllItems(cartID);
                for (NewItem element : temp) {
                    if(element.getState() == 1){ //surenku visus kur state yra true, tai reiskia, kad jis paimtas
                        true_count += 1;
                    }
                    all_items_count += 1;
                }
                if(all_items_count == 0){
                    proges_bar_proc = 0;
                }
                else {
                    proges_bar_proc = true_count * 100 / all_items_count;
                }

                bar1.setProgress(proges_bar_proc);
                int cartState = dataBaseHelper.getCartState(cartID);

                if(proges_bar_proc == 100){
                    if(cartState == 0){
                        int test = dataBaseHelper.changeCartState(1, cartID);
                        int test2 = dataBaseHelper.updateProfile(cartID);
                    }
                }
                else if(proges_bar_proc < 100){
                    if(cartState == 1){
                        int test = dataBaseHelper.changeCartState(0, cartID);
                    }
                }
                ShowCartsOnListView(dataBaseHelper);
            }
        });

    }
    private void changeItemState(NewItem item, int idCart){
        DataBase dataBaseHelper = new DataBase(ListOfItems.this);
        boolean success = dataBaseHelper.changeItemState(item, idCart); //metodas state pakeisti
    }
    private void runThirdActivity(boolean flag) {
        Intent intent = new Intent(context, ProfileInformation.class);
        intent.putExtras(intent);
        context.startActivity(intent);
        overridePendingTransition(R.anim.from_middle, R.anim.to_middle); //Animation
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.itemlistmenu, menu);

        MenuItem item = menu.findItem(R.id.item_search1);

        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                dataBaseHelper = new DataBase(ListOfItems.this);
                ShowItemsOnListView(dataBaseHelper, newText);
                return false;
            }
        });
        return true;
    }
    private void ShowItemsOnListView(DataBase dataBaseHelper, String name) {
        itemAdapter = new ItemListAdapter(ListOfItems.this, dataBaseHelper.getSelectedItems(cartID, name));
        myListView.setAdapter(itemAdapter);
    }
    //Tai cia kaip per apatinius navigacijos laukelius spaus atgal per naujo uzkraus ListOfCarts kad atsinaujintu duomenys
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(context, ListOfCarts.class);
        context.startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void ShowCartsOnListView(DataBase dataBaseHelper) {
        itemAdapter = new ItemListAdapter(ListOfItems.this, dataBaseHelper.getAllItems(cartID));
        myListView.setAdapter(itemAdapter);

        dataBaseHelper = new DataBase(ListOfItems.this);
        bar1 = (ProgressBar) findViewById(R.id.determinateBar);
        int all_items_count = 0;
        int true_count = 0;
        List<NewItem> temp = dataBaseHelper.getAllItems(cartID);
        for (NewItem element : temp) {
            if(element.getState() == 1){
                true_count += 1;
            }
            all_items_count += 1;
        }

        if(all_items_count == 0){
            proges_bar_proc = 0;
        }
        else {
            proges_bar_proc = true_count * 100 / all_items_count;
        }

        int cartState = dataBaseHelper.getCartState(cartID);

        if(proges_bar_proc == 100){
            if(cartState == 0){
                int test = dataBaseHelper.changeCartState(1, cartID);
                int test2 = dataBaseHelper.updateProfile(cartID);
            }
        }
        else if(proges_bar_proc < 100){
            if(cartState == 1){
                int test = dataBaseHelper.changeCartState(0, cartID);
            }
        }

        bar1.setProgress(proges_bar_proc);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();

        if(id == R.id.menu1){
            runThirdActivity(true);
        }
        if(id == R.id.menu2){
            createNewItem();
        }

        return super.onOptionsItemSelected(item);
    }
    //---------------------------------------------------------------------------------------------------------------
    public void actionSelect(NewItem value){
        //dialogBuilder = new AlertDialog.Builder(this);
        final  View popUpView = getLayoutInflater().inflate(R.layout.list_of_items_action_layout, null);
        final PopupWindow optionspu = new PopupWindow(popUpView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        optionspu.setAnimationStyle(R.style.popup_window_animation);
        optionspu.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        optionspu.setFocusable(true);
        optionspu.setOutsideTouchable(true);
        optionspu.update(0, 150, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        optionspu.showAtLocation(popUpView, Gravity.BOTTOM, 0, 300);

        delete_item = (Button) popUpView.findViewById(R.id.deleteItem);
        edit_item = (Button) popUpView.findViewById(R.id.editItem);
        delete_item.setBackgroundColor(getResources().getColor(R.color.Buttonred));

        final NewItem value1 = value;
        edit_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editItem(value1);
                ShowCartsOnListView(dataBaseHelper);
                optionspu.dismiss();
            }
        });
        delete_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteItem(value1);
                optionspu.dismiss();
            }
        });
    }
    public void deleteItem(NewItem value){
        dialogBuilder = new AlertDialog.Builder(this);
        final  View popUpView = getLayoutInflater().inflate(R.layout.confirmation_popup, null);

        delete_item = (Button) popUpView.findViewById(R.id.deleteYes);
        _cancel = (Button) popUpView.findViewById(R.id.deleteNo);

        dialogBuilder.setView(popUpView);
        dialog2 = dialogBuilder.create();
        dialog2.show();

        final NewItem value1 = value;
        delete_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataBaseHelper.deleteItem(cartID, value1);
                ShowCartsOnListView(dataBaseHelper);
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
    public void selectImageAction(){
        dialogBuilder = new AlertDialog.Builder(this);
        final  View popUpView = getLayoutInflater().inflate(R.layout.image_select, null);

        take_image = (Button) popUpView.findViewById(R.id.takePicture);
        upload_image = (Button) popUpView.findViewById(R.id.uploadImage);


        dialogBuilder.setView(popUpView);
        dialog9 = dialogBuilder.create();
        dialog9.show();

        upload_image.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent objectIntent = new Intent();
                objectIntent.setType("image/*");
                objectIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(objectIntent, PICK_IMAGE_REQUEST);

                //tai tuomet sitoje vietoje reikia kviest tuos 4 kartus iskarto
                dialog9.dismiss();
            }
        });
        take_image.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                } catch (ActivityNotFoundException e){
                    //error
                }
                dialog9.dismiss();
            }
        });

    }

    public void createNewItem(){
        dialogBuilder = new AlertDialog.Builder(this);
        final  View popUpView = getLayoutInflater().inflate(R.layout.popupitem, null);

        newitem_name = (EditText) popUpView.findViewById(R.id.newItemName);
        newitem_count = (EditText) popUpView.findViewById(R.id.newItemCount);
        newitem_cost = (EditText) popUpView.findViewById(R.id.newItemCost);
        newitemView = (ImageView) popUpView.findViewById(R.id.newicon); //laukau nufotografuota nuotrauka

        newitem_photo = (Button) popUpView.findViewById(R.id.uploadImage); //fotografavimo mygtukas

        newitem_save = (Button) popUpView.findViewById(R.id.saveButton);
        newitem_cancel = (Button) popUpView.findViewById(R.id.cancelButton);

        dialogBuilder.setView(popUpView);
        dialog3 = dialogBuilder.create();
        dialog3.show();

        newitem_photo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                selectImageAction();
            }
        });

        newitem_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //aprasomas save mygtukas, tai tiesiog sukuriamas tuscias cart
                //ir jam priskiriamas pavadinimas
                NewItem temp = new NewItem();
                try{
                    temp.setName(newitem_name.getText().toString());
                    temp.setCost(Double.parseDouble(newitem_cost.getText().toString())); //reikia gauti cost
                    temp.setCount(Integer.parseInt(newitem_count.getText().toString()));
                    temp.setImage(imageToStore); //nuotrauka saugoma

                    dialog3.dismiss();

                } catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Fill all fields or select image", Toast.LENGTH_LONG).show();
                }

                DataBase dataBaseHelper = new DataBase(ListOfItems.this);
                boolean success = dataBaseHelper.addItem(temp, cartID);
                if(success){
                    Toast.makeText(getApplicationContext(), "Item created", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Failed to create item", Toast.LENGTH_LONG).show();
                }

                ShowCartsOnListView(dataBaseHelper);

            }
        });

        newitem_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog3.dismiss();
                Toast.makeText(getApplicationContext(), "Canceled", Toast.LENGTH_LONG).show();

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageToStore = (Bitmap) extras.get("data");
            newitemView.setImageBitmap(imageToStore);
        } else {
            try {
                super.onActivityResult(requestCode, resultCode, data);
                if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                    imageFilePath = data.getData();
                    imageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(), imageFilePath);
                    newitemView.setImageBitmap(imageToStore);
                }
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void editItem(NewItem value){
        dialogBuilder = new AlertDialog.Builder(this);
        final  View popUpView = getLayoutInflater().inflate(R.layout.popupitem, null);
        newitem_name = (EditText) popUpView.findViewById(R.id.newItemName);
        newitem_count = (EditText) popUpView.findViewById(R.id.newItemCount);
        newitem_cost = (EditText) popUpView.findViewById(R.id.newItemCost);
        final String pirmas = String.valueOf(value.getName());
        final String antras = String.valueOf(value.getCount());
        final String trecias = String.valueOf(value.getCost());
        final Bitmap vaizdas = value.getImage();

        newitemView = (ImageView) popUpView.findViewById(R.id.newicon); //laukau nufotografuota nuotrauka

        newitem_photo = (Button) popUpView.findViewById(R.id.uploadImage); //fotografavimo mygtukas

        newitem_name.setText(String.valueOf(value.getName()));
        newitem_count.setText(String.valueOf(value.getCount()));
        newitem_cost.setText(String.valueOf(value.getCost()));
        newitemView.setImageBitmap(vaizdas);

        newitem_save = (Button) popUpView.findViewById(R.id.saveButton);
        newitem_cancel = (Button) popUpView.findViewById(R.id.cancelButton);

        dialogBuilder.setView(popUpView);
        dialog4 = dialogBuilder.create();
        dialog4.show();

        newitem_photo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                selectImageAction();
            }
        });

        newitem_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewItem temp = new NewItem();
                try{
                    if(!newitem_name.getText().toString().equals(pirmas) || !newitem_cost.getText().toString().equals(antras) || !newitem_count.getText().toString().equals(trecias)){

                        temp.setName(newitem_name.getText().toString());
                        temp.setCost(Double.parseDouble(newitem_cost.getText().toString())); //reikia gauti cost
                        temp.setCount(Integer.parseInt(newitem_count.getText().toString()));
                        if(imageToStore == null){
                            temp.setImage(vaizdas);
                        }
                        else{
                            temp.setImage(imageToStore);
                        }
                    }
                    dialog4.dismiss();
                } catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Fill all fields", Toast.LENGTH_LONG).show();
                }

                DataBase dataBaseHelper = new DataBase(ListOfItems.this);
                boolean success = dataBaseHelper.updateItem(temp, cartID, pirmas);
                if(success){
                    Toast.makeText(getApplicationContext(), "Item updated", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Failed to update", Toast.LENGTH_LONG).show();
                }
                ShowCartsOnListView(dataBaseHelper);
            }
        });

        newitem_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog4.dismiss();
                Toast.makeText(getApplicationContext(), "Canceled", Toast.LENGTH_LONG).show();
            }
        });
    }
}