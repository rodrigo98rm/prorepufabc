package mayer.rodrigo.prorepufabc;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import mayer.rodrigo.prorepufabc.Fragments.ReportsListFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    int numTabs;

    public PagerAdapter(FragmentManager fm, int numTabs) {
        super(fm);
        this.numTabs = numTabs;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();

        switch (position){
            case 0:
                ReportsListFragment fragment0 = new ReportsListFragment();
                bundle.putInt(ReportsListFragment.FRAGMENT_TYPE, ReportsListFragment.RECENTS);
                fragment0.setArguments(bundle);
                return fragment0;
            case 1:
            default:
                ReportsListFragment fragment1 = new ReportsListFragment();
                bundle.putInt(ReportsListFragment.FRAGMENT_TYPE, ReportsListFragment.POPULAR);
                fragment1.setArguments(bundle);
                return fragment1;
        }



    }

    @Override
    public int getCount() {
        return numTabs;
    }
}
