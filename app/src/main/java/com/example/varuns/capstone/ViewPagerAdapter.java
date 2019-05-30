package com.example.varuns.capstone;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class ViewPagerAdapter extends FragmentPagerAdapter {

    private String title[] = {"Bio", "Products"};
    private int artisanId;

    public ViewPagerAdapter(FragmentManager manager, int artisanId) {
        super(manager);
        this.artisanId = artisanId;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return BioFragment.getInstance(position, artisanId);
        } else {
            return ProductsFragment.getInstance(position, artisanId);
        }
    }

    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }

    public int getArtisanId() {
        return artisanId;
    }

    public void setArtisanId(int artisanId) {
        this.artisanId = artisanId;
    }
}
