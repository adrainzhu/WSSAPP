package com.example.dingtaihw.ui.gallery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dingtaihw.Model.LL.RequestParts;
import com.example.dingtaihw.Model.LL.SuggestParts;
import com.example.dingtaihw.R;

import java.util.List;

public class SuggestPartsAdapter extends ArrayAdapter<SuggestParts> {

    public SuggestPartsAdapter(@NonNull Context context, int resource, @NonNull List<SuggestParts> objects) {
        super(context, resource, objects);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

     SuggestParts suggestParts=getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.suggestparts,parent,false);
        TextView lhview=view.findViewById(R.id.s_lh);//料号
        TextView infoview=view.findViewById(R.id.info);//品名
        TextView cbview=view.findViewById(R.id.cb);//仓别
        TextView jwview=view.findViewById(R.id.jw);//架位
        TextView pnoview=view.findViewById(R.id.pc);//批次
        TextView numview=view.findViewById(R.id.requestnum);//实发数量
        lhview.setText(suggestParts.getLh());
        infoview.setText(suggestParts.getInfo());
        cbview.setText(suggestParts.getCb());
        jwview.setText(suggestParts.getLocation());
        pnoview.setText(suggestParts.getPno());
        numview.setText(suggestParts.getSuggestnum());
        return view;
    }


}
