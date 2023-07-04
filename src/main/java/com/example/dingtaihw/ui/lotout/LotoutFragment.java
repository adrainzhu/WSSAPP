package com.example.dingtaihw.ui.lotout;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dingtaihw.R;
import com.example.dingtaihw.databinding.FragmentHomeBinding;
import com.example.dingtaihw.databinding.FragmentLotoutBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LotoutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LotoutFragment extends Fragment {

private FragmentLotoutBinding binding;

private View root;
    public LotoutFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LotoutFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LotoutFragment newInstance(String param1, String param2) {
        LotoutFragment fragment = new LotoutFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
        root=binding.getRoot();
        return  root;
    }
}