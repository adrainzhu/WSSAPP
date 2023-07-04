//package com.example.dingtaihw.ui.gallery;
//
//import android.app.Activity;
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.dingtaihw.Model.LL.SendParts;
//import com.example.dingtaihw.R;
//
//import java.util.List;
//
//public class SendPartsEditAdapter extends ArrayAdapter<SendParts> {
//private List<SendParts> items;
//private Activity context;
//    public SendPartsEditAdapter(@NonNull Activity context, int resource, @NonNull List<SendParts> objects) {
//        super(context, resource, objects);
//    this.context=context;
//    this.items=objects;
//    }
//    static class ViewHolder{
//        protected EditText pno;
//        protected EditText sendnum;
//        protected TextView lhview;
//        protected TextView plantview;
//        protected TextView infoview;
//        protected Button deletebtn;
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//       View v=convertView;
//       SendParts s=items.get(position);
//       if(v==null)
//       {LayoutInflater inflater=context.getLayoutInflater();
//           v=inflater.inflate(R.layout.sendparts,null);
//           ViewHolder viewHolder=new ViewHolder();
//           viewHolder.pno=v.findViewById(R.id.pc);
//           viewHolder.sendnum=v.findViewById(R.id.sendnum);
//           viewHolder.pno.addTextChangedListener(new CustomTextWatcher(viewHolder,viewHolder.pno));
//
//       }
//
//    }
//
//    public SendPartsEditAdapter(Activity context)
//
//}
