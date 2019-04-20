package mayer.rodrigo.prorepufabc.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import mayer.rodrigo.prorepufabc.Fragments.ReportsListFragment;
import mayer.rodrigo.prorepufabc.R;

import android.os.Bundle;

public class MyReportsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reports);

        getSupportActionBar().setTitle("Meus relatos");

        insertFragment();


    }

    private void insertFragment(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ReportsListFragment fragment = new ReportsListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ReportsListFragment.FRAGMENT_TYPE, ReportsListFragment.USER);
        fragment.setArguments(bundle);

        ft.replace(R.id.frameLayout_MyReports, fragment);

        ft.commit();
    }

}
