package com.example.dingtaihw.ui.slideshow;

import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.dingtaihw.DataBase.DB_EUtils;
import com.example.dingtaihw.DataBase.DB_RUTils;
import com.example.dingtaihw.MainActivity;
import com.example.dingtaihw.Model.LotItem;
import com.example.dingtaihw.R;
import com.example.dingtaihw.databinding.FragmentSlideshowBinding;
import com.example.dingtaihw.ui.gallery.LL_Adapter;
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

public class SlideshowFragment extends Fragment implements BarcodeReader.BarcodeListener, BarcodeReader.TriggerListener {

    private FragmentSlideshowBinding binding;
    private AidcManager manager;
    private BarcodeReader barcodeReader;
    ListView donelot;
    TextView lotid;
    TextView lotstatus;
    TextView lotdate;
    LotAdapter lotAdapter;
    List<LotItem> lots;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        donelot = binding.mylots;
        lotid = binding.lot;
        lotstatus = binding.lotstatus;
        lotdate = binding.lotsdate;
        lots = new ArrayList<>();
        lotAdapter = new LotAdapter(getActivity(), R.layout.lotitem, lots);
        donelot.setAdapter(lotAdapter);
        initScan();
        return root;
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
                    System.out.println(e.getMessage());
                    Toast.makeText((MainActivity) getActivity(), "InvalidScannerNameException:" + e.getMessage(), Toast.LENGTH_LONG).show();
                } catch (UnsupportedPropertyException e) {
                    System.out.println(e.getMessage());
                    Toast.makeText((MainActivity) getActivity(), "UnsupportedPropertyException:" + e.getMessage(), Toast.LENGTH_LONG).show();
                } catch (ScannerUnavailableException e) {
                    System.out.println(e.getMessage());
                    Toast.makeText((MainActivity) getActivity(), "ScannerUnavailableException:" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                //barcodeReader.addBarcodeListener((MainActivity) getActivity());
                barcodeReader.addBarcodeListener(SlideshowFragment.this);
            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onBarcodeEvent(BarcodeReadEvent barcodeReadEvent) {
        //成品相关逻辑
        String raw = barcodeReadEvent.getBarcodeData();
        System.out.println(raw);
        String flag_str = raw.substring(0, 2);
        String lot_str = raw.substring(2, raw.length());
        if (!flag_str.equals("1T")) {
            System.out.println("1111");
getActivity().runOnUiThread(new Runnable() {
    @Override
    public void run() {
        Toast.makeText(getActivity(), "非法条码，请检查条码！", Toast.LENGTH_LONG).show();
    }
});
        } else {
            try {
                Runnable oracleTask = new Runnable() {
                    @Override
                    public void run() {
                        int r = DB_RUTils.checklot(lot_str);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        System.out.println("r=" + r);
                        if (r == 1) {

                         getActivity().runOnUiThread(new Runnable() {
                             @Override
                             public void run() {
                                 lotid.setText(lot_str);
                                 lotstatus.setText("Fail");
                                 lotdate.setText(sdf.format(new Date()));
                                 lots.add(new LotItem(lot_str, "Fail", sdf.format(new Date())));
                                 Toast.makeText(getActivity(), "入库失败,成品已入库!请检查！", Toast.LENGTH_LONG).show();
                                 lotAdapter = new LotAdapter(getActivity(), R.layout.lotitem, lots);
                                 donelot.setAdapter(lotAdapter);
                             }
                         });
                        } else if (r == 0) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    lotid.setText(lot_str);
                                    lotstatus.setText("SUCCESS");
                                    lotdate.setText(sdf.format(new Date()));
                                    lots.add(new LotItem(lot_str, "SUCCESS", sdf.format(new Date())));
                                    Toast.makeText(getActivity(), "入库成功！", Toast.LENGTH_LONG).show();
                                    lotAdapter = new LotAdapter(getActivity(), R.layout.lotitem, lots);
                                    donelot.setAdapter(lotAdapter);
                                }
                            });

                            lots.add(new LotItem("123", "SUCCESS", "456"));
                            System.out.println("breakpoint2");
                        } else if (r == -1) {

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    lotid.setText(lot_str);
                                    lotstatus.setText("Fail");
                                    lotdate.setText(sdf.format(new Date()));
                                    Toast.makeText(getActivity(), "入库失败,成品不存在!请检查！", Toast.LENGTH_LONG).show();
                                    lots.add(new LotItem(lot_str, "Fail", sdf.format(new Date())));
                                    lotAdapter = new LotAdapter(getActivity(), R.layout.lotitem, lots);
                                    donelot.setAdapter(lotAdapter);
                                }
                            });
                        }
                    }
                };
                Thread thread = new Thread(oracleTask);
                thread.start();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }


    }

    @Override
    public void onFailureEvent(BarcodeFailureEvent barcodeFailureEvent) {

    }

    @Override
    public void onTriggerEvent(TriggerStateChangeEvent triggerStateChangeEvent) {

    }
}