package com.example.dingtaihw.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dingtaihw.Model.Goods;
import com.example.dingtaihw.R;

import java.text.SimpleDateFormat;
import java.util.List;

public class GoodAdapter  extends ArrayAdapter<Goods> {

    public GoodAdapter(@NonNull Context context, int resource, @NonNull List<Goods> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Goods goods=getItem(position);
        View view =LayoutInflater.from(getContext()).inflate(R.layout.good_item,parent,false);
        TextView numberview=view.findViewById(R.id.number);
        TextView sobumberview=view.findViewById(R.id.sonumber);
        TextView lhview=view.findViewById(R.id.lh);
        TextView unitview=view.findViewById(R.id.unit);
        TextView statusview=view.findViewById(R.id.status);
        TextView dateview=view.findViewById(R.id.scandate);
        numberview.setText(goods.getNumber());
        statusview.setText(goods.getStatus());
        dateview.setText(goods.getScandate());
        sobumberview.setText(goods.getSoNumber());
        lhview.setText(goods.getLh());
        unitview.setText(goods.getUnit());

        return view;
    }
}
