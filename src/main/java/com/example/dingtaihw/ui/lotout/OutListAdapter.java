package com.example.dingtaihw.ui.lotout;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dingtaihw.Model.Out.Lot_Out;
import com.example.dingtaihw.R;


import java.util.List;

public class OutListAdapter extends ArrayAdapter<Lot_Out> {
    public OutListAdapter(@NonNull Context context, int resource, @NonNull List<Lot_Out> objects) {
        super(context, resource, objects);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.outlot, parent, false);
         Lot_Out item=getItem(position);
        TextView lcbhview = view.findViewById(R.id.lcbh);
        TextView sqrview = view.findViewById(R.id.sqr);
        TextView dnview = view.findViewById(R.id.dn);
        lcbhview.setText(item.getLcbh());
        sqrview.setText(item.getSqr());
        dnview.setText(item.getDn());
        return view;
    }
}
