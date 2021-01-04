package com.example.myshoppingapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import java.util.List;

public class ListAdapter extends ArrayAdapter<NewItem> {

    public ListAdapter(Context context, List<NewItem> objects){
        super(context, R.layout.listitemdesign, objects);
    }

    @Nullable
    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view = convertView;

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.adapter_cart_layout, null);
        }

        TextView name = (TextView) view.findViewById(R.id.title);

        NewItem item = getItem(position);

        name.setText(item.getName());

        return view;
    }

}
