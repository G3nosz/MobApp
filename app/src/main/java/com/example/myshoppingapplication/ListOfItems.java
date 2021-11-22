package com.example.myshoppingapplication;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
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
    private EditText newitemName, newitemCost, newitemCount;
    private Button newitemSave, newitemCancel, newitemPhoto, takeImage, uploadImage;
    private Button deleteItem, editItem, _cancel;
    private ImageView newitemView;
    private ProgressBar bar1;
    private int progesBarProc = 0;

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
                    if(element.getState() == 1){
                        true_count += 1;
                    }
                    all_items_count += 1;
                }
                if(all_items_count == 0){
                    progesBarProc = 0;
                }
                else {
                    progesBarProc = true_count * 100 / all_items_count;
                }

                bar1.setProgress(progesBarProc);
                int cartState = dataBaseHelper.getCartState(cartID);

                if(progesBarProc == 100){
                    if(cartState == 0){
                        int test = dataBaseHelper.changeCartState(1, cartID);
                        int test2 = dataBaseHelper.updateProfile(cartID);
                    }
                }
                else if(progesBarProc < 100){
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
        boolean success = dataBaseHelper.changeItemState(item, idCart);
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
            progesBarProc = 0;
        }
        else {
            progesBarProc = true_count * 100 / all_items_count;
        }

        int cartState = dataBaseHelper.getCartState(cartID);

        if(progesBarProc == 100){
            if(cartState == 0){
                int test = dataBaseHelper.changeCartState(1, cartID);
                int test2 = dataBaseHelper.updateProfile(cartID);
            }
        }
        else if(progesBarProc < 100){
            if(cartState == 1){
                int test = dataBaseHelper.changeCartState(0, cartID);
            }
        }

        bar1.setProgress(progesBarProc);
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

        deleteItem = (Button) popUpView.findViewById(R.id.deleteItem);
        editItem = (Button) popUpView.findViewById(R.id.editItem);
        deleteItem.setBackgroundColor(getResources().getColor(R.color.Buttonred));

        final NewItem value1 = value;
        editItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editItem(value1);
                ShowCartsOnListView(dataBaseHelper);
                optionspu.dismiss();
            }
        });
        deleteItem.setOnClickListener(new View.OnClickListener() {
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

        deleteItem = (Button) popUpView.findViewById(R.id.deleteYes);
        _cancel = (Button) popUpView.findViewById(R.id.deleteNo);

        dialogBuilder.setView(popUpView);
        dialog2 = dialogBuilder.create();
        dialog2.show();

        final NewItem value1 = value;
        deleteItem.setOnClickListener(new View.OnClickListener() {
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

        takeImage = (Button) popUpView.findViewById(R.id.takePicture);
        uploadImage = (Button) popUpView.findViewById(R.id.uploadImage);


        dialogBuilder.setView(popUpView);
        dialog9 = dialogBuilder.create();
        dialog9.show();

        uploadImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent objectIntent = new Intent();
                objectIntent.setType("image/*");
                objectIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(objectIntent, PICK_IMAGE_REQUEST);

                dialog9.dismiss();
            }
        });
        takeImage.setOnClickListener(new View.OnClickListener(){
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

        newitemName = (EditText) popUpView.findViewById(R.id.newItemName);
        newitemCount = (EditText) popUpView.findViewById(R.id.newItemCount);
        newitemCost = (EditText) popUpView.findViewById(R.id.newItemCost);
        newitemView = (ImageView) popUpView.findViewById(R.id.newicon);

        newitemPhoto = (Button) popUpView.findViewById(R.id.uploadImage);

        newitemSave = (Button) popUpView.findViewById(R.id.saveButton);
        newitemCancel = (Button) popUpView.findViewById(R.id.cancelButton);

        dialogBuilder.setView(popUpView);
        dialog3 = dialogBuilder.create();
        dialog3.show();

        newitemPhoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                selectImageAction();
            }
        });

        newitemSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewItem temp = new NewItem();
                try{
                    temp.setName(newitemName.getText().toString());
                    temp.setCost(Double.parseDouble(newitemCost.getText().toString())); //reikia gauti cost
                    temp.setCount(Integer.parseInt(newitemCount.getText().toString()));
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

        newitemCancel.setOnClickListener(new View.OnClickListener() {
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
        newitemName = (EditText) popUpView.findViewById(R.id.newItemName);
        newitemCount = (EditText) popUpView.findViewById(R.id.newItemCount);
        newitemCost = (EditText) popUpView.findViewById(R.id.newItemCost);
        final String pirmas = String.valueOf(value.getName());
        final String antras = String.valueOf(value.getCount());
        final String trecias = String.valueOf(value.getCost());
        final Bitmap vaizdas = value.getImage();

        newitemView = (ImageView) popUpView.findViewById(R.id.newicon);

        newitemPhoto = (Button) popUpView.findViewById(R.id.uploadImage);

        newitemName.setText(String.valueOf(value.getName()));
        newitemCount.setText(String.valueOf(value.getCount()));
        newitemCost.setText(String.valueOf(value.getCost()));
        newitemView.setImageBitmap(vaizdas);

        newitemSave = (Button) popUpView.findViewById(R.id.saveButton);
        newitemCancel = (Button) popUpView.findViewById(R.id.cancelButton);

        dialogBuilder.setView(popUpView);
        dialog4 = dialogBuilder.create();
        dialog4.show();

        newitemPhoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                selectImageAction();
            }
        });

        newitemSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewItem temp = new NewItem();
                try{
                    if(!newitemName.getText().toString().equals(pirmas) || !newitemCost.getText().toString().equals(antras) || !newitemCount.getText().toString().equals(trecias)){

                        temp.setName(newitemName.getText().toString());
                        temp.setCost(Double.parseDouble(newitemCost.getText().toString()));
                        temp.setCount(Integer.parseInt(newitemCount.getText().toString()));
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

        newitemCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog4.dismiss();
                Toast.makeText(getApplicationContext(), "Canceled", Toast.LENGTH_LONG).show();
            }
        });
    }
}