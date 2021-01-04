package com.example.myshoppingapplication;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import java.util.List;



public class ItemListAdapter extends ArrayAdapter<NewItem> {
    private Context context;

    public ItemListAdapter(Context context, List<NewItem> objects) {
        super(context, R.layout.adapter_item_layout, objects);
        this.context = context;
    }

    @Nullable
    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view = convertView;

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.adapter_item_layout, null);
        }


        TextView name = (TextView) view.findViewById(R.id.pirmasItemView1);
        TextView price = (TextView) view.findViewById(R.id.antrasItemView1);
        TextView amount = (TextView) view.findViewById(R.id.treciasItemView1);
        ImageView image = (ImageView) view.findViewById(R.id.imageVaizdas);

        //Tuomet cia reikia is duomenu bazes dabar issitraukti ar jis yra bool true ar bool yra false ir pagal tai nustatyti jam spalva


        NewItem item = getItem(position);
        int state = item.getState();

        if (state == 1){ //if true state
            view.setBackgroundColor(Color.parseColor("#778899"));
        }
        else{ //if false state
            view.setBackgroundColor(Color.parseColor("#e8eefe"));
        }


        assert item != null;
        name.setText(item.getName());
        price.setText(String.valueOf(item.getCost()));
        amount.setText(String.valueOf(item.getCount()));

        image.setImageBitmap((item.getImage())); //jia jis ta foto issisaugos atvaizdavimui

        return view;
    }
}