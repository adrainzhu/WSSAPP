package com.example.dingtaihw.ui.lotout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dingtaihw.Model.Out.Out_Wuliu;
import com.example.dingtaihw.R;

import java.util.List;

public class Out_wlAdapter extends ArrayAdapter<Out_Wuliu> {
    public Out_wlAdapter(@NonNull Context context, int resource, @NonNull List<Out_Wuliu> objects) {
        super(context, resource, objects);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.wloutlot, parent, false);
        Out_Wuliu item=getItem(position);
        TextView wlbhview=view.findViewById(R.id.wlbh);
        wlbhview.setText(item.getWuliuno());
        TextView sqrview=view.findViewById(R.id.sqr);
        sqrview.setText(item.getSqr());
        TextView lcbhview=view.findViewById(R.id.lcbh);
        lcbhview.setText(item.getLcbh());
        return view;

}
}
