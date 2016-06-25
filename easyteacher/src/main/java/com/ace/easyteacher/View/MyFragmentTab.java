package com.ace.easyteacher.View;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import java.util.ArrayList;

public class MyFragmentTab {
    private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();
    private FrameLayout mRealTabContent;
    private Context mContext;
    private FragmentManager mFragmentManager;
    private TabInfo mLastTab;
    private int containerid;

    static final class TabInfo {
        private final String tag;
        private Fragment fragment;

        TabInfo(String _tag, Fragment _fragment) {
            tag = _tag;
            fragment = _fragment;
        }
    }


    public void setup(Context context, FragmentManager manager, int _containerid) {
        mContext = context;
        mFragmentManager = manager;
        containerid = _containerid;
    }


    public void addTab(Fragment fragment, String tag) {

        TabInfo info = new TabInfo(tag, fragment);
        mTabs.add(info);
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.add(containerid, info.fragment, info.tag);
        ft.commitAllowingStateLoss();
    }

    public void setCurrentTab(String tag) {
        FragmentTransaction ft = doTabChanged(tag);
        if (ft != null) {
            ft.commitAllowingStateLoss();
        }
    }

    private FragmentTransaction doTabChanged(String tag) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        for (int i = 0; i < mTabs.size(); i++) {
            TabInfo tab = mTabs.get(i);
            if (tab.tag.equals(tag)) {
                ft.show(tab.fragment);
            } else {
                ft.hide(tab.fragment);
            }
        }
        return ft;
    }
}