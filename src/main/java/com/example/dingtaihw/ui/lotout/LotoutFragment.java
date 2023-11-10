package com.example.dingtaihw.ui.lotout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.dingtaihw.DataBase.DB_EUtils;
import com.example.dingtaihw.ui.Activitys.Lot_OutActivity;
import com.example.dingtaihw.Model.Out.Lot_Out;
import com.example.dingtaihw.Model.Out.Out_Wuliu;
import com.example.dingtaihw.R;
import com.example.dingtaihw.ui.Activitys.Wuliu_OutActivity;
import com.example.dingtaihw.databinding.FragmentLotoutBinding;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class LotoutFragment extends Fragment {

    private FragmentLotoutBinding binding;
    ListView outlist;
    ListView wuliulist;
    List<Lot_Out> items;
    List<Out_Wuliu> w_items;
    OutListAdapter out_adapter;
    Out_wlAdapter out_wlAdapter;
    private View root;
    Button rebtn;

    public LotoutFragment() {
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
        binding = FragmentLotoutBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        outlist = binding.chd;
        wuliulist=binding.wuliu;
        rebtn=binding.refresh;
        outlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle1 = getActivity().getIntent().getExtras();
                Bundle bundle = new Bundle();
                bundle.putString("requestid", items.get(i).getRequestid());
                bundle.putString("pushid", bundle1.getString("pushid"));
                Intent intent = new Intent(getActivity(), Lot_OutActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        wuliulist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle1 = getActivity().getIntent().getExtras();
                Bundle bundle = new Bundle();
                bundle.putString("requestid", w_items.get(i).getRequesid());
                bundle.putString("pushid", bundle1.getString("pushid"));
                Intent intent = new Intent(getActivity(), Wuliu_OutActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        rebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final RotateAnimation animation = new RotateAnimation(0.0f, 180.0f,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF,   0.5f);
                animation.setDuration(500);
                rebtn.startAnimation( animation );

                getItem();
            }
        });
        getItem();
        return root;
    }
    private void getItem()
    {try {
        Runnable oracleTask = new Runnable() {
            @Override
            public void run() {
                items = DB_EUtils.getOutList();
                w_items=DB_EUtils.getWLList();
            }
        };
        Thread thread = new Thread(oracleTask);
        thread.start();
        try {
            thread.join();
            out_adapter = new OutListAdapter(getActivity(), R.layout.outlot, items);
            out_wlAdapter=new Out_wlAdapter(getActivity(),R.layout.wloutlot,w_items);
            outlist.setAdapter(out_adapter);
            wuliulist.setAdapter(out_wlAdapter);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    } catch (Exception e) {
        System.out.println(e.toString());
    }

    }
}