package com.example.dingtaihw.ui.lotout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dingtaihw.Model.Out.Out_LotDetail;
import com.example.dingtaihw.R;

import java.util.List;

public class Out_LotDetailAdapter extends ArrayAdapter<Out_LotDetail> {
    List<Out_LotDetail> items;
    public Out_LotDetailAdapter(@NonNull Context context, int resource, @NonNull List<Out_LotDetail> objects) {
        super(context, resource, objects);
        items=objects;
    }
    public List<Out_LotDetail> getAllitems()
    {
       return  items;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.out_lotdetail, parent, false);
        Out_LotDetail item = getItem(position);
        TextView materialview = view.findViewById(R.id.material);
        TextView lotidview = view.findViewById(R.id.Lotid);
        TextView lotqtyview = view.findViewById(R.id.Lotqty);
        TextView prdview = view.findViewById(R.id.Productid);
        materialview.setText(item.getMaterialNo());
        lotidview.setText(item.getLotid());
        lotqtyview.setText(item.getLotnum());
        prdview.setText(item.getProductid());
        return view;
    }
}
