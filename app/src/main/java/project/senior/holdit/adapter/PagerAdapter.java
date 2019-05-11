package project.senior.holdit.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import project.senior.holdit.fragment.order.SubTabBuyer;
import project.senior.holdit.fragment.order.SubTabSeller;

public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNoOfTabs;

    public PagerAdapter(FragmentManager fm, int NumberOfTabs) {
        super(fm);
        this.mNoOfTabs = NumberOfTabs;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {

            case 0:
                SubTabBuyer tab2 = new SubTabBuyer();
                return tab2;
            case 1:

                SubTabSeller tab1 = new SubTabSeller();
                return tab1;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}