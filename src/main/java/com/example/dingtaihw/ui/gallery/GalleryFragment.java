package com.example.dingtaihw.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.dingtaihw.DataBase.DB_EUtils;
import com.example.dingtaihw.MainActivity;
import com.example.dingtaihw.Model.ll_item;
import com.example.dingtaihw.R;
import com.example.dingtaihw.databinding.FragmentGalleryBinding;
import com.example.dingtaihw.ui.LL_Detail;
import com.honeywell.aidc.AidcManager;
import com.honeywell.aidc.BarcodeReader;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;


public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    private AidcManager manager;
    private BarcodeReader barcodeReader;
    private ListView listView;
    private List<ll_item> items = new ArrayList<>();
    private View root;
    private boolean isdone = false;
    private LL_Adapter ll_adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        listView = binding.lld;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle1 = getActivity().getIntent().getExtras();
                Bundle bundle = new Bundle();
                bundle.putString("lldh", items.get(i).getSerialno());
                bundle.putString("sqr", items.get(i).getUser());
                bundle.putString("type", items.get(i).getType());
                bundle.putString("requestid",items.get(i).getRequestid());
                bundle.putString("pushuser",bundle1.getString("userid"));
                bundle.putString("pushid", bundle1.getString("pushid"));
                Intent intent = new Intent(getActivity(),LL_Detail.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        isdone = false;
        items.clear();
        try {
            Runnable oracleTask = new Runnable() {
                @Override
                public void run() {
                    items = DB_EUtils.getllitem();
                    System.out.println("items:"+items.size());
                }
            };
            Thread thread = new Thread(oracleTask);
            thread.start();
            try{
            thread.join();
                ll_adapter = new LL_Adapter(getActivity(), R.layout.ll_item, items);
                listView.setAdapter(ll_adapter);}
            catch (InterruptedException e)
            {
                System.out.println(e.getMessage());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
//        while (true) {
//            if (isdone) {
//                ll_adapter = new LL_Adapter(getActivity(), R.layout.ll_item, items);
//                listView.setAdapter(ll_adapter);
//                break;
//            }
//        }
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onStart() {

        super.onStart();
    }
}