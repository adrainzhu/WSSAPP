package com.example.dingtaihw.ui.home;

import android.app.ActivityManager;
import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.example.dingtaihw.DataBase.DB_EUtils;
import com.example.dingtaihw.MainActivity;

import com.example.dingtaihw.Model.Goods;
import com.example.dingtaihw.R;
import com.example.dingtaihw.databinding.FragmentHomeBinding;

import com.example.dingtaihw.ui.slideshow.LotAdapter;
import com.honeywell.aidc.AidcManager;

import com.honeywell.aidc.BarcodeFailureEvent;
import com.honeywell.aidc.BarcodeReadEvent;
import com.honeywell.aidc.BarcodeReader;
import com.honeywell.aidc.InvalidScannerNameException;

import com.honeywell.aidc.ScannerUnavailableException;

import com.honeywell.aidc.TriggerStateChangeEvent;
import com.honeywell.aidc.UnsupportedPropertyException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class HomeFragment extends Fragment implements BarcodeReader.BarcodeListener, BarcodeReader.TriggerListener {

    private FragmentHomeBinding binding;

    private AidcManager manager;
    private BarcodeReader barcodeReader;
    private View root;
    private ListView current_l;
    private ListView done_l;
    private List<Goods> goods;
    private List<Goods> d_goods;
    private GoodAdapter goodAdapter;;
    private GoodAdapter d_goodAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        initScan();
        if (root == null) {
            root = binding.getRoot();
        }
        ViewGroup parent = (ViewGroup) root.getParent();
        if (parent != null) {
            parent.removeView(root);
        }
        current_l=binding.currentlist;
        done_l=binding.goodslist;
        goods=new ArrayList<>();
        d_goods=new ArrayList<>();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initScan() {
        AidcManager.create((MainActivity) getActivity(), new AidcManager.CreatedCallback() {
            @Override
            public void onCreated(AidcManager aidcManager) {
                manager = aidcManager;
                try {
                    barcodeReader = manager.createBarcodeReader();
                    barcodeReader.setProperty(BarcodeReader.PROPERTY_CODE_128_ENABLED, true);
                    barcodeReader.setProperty(BarcodeReader.PROPERTY_EAN_13_ENABLED, true);
                    barcodeReader.setProperty(BarcodeReader.PROPERTY_TRIGGER_CONTROL_MODE, BarcodeReader.TRIGGER_CONTROL_MODE_AUTO_CONTROL);
                    barcodeReader.claim();
                } catch (InvalidScannerNameException e) {
                    Toast.makeText((MainActivity) getActivity(), "InvalidScannerNameException:" + e.getMessage(), Toast.LENGTH_LONG).show();
                } catch (UnsupportedPropertyException e) {
                    Toast.makeText((MainActivity) getActivity(), "UnsupportedPropertyException:" + e.getMessage(), Toast.LENGTH_LONG).show();
                } catch (ScannerUnavailableException e) {
                    Toast.makeText((MainActivity) getActivity(), "ScannerUnavailableException:" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                //barcodeReader.addBarcodeListener((MainActivity) getActivity());
                barcodeReader.addBarcodeListener(HomeFragment.this);
            }
        });


    }


    @Override
    public void onBarcodeEvent(BarcodeReadEvent barcodeReadEvent) {
        String raw = barcodeReadEvent.getBarcodeData();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String[] result = raw.split("\\+");
        Goods good = new Goods(result[0], "Fail", simpleDateFormat.format(new Date()), result[2], "pcs", " ", result[1]);
        //验证逻辑
        try {
            Runnable oracleTask = new Runnable() {
                @Override
                public void run() {
                   String r=DB_EUtils.checkgood(good.getSoNumber(),good.getLh(),good.getNumber());
                    if(r.equals("OK"))
                    {//存在逻辑
                        good.setStatus("入库成功");
                        goods.clear();
                        goods.add(good);
                        d_goods.add(good);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "入库成功", Toast.LENGTH_LONG).show();
                                goodAdapter=new GoodAdapter(getActivity(), R.layout.good_item, goods);
                                d_goodAdapter=new GoodAdapter(getActivity(),R.layout.good_item,d_goods);
                                current_l.setAdapter(goodAdapter);
                                done_l.setAdapter(d_goodAdapter);
                            }
                        });
                    }
                    else if (r.equals("Fail"))
                    {//不存在逻辑
                        goods.clear();
                        goods.add(good);
                        d_goods.add(good);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "信息不匹配", Toast.LENGTH_LONG).show();
                                goodAdapter=new GoodAdapter(getActivity(), R.layout.good_item, goods);
                                d_goodAdapter=new GoodAdapter(getActivity(),R.layout.good_item,d_goods);
                                current_l.setAdapter(goodAdapter);
                                done_l.setAdapter(d_goodAdapter);
                            }
                        });

                    }
                }
            };
            Thread thread=new Thread(oracleTask);
            thread.run();


        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @Override
    public void onFailureEvent(BarcodeFailureEvent barcodeFailureEvent) {

    }

    @Override
    public void onTriggerEvent(TriggerStateChangeEvent triggerStateChangeEvent) {

    }
}