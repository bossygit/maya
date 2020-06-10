package com.nasande.pharmaciemayamaya.ui;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.nasande.pharmaciemayamaya.R;

import java.io.InputStream;

public class OrdonnanceFragment extends Fragment {

    private OrdonnanceViewModel mViewModel;


    public static OrdonnanceFragment newInstance() {
        return new OrdonnanceFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.ordonnance_fragment, container, false);


        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(OrdonnanceViewModel.class);
        // TODO: Use the ViewModel

    }



}
