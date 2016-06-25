package com.ace.easyteacher.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.ace.easyteacher.Adapter.TabFragmentAdapter;
import com.ace.easyteacher.DataBase.StudentInfo;
import com.ace.easyteacher.Fragment.StudentInfoFragment;
import com.ace.easyteacher.Fragment.StudentScoreFragment;
import com.ace.easyteacher.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lenovo on 2016-3-23.
 */
public class StudentInfoActivity extends AppCompatActivity {
    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;
    @Bind(R.id.viewpager)
    ViewPager mViewPager;
    @Bind(R.id.rotateloading)
    RotateLoading mLoading;

    private List<String> mTitleList;
    private StudentInfo mStudentInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_stu_info);
        ButterKnife.bind(this);
        Intent intent = this.getIntent();
        mStudentInfo = (StudentInfo) intent.getSerializableExtra("user");
//        mLoading.start();
        initTittleName();
        initView();
    }

    public StudentInfo getStudent() {
        return mStudentInfo;
    }

    private void initView() {
        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(new StudentInfoFragment());
        fragments.add(new StudentScoreFragment());
        TabFragmentAdapter fragmentAdapter = new TabFragmentAdapter(getSupportFragmentManager(), fragments, mTitleList);
        mTabLayout.setTabsFromPagerAdapter(fragmentAdapter);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mViewPager.setAdapter(fragmentAdapter);//给ViewPager设置适配器
        mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。
    }

    private void initTittleName() {
        mTitleList = new ArrayList<String>();
        mTitleList.add("学生信息");
        mTitleList.add("学生成绩");
    }
}
