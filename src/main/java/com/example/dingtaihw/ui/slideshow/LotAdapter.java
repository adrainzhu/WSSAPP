package com.example.dingtaihw.ui.slideshow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dingtaihw.Model.LotItem;
import com.example.dingtaihw.R;

import java.util.List;

public class LotAdapter extends ArrayAdapter<LotItem> {
    public LotAdapter(@NonNull Context context, int resource, @NonNull List<LotItem> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       LotItem item=getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.lotitem,parent,false);
        TextView lotid=view.findViewById(R.id.h_lot);
        TextView lotstatus=view.findViewById(R.id.h_lotstatus);
        TextView lotdate=view.findViewById(R.id.h_lotsdate);
        lotid.setText(item.getLotid());
        lotstatus.setText(item.getLotstatus());
        lotdate.setText(item.getLotsdate());
        return view;
    }
}
