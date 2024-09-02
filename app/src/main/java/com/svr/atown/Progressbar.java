package com.svr.atown;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class Progressbar extends Fragment {


    public Progressbar() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_progressbar, container, false);
        ProgressBar progressBar=view.findViewById(R.id.progressBar);
        progressBar.setIndeterminate(false);
        return view;
    }
}
