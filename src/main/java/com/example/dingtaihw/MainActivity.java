package com.example.dingtaihw;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dingtaihw.Model.Goods;
import com.example.dingtaihw.Model.LotItem;
import com.example.dingtaihw.Tools.util.RequestHandler;
import com.example.dingtaihw.ui.home.GoodAdapter;
import com.example.dingtaihw.ui.login.Login;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dingtaihw.databinding.ActivityMainBinding;
import com.honeywell.aidc.AidcManager;
import com.honeywell.aidc.BarcodeReader;
import com.honeywell.aidc.InvalidScannerNameException;
import com.honeywell.aidc.ScannerUnavailableException;
import com.honeywell.aidc.UnsupportedPropertyException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

public class MainActivity extends AppCompatActivity  {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private AidcManager manager;
    private BarcodeReader barcodeReader;
    private static List<Goods> goods = new ArrayList<>();
    private static List<Goods> currentgoods = new ArrayList<>();

    private static List<LotItem> Lots = new ArrayList<>();
    private ListView listView;
    private ListView clistview;
    private ListView lotlistview;
    private boolean isdone = false;
    private GoodAdapter goodAdapter;
    private GoodAdapter currentAdapter;

    private int currentframentno = 2131231055;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        listView = findViewById(R.id.goodslist);
        goodAdapter = new GoodAdapter(this, R.layout.good_item, goods);
        currentAdapter = new GoodAdapter(this, R.layout.good_item, currentgoods);
        clistview = findViewById(R.id.currentlist);
        listView.setAdapter(goodAdapter);
        clistview.setAdapter(currentAdapter);
        try{
            Runnable httptask=new Runnable() {
                @Override
                public void run() {
                    RequestHandler handler=new RequestHandler();
                   // handler.submitRequest("623638","734");
                }
            };
            Thread thread=new Thread(httptask);
            thread.start();
        } catch (Exception e)
        {
            System.out.println(e.toString());
        }

        Bundle bundle = getIntent().getExtras();
        setSupportActionBar(binding.appBarMain.toolbar);

//        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        View headview = navigationView.getHeaderView(0);
        TextView userid = headview.findViewById(R.id.idtext);
        TextView name = headview.findViewById(R.id.textView);
        userid.setText(bundle.getString("userid"));
        name.setText(bundle.getString("showname"));
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,R.id.nav_lotout,R.id.nav_setting,R.id.nav_location)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                currentframentno = navDestination.getId();
            }
        });
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void initScan() {
        AidcManager.create(MainActivity.this, new AidcManager.CreatedCallback() {
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
                    Toast.makeText(MainActivity.this, "InvalidScannerNameException:" + e.getMessage(), Toast.LENGTH_LONG).show();
                } catch (UnsupportedPropertyException e) {
                    System.out.println(e.getMessage());
                    Toast.makeText(MainActivity.this, "UnsupportedPropertyException:" + e.getMessage(), Toast.LENGTH_LONG).show();
                } catch (ScannerUnavailableException e) {
                    System.out.println(e.getMessage());
                    Toast.makeText(MainActivity.this, "ScannerUnavailableException:" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                System.out.println("barcodeinfo" + barcodeReader.getAllDefaultProperties());
               // barcodeReader.addBarcodeListener(MainActivity.this);
                barcodeReader.addBarcodeListener((BarcodeReader.BarcodeListener) getSupportFragmentManager().findFragmentById(R.id.nav_slideshow));
            }
        });
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.action_settings);
        Intent intent = new Intent(this, Login.class);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                //startActivity(intent);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            }
        });
        return super.onPrepareOptionsMenu(menu);
    }



    @Override
    protected void onStart() {

        super.onStart();

    }
}