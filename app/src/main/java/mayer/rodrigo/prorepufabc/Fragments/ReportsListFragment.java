package mayer.rodrigo.prorepufabc.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import mayer.rodrigo.prorepufabc.Adapters.ReportListAdapter;
import mayer.rodrigo.prorepufabc.Model.Report;
import mayer.rodrigo.prorepufabc.Model.User;
import mayer.rodrigo.prorepufabc.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportsListFragment extends Fragment {

    public static final int RECENTS = 1, POPULAR = 2, USER = 3;
    public static final String FRAGMENT_TYPE = "type";

    private ArrayList<Report> reports = new ArrayList<>();

    //Views
    private RecyclerView recyclerView;


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
        recyclerView = view.findViewById(R.id.recyclerView_ReportsFragment);

        int type = getArguments().getInt(FRAGMENT_TYPE, 1);

        if(type == RECENTS){
            User user = new User("Rodrigo Rominho Mayer", "https://firebasestorage.googleapis.com/v0/b/prorepufabc.appspot.com/o/images%2FIz5K1w1F8AQPrwZpJBPCWwMOKQg1.jpg?alt=media&token=4f27659d-acb0-42ca-8943-29df12551307");
            ArrayList<String> photosUrls = new ArrayList<>();
            photosUrls.add("https://images.unsplash.com/photo-1553075712-453f7213c24f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=634&q=80");
            photosUrls.add("https://firebasestorage.googleapis.com/v0/b/prorepufabc.appspot.com/o/images%2FIz5K1w1F8AQPrwZpJBPCWwMOKQg1.jpg?alt=media&token=4f27659d-acb0-42ca-8943-29df12551307");
            Report report = new Report(user, "Bebedouro quebrado", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.", 1345, 1555268610000L, photosUrls);
            reports.add(report);
        }else if(type == POPULAR){
            User user = new User("Rodrigo Rominho Mayer", "https://firebasestorage.googleapis.com/v0/b/prorepufabc.appspot.com/o/images%2FIz5K1w1F8AQPrwZpJBPCWwMOKQg1.jpg?alt=media&token=4f27659d-acb0-42ca-8943-29df12551307");
            ArrayList<String> photosUrls = new ArrayList<>();
            photosUrls.add("https://images.unsplash.com/photo-1553075712-453f7213c24f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=634&q=80");
            photosUrls.add("https://firebasestorage.googleapis.com/v0/b/prorepufabc.appspot.com/o/images%2FIz5K1w1F8AQPrwZpJBPCWwMOKQg1.jpg?alt=media&token=4f27659d-acb0-42ca-8943-29df12551307");
            Report report = new Report(user, "Saída de emergência danificada", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.", 1345, 1555268610000L, photosUrls);
            reports.add(report);
        }else if(type == USER){
            User user = new User("Rodrigo Rominho Mayer", "https://firebasestorage.googleapis.com/v0/b/prorepufabc.appspot.com/o/images%2FIz5K1w1F8AQPrwZpJBPCWwMOKQg1.jpg?alt=media&token=4f27659d-acb0-42ca-8943-29df12551307");
            ArrayList<String> photosUrls = new ArrayList<>();
            photosUrls.add("https://images.unsplash.com/photo-1555570181-c590c39f61fc?ixlib=rb-1.2.1&auto=format&fit=crop&w=1050&q=80");
            photosUrls.add("https://images.unsplash.com/photo-1555579877-8887dfaed207?ixlib=rb-1.2.1&auto=format&fit=crop&w=1601&q=80");
            Report report = new Report(user, "Goteira na rampa", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.", 1345, 1555268610000L, new ArrayList<>(photosUrls));
            reports.add(report);

            photosUrls.clear();
            photosUrls.add("https://images.unsplash.com/photo-1555284023-249222086985?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1050&q=80");
            photosUrls.add("https://images.unsplash.com/photo-1555086196-a28d9bc9d677?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1050&q=80");
            report = new Report(user, "Buraco no teto", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.", 1345, 1555268610000L, new ArrayList<>(photosUrls));
            reports.add(report);
        }

        ReportListAdapter adapter = new ReportListAdapter(reports);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

    }
}
