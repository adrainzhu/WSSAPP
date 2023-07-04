package com.example.dingtaihw.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dingtaihw.DataBase.DB_EUtils;
import com.example.dingtaihw.MainActivity;
import com.example.dingtaihw.Model.Goods;
import com.example.dingtaihw.Model.LL.RequestParts;
import com.example.dingtaihw.Model.LL.SendParts;
import com.example.dingtaihw.Model.LL.SuggestParts;
import com.example.dingtaihw.R;
import com.example.dingtaihw.Tools.util.RequestHandler;
import com.example.dingtaihw.ui.gallery.RequestPartsAdapter;
import com.example.dingtaihw.ui.gallery.SendPartsAdapter;
import com.example.dingtaihw.ui.gallery.SuggestPartsAdapter;
import com.example.dingtaihw.ui.home.GoodAdapter;
import com.example.dingtaihw.ui.home.HomeFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.honeywell.aidc.AidcManager;
import com.honeywell.aidc.BarcodeFailureEvent;
import com.honeywell.aidc.BarcodeReadEvent;
import com.honeywell.aidc.BarcodeReader;
import com.honeywell.aidc.InvalidScannerNameException;
import com.honeywell.aidc.ScannerUnavailableException;
import com.honeywell.aidc.TriggerStateChangeEvent;
import com.honeywell.aidc.UnsupportedPropertyException;
import com.example.dingtaihw.Model.LL.DragFloatActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LL_Detail extends AppCompatActivity implements BarcodeReader.BarcodeListener, BarcodeReader.TriggerListener {
    String dh;
    String sqr;
    String type;
    String pushuser;
    String requestid;
    private AidcManager manager;
    private BarcodeReader barcodeReader;
    List<RequestParts> requestParts = new ArrayList<>();
    List<SuggestParts> suggestParts = new ArrayList<>();
    List<SendParts> sendParts = new ArrayList<>();
    ListView requestlistView;
    ListView suggestListView;

    ListView sendListView;
    SendPartsAdapter sendPartsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ll_detail);
        initScan();
        Bundle bundle = getIntent().getExtras();
        dh = bundle.getString("lldh");
        sqr = bundle.getString("sqr");
        type = bundle.getString("type");
        pushuser = bundle.getString("pushid");
        requestid = bundle.getString("requestid");
        requestlistView = findViewById(R.id.requestpartslist);
        suggestListView = findViewById(R.id.suggestpartslist);
        sendListView = findViewById(R.id.senddpartslist);
        System.out.println("当前用户ID：" + pushuser + "/" + requestid);
        DragFloatActionButton floatingActionButton = findViewById(R.id.mybtn);

        try {
            Runnable oralcethread = new Runnable() {
                @Override
                public void run() {
                    requestParts = DB_EUtils.getRquestParts(dh);
                    suggestParts = DB_EUtils.getSuggestParts(dh);
                }
            };
            Thread thread = new Thread(oralcethread);
            thread.start();
            try {
                thread.join();
                RequestPartsAdapter requestPartsAdapter = new RequestPartsAdapter(this, R.layout.requestparts, requestParts);
                SuggestPartsAdapter suggestPartsAdapter = new SuggestPartsAdapter(this, R.layout.suggestparts, suggestParts);
                suggestListView.setAdapter(suggestPartsAdapter);
                requestlistView.setAdapter(requestPartsAdapter);
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        sendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SendParts s = (SendParts) adapterView.getAdapter().getItem(i);
                s.setPno(s.getPno());
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Integer> s_map = new HashMap<>();//建议发料map
                HashMap<String, Integer> f_map = new HashMap<>();//实际发料map;
                boolean isequal = true;
                if (suggestParts.size() > 0 && sendParts.size() > 0) {
                    //遍历List维护HashMap
                    for (int i = 0; i < suggestParts.size(); i++) {
                        try {

                            if (s_map.containsKey(suggestParts.get(i).getLh())) {
                                int t = s_map.get(suggestParts.get(i).getLh()) + Integer.parseInt(suggestParts.get(i).getSuggestnum());
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    s_map.replace(suggestParts.get(i).getLh(), s_map.get(suggestParts.get(i).getSuggestnum()), t);
                                }
                            } else {
                                s_map.put(suggestParts.get(i).getLh(), Integer.parseInt(suggestParts.get(i).getSuggestnum()));
                            }
                        } catch (Exception e) {
                            System.out.println(e.toString());
                        }
                    }
                    for (int i = 0; i < sendParts.size(); i++) {
                        try {
                            if (f_map.containsKey(sendParts.get(i).getLh())) {
                                int t = f_map.get(sendParts.get(i).getLh()) + Integer.parseInt(sendParts.get(i).getSendnum());
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    f_map.replace(sendParts.get(i).getLh(), f_map.get(sendParts.get(i).getSendnum()), t);
                                }
                            } else {
                                f_map.put(sendParts.get(i).getLh(), Integer.parseInt(sendParts.get(i).getSendnum()));
                            }
                        } catch (Exception e) {
                            System.out.println(e.toString());
                        }
                    }
                    //比较两个HashMap
                    if (s_map.size() != f_map.size()) {
                        isequal = false;
                    }
                    for (Map.Entry<String, Integer> entry : s_map.entrySet()) {
                        if (f_map.get(entry.getKey()) != entry.getValue()) {
                            isequal = false;
                        }
                    }
                    if (isequal) {
                        Toast.makeText(LL_Detail.this, "发料成功", Toast.LENGTH_LONG).show();
                        try {
                            Runnable httptask = new Runnable() {
                                @Override
                                public void run() {
                                    RequestHandler handler = new RequestHandler();
                                    handler.submitRequest(requestid, pushuser, sendParts);
                                }
                            };
                            Thread thread = new Thread(httptask);
                            thread.start();
                        } catch (Exception e) {
                            System.out.println(e.toString());
                        }


                    } else {
                        Toast.makeText(LL_Detail.this, "发料信息与建议信息不一致，请检查", Toast.LENGTH_LONG).show();
                    }


                } else {
                    Toast.makeText(LL_Detail.this, "无建议发料或扫码发料信息", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(LL_Detail.this, "InvalidScannerNameException:" + e.getMessage(), Toast.LENGTH_LONG).show();
                } catch (UnsupportedPropertyException e) {
                    Toast.makeText(LL_Detail.this, "UnsupportedPropertyException:" + e.getMessage(), Toast.LENGTH_LONG).show();
                } catch (ScannerUnavailableException e) {
                    Toast.makeText(LL_Detail.this, "ScannerUnavailableException:" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                //barcodeReader.addBarcodeListener((MainActivity) getActivity());
                barcodeReader.addBarcodeListener(LL_Detail.this);
            }
        });


    }


    @Override
    public void onBarcodeEvent(BarcodeReadEvent barcodeReadEvent) {

        try {
            Runnable oracleTask = new Runnable() {
                @Override
                public void run() {
                    SendParts sendPart = new SendParts();
                    String raw = barcodeReadEvent.getBarcodeData();
                    String[] result = raw.split("\\+");
                    sendPart.setLh(result[0]);
                    sendPart.setPno(result[1]);
                    sendPart.setSendnum(result[2]);
                    sendPart = DB_EUtils.getPartsinfo(sendPart);
                    sendParts.add(sendPart);

                }
            };
            Thread thread = new Thread(oracleTask);
            thread.start();
            try {
                thread.join();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        sendPartsAdapter = new SendPartsAdapter(LL_Detail.this, R.layout.sendparts, sendParts);
                        sendListView.setAdapter(sendPartsAdapter);
                    }
                });
            } catch (Exception e) {
                System.out.println(e.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.toString());
        }


    }

    @Override
    public void onFailureEvent(BarcodeFailureEvent barcodeFailureEvent) {
        System.out.println(barcodeFailureEvent.toString());
    }

    @Override
    public void onTriggerEvent(TriggerStateChangeEvent triggerStateChangeEvent) {

    }
}