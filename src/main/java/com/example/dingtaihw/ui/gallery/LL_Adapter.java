package com.example.dingtaihw.ui.gallery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dingtaihw.Model.Goods;
import com.example.dingtaihw.Model.ll_item;
import com.example.dingtaihw.R;

import java.util.List;

public class LL_Adapter extends ArrayAdapter<ll_item> {
    public  LL_Adapter(@NonNull Context context, int resource, @NonNull List<ll_item> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.ll_item, parent, false);
        ll_item item = getItem(position);
        TextView serialview = view.findViewById(R.id.serialno);
        TextView userview = view.findViewById(R.id.user);
        TextView typeview = view.findViewById(R.id.type);
        serialview.setText(item.getSerialno());
        userview.setText(item.getUser());
        typeview.setText(item.getType());
        return view;
    }
}
