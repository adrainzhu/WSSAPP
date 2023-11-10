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
import com.example.dingtaihw.ui.lotout.Out_LotWLDetailAdapter;
import com.example.dingtaihw.ui.lotout.Out_WLScanAdapter;
import com.honeywell.aidc.AidcManager;
import com.honeywell.aidc.BarcodeFailureEvent;
import com.honeywell.aidc.BarcodeReadEvent;
import com.honeywell.aidc.BarcodeReader;
import com.honeywell.aidc.InvalidScannerNameException;
import com.honeywell.aidc.ScannerUnavailableException;
import com.honeywell.aidc.UnsupportedPropertyException;

import java.util.ArrayList;
import java.util.List;

public class Wuliu_OutActivity extends AppCompatActivity implements BarcodeReader.BarcodeListener {
    ListView wldetail;
    List<Out_LotDetail> items;
    ListView scanlist;
    List<Out_Scan> scanitems;
    Out_WLScanAdapter scanAdapter;
    Out_LotWLDetailAdapter detailAdapter;
    private AidcManager manager;
    private BarcodeReader barcodeReader;
    String requestid;
    String pushid;
    Button submit;
    String errorinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wuliu_out);
        wldetail = findViewById(R.id.outwldetaillist);
        Bundle bundle = getIntent().getExtras();
        requestid = bundle.getString("requestid");
        scanitems = new ArrayList<>();
        pushid = bundle.getString("pushid");
        System.out.println("pushid="+pushid);
        scanlist = findViewById(R.id.scanlotlist);
        submit = findViewById(R.id.submit);
        initScan();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                errorinfo = "";
                List<String> lotlist = new ArrayList<>();
                for (Out_LotDetail item : items) {
                    lotlist.add(item.getLotid());
                }

                for (Out_Scan scanitem : scanitems) {
                    for (int i = 0; i < lotlist.size(); i++) {
                        if (scanitem.getLotid().equals(lotlist.get(i))) {
                            lotlist.remove(i);
                        }
                    }}
                if(items.size()!=scanitems.size())
                {
                    Toast.makeText(Wuliu_OutActivity.this, "数量不一致，提交失败", Toast.LENGTH_LONG).show();
                return;
                }

                    if (lotlist.size() == 0) {
                        try {
                            Toast.makeText(Wuliu_OutActivity.this, "检查成功，已提交", Toast.LENGTH_LONG).show();
                            Runnable httptask = new Runnable() {
                                @Override
                                public void run() {
                                    RequestHandler handler = new RequestHandler();
                                    System.out.println(requestid+","+pushid);

                                    handler.outsubmitRequest(requestid, pushid);
                                }
                            };
                            Thread thread = new Thread(httptask);
                            thread.start();
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                        Wuliu_OutActivity.this.finish();
                    } else {
                        for (int i = 0; i < lotlist.size(); i++) {
                            errorinfo += lotlist.get(i) + ",";
                        }
                        errorinfo = errorinfo.substring(0, errorinfo.length() - 1);
                        errorinfo += "未Check,提交失败!";
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Wuliu_OutActivity.this, errorinfo, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            });
        try

            {
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
                    detailAdapter = new Out_LotWLDetailAdapter(this, R.layout.out_lotdetail, items);
                    wldetail.setAdapter(detailAdapter);
                } catch (Exception e) {
                    System.out.println(e);
                }
            } catch(
            Exception e)

            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Wuliu_OutActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }

        private void initScan () {
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
                        Toast.makeText(Wuliu_OutActivity.this, "InvalidScannerNameException:" + e.getMessage(), Toast.LENGTH_LONG).show();
                    } catch (UnsupportedPropertyException e) {
                        Toast.makeText(Wuliu_OutActivity.this, "UnsupportedPropertyException:" + e.getMessage(), Toast.LENGTH_LONG).show();
                    } catch (ScannerUnavailableException e) {
                        Toast.makeText(Wuliu_OutActivity.this, "ScannerUnavailableException:" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    //barcodeReader.addBarcodeListener((MainActivity) getActivity());
                    barcodeReader.addBarcodeListener(Wuliu_OutActivity.this);
                }
            });

        }

        @Override
        public void onBarcodeEvent (BarcodeReadEvent barcodeReadEvent){
            try {
                boolean iscontain = false;
                String raw = barcodeReadEvent.getBarcodeData();
                String flag_str = raw.substring(0, 2);
                String lot_str = raw.substring(2, raw.length());
                System.out.println(lot_str);
                for (int i = 0; i < scanitems.size(); i++) {
                    System.out.println("已扫描:" + scanitems.get(i).getLotid() + "scan:" + lot_str);
                    if (scanitems.get(i).getLotid().equals(lot_str)) {
                        System.out.println("11111");
                        iscontain = true;
                    }
                }
                if (iscontain) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("222222");
                            Toast.makeText(Wuliu_OutActivity.this, "该Lot已扫描，请勿重复扫描", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    scanitems.add(new Out_Scan(lot_str, "检查完成", "检查完成"));
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        scanAdapter = new Out_WLScanAdapter(Wuliu_OutActivity.this, R.layout.outwlscan, scanitems);
                        scanlist.setAdapter(scanAdapter);
                    }
                });

            } catch (Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Wuliu_OutActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                    }
                });

            }
        }

        @Override
        public void onFailureEvent (BarcodeFailureEvent barcodeFailureEvent){

        }
    }
