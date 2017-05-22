package com.project.eelhea.vaportalk_android.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.project.eelhea.vaportalk_android.FragEventTab;
import com.project.eelhea.vaportalk_android.FragFriendListTab;
import com.project.eelhea.vaportalk_android.FragSettingTab;
import com.project.eelhea.vaportalk_android.FragTalkListTab;

/**
 * Created by eelhea on 2017. 4. 5..
 */

public class MainPagerAdapter extends FragmentStatePagerAdapter {

    int _numOfTabs;

    public MainPagerAdapter(FragmentManager fragmentManager, int numOfTabs){
        super(fragmentManager);
        this._numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                FragFriendListTab fragFriendListTab = new FragFriendListTab();
                return fragFriendListTab;
            case 1:
                FragTalkListTab fragTalkListTab = new FragTalkListTab();
                return fragTalkListTab;
            case 2:
                FragEventTab fragEventTab = new FragEventTab();
                return  fragEventTab;
            case 3:
                FragSettingTab fragSettingTab = new FragSettingTab();
                return fragSettingTab;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return _numOfTabs;
    }
}
