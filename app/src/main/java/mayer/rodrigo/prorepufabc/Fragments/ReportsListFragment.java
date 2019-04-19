package mayer.rodrigo.prorepufabc.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mayer.rodrigo.prorepufabc.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportsListFragment extends Fragment {

    public static final int RECENTS = 1, POPULAR = 2, USER = 3;
    public static final String FRAGMENT_TYPE = "type";

    //Views
    private TextView textView;


    public ReportsListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reports_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Views
        textView = view.findViewById(R.id.textView_test_ReportsFragment);

        int type = getArguments().getInt(FRAGMENT_TYPE, 1);

        if(type == RECENTS){
            textView.setText("Recent reports");
        }else if(type == POPULAR){
            textView.setText("Popular reports");
        }

    }
}
