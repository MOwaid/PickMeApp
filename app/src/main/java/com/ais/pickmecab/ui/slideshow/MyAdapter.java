package com.ais.pickmecab.ui.slideshow;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

class MyAdapter extends FragmentPagerAdapter {

    int totalTabs;
    public MyAdapter(SlideshowFragment c, FragmentManager fm, int totalTabs) {
        super(fm);

        this.totalTabs = totalTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Accepted AcceptedFragment = new Accepted();
                return AcceptedFragment;
            case 1:
                Allocated AllocatedFragment = new Allocated();
                return AllocatedFragment;
            case 2:
                Completed CompletedFragment = new Completed();
                return CompletedFragment;
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return totalTabs;
    }
}