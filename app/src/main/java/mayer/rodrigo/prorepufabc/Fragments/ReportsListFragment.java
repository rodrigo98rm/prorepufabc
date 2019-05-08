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
import com.google.firebase.auth.FirebaseAuth;
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

    private FirebaseAuth auth;
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
        auth = FirebaseAuth.getInstance();

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

        }else if(type == POPULAR){

            db.collection("reports").orderBy("upvotes", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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

        }else if(type == USER){

            db.collection("reports").whereEqualTo("user_id", auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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


        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

    }
}
