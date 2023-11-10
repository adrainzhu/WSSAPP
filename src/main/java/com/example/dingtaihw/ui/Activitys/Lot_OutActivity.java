package com.example.dingtaihw.ui.Activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dingtaihw.DataBase.DB_EUtils;
import com.example.dingtaihw.Model.Out.Out_LotDetail;
import com.example.dingtaihw.Model.Out.Out_Scan;
import com.example.dingtaihw.R;
import com.example.dingtaihw.Tools.util.RequestHandler;
import com.example.dingtaihw.ui.lotout.Out_LotDetailAdapter;
import com.example.dingtaihw.ui.lotout.Out_ScanAdapter;
import com.honeywell.aidc.AidcManager;
import com.honeywell.aidc.BarcodeFailureEvent;
import com.honeywell.aidc.BarcodeReadEvent;
import com.honeywell.aidc.BarcodeReader;
import com.honeywell.aidc.InvalidScannerNameException;
import com.honeywell.aidc.ScannerUnavailableException;
import com.honeywell.aidc.UnsupportedPropertyException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Lot_OutActivity extends AppCompatActivity implements BarcodeReader.BarcodeListener {
    ListView out_detail;
    Out_LotDetailAdapter detailAdapter;
    List<Out_LotDetail> items;
    String requestid;
    String pushid;
    List<Out_Scan> scanitems;
    private AidcManager manager;
    private BarcodeReader barcodeReader;
    Out_ScanAdapter out_scanAdapter;
    ListView scanlist;
    Button subbtn;
    String errorinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lot_out);
        out_detail = findViewById(R.id.outlotdetaillist);
        scanlist = findViewById(R.id.scanlotlist);
        scanitems = new ArrayList<>();
        subbtn = findViewById(R.id.submit);
        Bundle bundle = getIntent().getExtras();
        requestid = bundle.getString("requestid");
        pushid = bundle.getString("pushid");
        initScan();
        try {
            Runnable oralcethread = new Runnable() {
                @Override
                public void run() {
                    items = DB_EUtils.getOutDetailList(requestid);
                }
            };
            Thread thread = new Thread(oralcethread);
            thread.start();
            try {
                thread.join();
                detailAdapter = new Out_LotDetailAdapter(this, R.layout.out_lotdetail, items);
                out_detail.setAdapter(detailAdapter);
            } catch (Exception e) {
                System.out.println(e);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        subbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> checkitems = new ArrayList<>();
                for (int i = 0; i < items.size(); i++) {
                    checkitems.add(items.get(i).getLotid());
                }
                System.out.println("扫描List内容:");
                for (int i = 0; i < checkitems.size(); i++) {
                    System.out.println(checkitems.get(i));
                }
                errorinfo = "Lotid:";
                if (checkitems.size() != scanitems.size()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Lot_OutActivity.this, "Lot数量不一致，请检查！", Toast.LENGTH_LONG).show();
                            return;
                        }
                    });
                    return;
                }
                Iterator<String> lotIter = checkitems.iterator();
                while (lotIter.hasNext()) {
                    boolean isscan = false;
                    String c_lot = lotIter.next();
                    for (int i1 = 0; i1 < scanitems.size(); i1++) {
                        if (c_lot.equals(scanitems.get(i1).getLotid()) && scanitems.get(i1).getInnerlabel().equals("√") && scanitems.get(i1).getOuterlable().equals("√")) {
                            isscan = true;
                        }
                    }
                    if (isscan) {
                        lotIter.remove();
                    }
                }
                if (checkitems.size() == 0) {
                    try {
                        Toast.makeText(Lot_OutActivity.this, "检查成功，已提交", Toast.LENGTH_LONG).show();
                        Runnable httptask = new Runnable() {
                            @Override
                            public void run() {
                                RequestHandler handler = new RequestHandler();
                                handler.outsubmitRequest(requestid, pushid);
                            }
                        };
                        Thread thread = new Thread(httptask);
                        thread.start();
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    Lot_OutActivity.this.finish();
                } else {
                    for (int i = 0; i < checkitems.size(); i++) {
                        errorinfo += checkitems.get(i) + ",";
                    }
                    errorinfo = errorinfo.substring(0, errorinfo.length() - 1);
                    errorinfo += "未Check,提交失败!";
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Lot_OutActivity.this, errorinfo, Toast.LENGTH_LONG).show();
                            return;
                        }
                    });
                }
            }
        });

    }
    private void initScan() {
        AidcManager.create(this, new AidcManager.CreatedCallback() {
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
                    Toast.makeText(Lot_OutActivity.this, "InvalidScannerNameException:" + e.getMessage(), Toast.LENGTH_LONG).show();
                } catch (UnsupportedPropertyException e) {
                    Toast.makeText(Lot_OutActivity.this, "UnsupportedPropertyException:" + e.getMessage(), Toast.LENGTH_LONG).show();
                } catch (ScannerUnavailableException e) {
                    Toast.makeText(Lot_OutActivity.this, "ScannerUnavailableException:" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                //barcodeReader.addBarcodeListener((MainActivity) getActivity());
                barcodeReader.addBarcodeListener(Lot_OutActivity.this);
            }
        });

    }
    @Override
    public void onBarcodeEvent(BarcodeReadEvent barcodeReadEvent) {
        boolean iscontain = false;
        System.out.println(barcodeReadEvent.getBarcodeData());
        try {
            String raw = barcodeReadEvent.getBarcodeData();
            String flag_str = raw.substring(0, 2);
            String lot_str = raw.substring(2, raw.length());
            if (!flag_str.equals("1T")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Lot_OutActivity.this, "非法条码，请检查！", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                for (int i = 0; i < scanitems.size(); i++) {
                    if (scanitems.get(i).getLotid().equals(lot_str) && scanitems.get(i).getOuterlable().equals("×")) {
                        scanitems.get(i).setOuterlable("√");
                        iscontain = true;
                        break;
                    }
                    if (scanitems.get(i).getLotid().equals(lot_str) && scanitems.get(i).getOuterlable().equals("√")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Lot_OutActivity.this, "该Lot已检查完成，请勿重复扫描！", Toast.LENGTH_LONG).show();
                            }
                        });
                        iscontain = true;
                        break;
                    }
                    iscontain = false;
                }
                if (!iscontain) {
                    Out_Scan out_scan = new Out_Scan(lot_str, "√", "×");
                    scanitems.add(out_scan);
                }
            }
        } catch (Exception e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(Lot_OutActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                }
            });
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                out_scanAdapter = new Out_ScanAdapter(Lot_OutActivity.this, R.layout.outscan, scanitems);
                scanlist.setAdapter(out_scanAdapter);
            }
        });
    }

    @Override
    public void onFailureEvent(BarcodeFailureEvent barcodeFailureEvent) {

    }
}