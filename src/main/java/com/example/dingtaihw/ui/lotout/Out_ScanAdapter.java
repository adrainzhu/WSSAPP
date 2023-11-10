package com.example.dingtaihw.ui.lotout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dingtaihw.Model.LL.SendParts;
import com.example.dingtaihw.Model.Out.Out_LotDetail;
import com.example.dingtaihw.Model.Out.Out_Scan;
import com.example.dingtaihw.R;

import java.util.List;

public class Out_ScanAdapter extends ArrayAdapter<Out_Scan> {
private List<Out_Scan> myitems=null;
    private Context mycontext = null;
    public Out_ScanAdapter(@NonNull Context context, int resource ,@NonNull List<Out_Scan> objects) {
        super(context, resource, objects);
        myitems=objects;
        mycontext=context;
    }

    @Override
    public int getCount() {
        return myitems.size();
    }

    @Nullable
    @Override
    public Out_Scan getItem(int position) {
        return myitems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.outscan, parent, false);
       Out_Scan item = getItem(position);
        TextView Lotid=view.findViewById(R.id.Lotid);
        Lotid.setText(item.getLotid());
       TextView innerlabel=view.findViewById(R.id.innerlabel);
       innerlabel.setText(item.getInnerlabel());
       TextView outlabel=view.findViewById(R.id.outerlabel);
       outlabel.setText(item.getOuterlable());
        Button btn=view.findViewById(R.id.deletebtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myitems.remove(position);
                notifyDataSetChanged();
            }
        });
       return view;

    }
}
