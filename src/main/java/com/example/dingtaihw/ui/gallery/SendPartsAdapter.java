package com.example.dingtaihw.ui.gallery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dingtaihw.Model.LL.SendParts;
import com.example.dingtaihw.R;

import java.util.List;

public class SendPartsAdapter extends ArrayAdapter<SendParts> {
    private List<SendParts> myparts = null;
    private Context mycontext = null;

    public SendPartsAdapter(@NonNull Context context, int resource, @NonNull List<SendParts> objects) {
        super(context, resource, objects);
        myparts = objects;
        mycontext = context;
    }

    @Override
    public int getCount() {
        return myparts.size();
    }

    @Nullable
    @Override
    public SendParts getItem(int position) {
        return myparts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        SendParts sendParts = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.sendparts, parent, false);
        EditText lhview = view.findViewById(R.id.s_lh);
        lhview.addTextChangedListener(new CustomTextWatcher(lhview,sendParts));
        TextView infoview = view.findViewById(R.id.info);
        EditText pcview = view.findViewById(R.id.pc);
        pcview.addTextChangedListener(new CustomTextWatcher(pcview,sendParts));
        TextView plantview = view.findViewById(R.id.plant);
        EditText sendnumview = view.findViewById(R.id.sendnum);
        sendnumview.addTextChangedListener(new CustomTextWatcher(sendnumview,sendParts));
        Button deletebtn = view.findViewById(R.id.deletebtn);
        lhview.setText(sendParts.getLh());
        infoview.setText(sendParts.getInfo());
        pcview.setText(sendParts.getPno());
        plantview.setText(sendParts.getLocation());
        sendnumview.setText(sendParts.getSendnum());
        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myparts.remove(position);
                notifyDataSetChanged();
            }
        });

        return view;
    }
}
