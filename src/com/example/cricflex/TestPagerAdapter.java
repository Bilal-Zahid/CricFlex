package com.example.cricflex;

/**
 * Created by abcd on 9/25/2016.
 */



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TestPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public TestPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                TestTabFragment1 tab1 = new TestTabFragment1();
                return tab1;
            case 1:
                TestTabFragment2 tab2 = new TestTabFragment2();
                return tab2;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}