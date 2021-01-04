package com.example.myshoppingapplication;
import android.graphics.Color;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.widget.Filterable;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CartListAdapter extends ArrayAdapter<NewCart> implements  Filterable {
    private List<NewCart> exampleList;
    public GestureDetector gestureDetector;
    private List<NewCart> allCarts;
    private int updateProfile;
    DataBase dataBaseHelper;

    public CartListAdapter(Context context, List<NewCart> objects){
        super(context, R.layout.adapter_cart_layout, objects);
        dataBaseHelper = new DataBase(context);
    }

    @Nullable
    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view = convertView;

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.adapter_cart_layout, null);
        }

        TextView name = (TextView) view.findViewById(R.id.pirmasView1);
        TextView price = (TextView) view.findViewById(R.id.antrasView1);
        TextView amount = (TextView) view.findViewById(R.id.treciasView1);

        //zemiau tiesiog papildomos funkcijos skaiciavimai
        NewCart item = getItem(position);

        int success = dataBaseHelper.loadCartData(item);

        double profit = 0;
        assert item != null;
        name.setText(item.getName());
        profit = item.getLimit() - item.getCost();
        price.setText(String.valueOf(item.getCost())); //profit
        amount.setText(String.valueOf(item.getCount()));


        if(item.getLimit() >= item.getCost()){
            price.setTextColor(Color.parseColor("#228B22"));
        }
        else
        {
            price.setTextColor(Color.parseColor("#B43757"));
        }

        NewCart cart = getItem(position);
        int state = item.getState(); //cia state priklauso jei visi jos item state yra true, tuomet cart vadinas yra ivygdytas

        if (state == 1){ //if true state
            view.setBackgroundColor(Color.parseColor("#778899"));
            if(item.getLimit() >= item.getCost()){
                price.setTextColor(Color.parseColor("#FFFFF0"));
            }
            else
            {
                price.setTextColor(Color.parseColor("#FF7F00"));
            }
        }
        else{ //if false state
            view.setBackgroundColor(Color.parseColor("#e8eefe"));
        }

        return view;
    }

}

