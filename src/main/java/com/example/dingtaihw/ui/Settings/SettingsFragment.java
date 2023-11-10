package com.example.dingtaihw.ui.Settings;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dingtaihw.DataBase.DB_EUtils;
import com.example.dingtaihw.MainActivity;
import com.example.dingtaihw.Model.LotItem;
import com.example.dingtaihw.R;
import com.example.dingtaihw.Tools.util.RequestHandler;
import com.example.dingtaihw.databinding.FragmentLotoutBinding;
import com.example.dingtaihw.databinding.FragmentSettingsBinding;
import com.example.dingtaihw.ui.slideshow.LotAdapter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SettingsFragment extends Fragment {
    private FragmentSettingsBinding binding;
    private View root;
    EditText nodeid;
    EditText outid;
    EditText wuliuid;
    Map<String, String> current = new HashMap<>();
    Map<String, String> change = new HashMap<>();

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        Button savebutton = binding.savebutton;

        try {
            Runnable oracletask = new Runnable() {
                @Override
                public void run() {
                    current = DB_EUtils.getSettings();
                }
            };
            Thread thread = new Thread(oracletask);
            thread.start();
            try{thread.join();
                nodeid = binding.llNodeid;
                outid=binding.outNodeid;
                wuliuid=binding.wuliuNodeid;
                nodeid.setText(current.get("Nodeid"));
                outid.setText(current.get("Outid"));
                wuliuid.setText(current.get("Wuliu"));
            }
            catch (Exception e)
            {

            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change.put("Nodeid", String.valueOf(nodeid.getText()));
                change.put("Outid", String.valueOf(outid.getText()));
                change.put("Wuliu",String.valueOf(wuliuid.getText()));
                try {
                    Runnable oracletask = new Runnable() {
                        @Override
                        public void run() {
                            if (DB_EUtils.UpdateSettings(change)) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    };
                    Thread thread = new Thread(oracletask);
                    thread.start();
                } catch (Exception e) {
                    System.out.println(e.toString());
                }

            }
        });
        return root;
    }
}