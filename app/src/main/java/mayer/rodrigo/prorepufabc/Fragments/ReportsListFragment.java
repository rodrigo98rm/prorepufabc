package mayer.rodrigo.prorepufabc.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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

    private FirebaseFirestore db;
    private ArrayList<Report> reports = new ArrayList<>();

    //Views
    private RecyclerView recyclerView;
    ReportListAdapter adapter;


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

        db = FirebaseFirestore.getInstance();

        //Views
        recyclerView = view.findViewById(R.id.recyclerView_ReportsFragment);
        adapter = new ReportListAdapter(reports);
        recyclerView.setAdapter(adapter);

        int type = getArguments().getInt(FRAGMENT_TYPE, 1);

        if(type == RECENTS){

            db.collection("reports").orderBy("timestamp", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for (final QueryDocumentSnapshot document : task.getResult()) {

                            //User
                            String userId = document.getString("user_id");

                            db.collection("users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()){
                                        User user = task.getResult().toObject(User.class);

                                        Report report = document.toObject(Report.class);
                                        report.setUser(user);
                                        reports.add(report);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            });

                        }
                    }
                }
            });

//            User user = new User("Rodrigo Rominho Mayer", "https://firebasestorage.googleapis.com/v0/b/prorepufabc.appspot.com/o/images%2FIz5K1w1F8AQPrwZpJBPCWwMOKQg1.jpg?alt=media&token=4f27659d-acb0-42ca-8943-29df12551307");
//            ArrayList<String> photosUrls = new ArrayList<>();
//            photosUrls.add("https://i.imgur.com/Ls8jpOim.jpg");
//            photosUrls.add("https://i.imgur.com/sVqliwHm.jpg");
//            Report report = new Report(user, "Bebedouro quebrado", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.", 15, 1555268610000L, new ArrayList<>(photosUrls));
//            reports.add(report);
//
//            photosUrls.clear();
//            photosUrls.add("https://i.imgur.com/iEDJJkrm.jpg");
//            photosUrls.add("https://i.imgur.com/iEDJJkrm.jpg");
//            report = new Report(user, "Saída de emergência danificada", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.", 1345, 1555268610000L, new ArrayList<>(photosUrls));
//            reports.add(report);

        }else if(type == POPULAR){
            User user = new User("Rodrigo Rominho Mayer", "https://firebasestorage.googleapis.com/v0/b/prorepufabc.appspot.com/o/images%2FIz5K1w1F8AQPrwZpJBPCWwMOKQg1.jpg?alt=media&token=4f27659d-acb0-42ca-8943-29df12551307");
            ArrayList<String> photosUrls = new ArrayList<>();

            photosUrls.add("https://i.imgur.com/iEDJJkrm.jpg");
            photosUrls.add("https://i.imgur.com/iEDJJkrm.jpg");
            Report report = new Report(user, "Saída de emergência danificada", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.", 1345, 1555268610000L, new ArrayList<>(photosUrls));
            reports.add(report);

            photosUrls.clear();
            photosUrls.add("https://i.imgur.com/Ls8jpOim.jpg");
            photosUrls.add("https://i.imgur.com/sVqliwHm.jpg");
            report = new Report(user, "Bebedouro quebrado", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.", 15, 1555268610000L, new ArrayList<>(photosUrls));
            reports.add(report);
        }else if(type == USER){

            User user = new User("Rodrigo Rominho Mayer", "https://firebasestorage.googleapis.com/v0/b/prorepufabc.appspot.com/o/images%2FIz5K1w1F8AQPrwZpJBPCWwMOKQg1.jpg?alt=media&token=4f27659d-acb0-42ca-8943-29df12551307");
            ArrayList<String> photosUrls = new ArrayList<>();
            photosUrls.add("https://i.imgur.com/DJiE2pEm.jpg");
            photosUrls.add("https://i.imgur.com/dG1K9JZm.jpg");
            Report report = new Report(user, "Goteiras no piso vermelho", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.", 0, 1555268610000L, new ArrayList<>(photosUrls));
            reports.add(report);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

    }
}
