package com.example.dingtaihw.ui.location;

import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.dingtaihw.DataBase.DB_EUtils;
import com.example.dingtaihw.MainActivity;
import com.example.dingtaihw.databinding.FragmentLocationBinding;
import com.honeywell.aidc.AidcManager;
import com.honeywell.aidc.BarcodeFailureEvent;
import com.honeywell.aidc.BarcodeReadEvent;
import com.honeywell.aidc.BarcodeReader;
import com.honeywell.aidc.InvalidScannerNameException;
import com.honeywell.aidc.ScannerUnavailableException;
import com.honeywell.aidc.UnsupportedPropertyException;

/**
 * 架位管理Fragment
 * 扫码将Lot上架至对应架位
 */
public class LocationFragment extends Fragment implements BarcodeReader.BarcodeListener {
    private AidcManager manager;
    private BarcodeReader barcodeReader;
    private FragmentLocationBinding binding;
    Button submit;
    String result;
    EditText lotText;
    EditText locText;

    private View root;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        initScan();
        binding = FragmentLocationBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        submit = binding.sjbtn;
        lotText = binding.lotid;
        locText = binding.jwid;
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //上架逻辑
                String lotid = String.valueOf(lotText.getText());
                String locid = String.valueOf(locText.getText());

                try {
                    Runnable oracleTask = new Runnable() {
                        @Override
                        public void run() {
                            result = DB_EUtils.submitlocation(lotid, locid);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    };
                    Thread thread = new Thread(oracleTask);
                    thread.start();
                } catch (Exception e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
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
                    Toast.makeText((MainActivity) getActivity(), "InvalidScannerNameException:" + e.getMessage(), Toast.LENGTH_LONG).show();
                } catch (UnsupportedPropertyException e) {
                    Toast.makeText((MainActivity) getActivity(), "UnsupportedPropertyException:" + e.getMessage(), Toast.LENGTH_LONG).show();
                } catch (ScannerUnavailableException e) {
                    Toast.makeText((MainActivity) getActivity(), "ScannerUnavailableException:" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                //barcodeReader.addBarcodeListener((MainActivity) getActivity());
                barcodeReader.addBarcodeListener(LocationFragment.this);
            }
        });


    }

    @Override
    public void onBarcodeEvent(BarcodeReadEvent barcodeReadEvent) {
        try {
            String raw = barcodeReadEvent.getBarcodeData();
            String flag_str = raw.substring(0, 2);
            if (flag_str.equals("1T")) {//扫描条码为Lotid
                String lot_str = raw.substring(2, raw.length());
                lotText.setText(lot_str);
            } else {//扫描条码为架位id
                locText.setText(raw);
            }
            System.out.println("上架扫描内容" + raw);
        } catch (Exception e) {
            System.out.println("异常" + e.toString());
        }
    }

    @Override
    public void onFailureEvent(BarcodeFailureEvent barcodeFailureEvent) {
        System.out.println(barcodeFailureEvent.toString());
    }
}