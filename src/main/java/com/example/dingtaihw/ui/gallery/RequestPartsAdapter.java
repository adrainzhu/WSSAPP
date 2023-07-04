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
import com.example.dingtaihw.Model.LL.RequestParts;
import com.example.dingtaihw.R;

import java.util.List;

public class RequestPartsAdapter extends ArrayAdapter<RequestParts> {

    public RequestPartsAdapter(@NonNull Context context, int resource, @NonNull List<RequestParts> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        RequestParts requestParts=getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.requestparts,parent,false);
        TextView lhview=view.findViewById(R.id.lh);
        TextView infoview=view.findViewById(R.id.info);
        TextView typeview=view.findViewById(R.id.type);
        TextView numview=view.findViewById(R.id.requestnum);
        TextView whview=view.findViewById(R.id.orderwh);
        TextView pnoview=view.findViewById(R.id.pno);
        TextView bzview=view.findViewById(R.id.bz);
        lhview.setText(requestParts.getLh());
        infoview.setText(requestParts.getInfo());
        numview.setText(requestParts.getRequestnum());
        whview.setText(requestParts.getOrderwh());
        pnoview.setText(requestParts.getPno());
        bzview.setText(requestParts.getBz());
        typeview.setText(requestParts.getRequesttype());

        return view;
    }


}
