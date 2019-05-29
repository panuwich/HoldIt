package project.senior.holdit.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import project.senior.holdit.report.SalesReport;
import project.senior.holdit.report.TypeReport;

public class PagerReportAdapter extends FragmentStatePagerAdapter {

    int mNoOfTabs;

    public PagerReportAdapter(FragmentManager fm, int NumberOfTabs) {
        super(fm);
        this.mNoOfTabs = NumberOfTabs;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {

            case 0:
                TypeReport tab1 = new TypeReport();
                return tab1;
            case 1:

                SalesReport tab2 = new SalesReport();
                return tab2;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}