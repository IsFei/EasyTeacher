package com.ace.easyteacher.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ace.easyteacher.Adapter.PeopleAdapter;
import com.ace.easyteacher.DataBase.DBUtils;
import com.ace.easyteacher.DataBase.StudentInfo;
import com.ace.easyteacher.DataBase.TeacherInfo;
import com.ace.easyteacher.EasyTeacherApplication;
import com.ace.easyteacher.Http.HttpUtils;
import com.ace.easyteacher.Http.MHttpClient;
import com.ace.easyteacher.R;
import com.ace.easyteacher.Utils.MessageInfoUtils;
import com.ace.easyteacher.Utils.ToastUtils;
import com.magicare.mutils.common.Callback;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.Serializable;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lenovo on 2016-4-9.
 */
public class PeopleForSendMessageActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recycler_view_people)
    RecyclerView mRecyclerView;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.swip)
    SwipeRefreshLayout mSwip;
    @Bind(R.id.text_all)
    TextView mTextAll;
    //    @Bind(R.id.recycler_view_people)
//    RecyclerView mRecyclerView;

    private PeopleAdapter mAdapter;
    private List<StudentInfo> mListStudent;
    private TeacherInfo mTeacherInfo;
    private TextView name, number;
    private ImageView mCheck;
    private List<MessageInfoUtils> mListPeople, mListCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_send_message);
        ButterKnife.bind(this);
        toolbar.setTitle("选择联系人");
        toolbar.setBackgroundColor(MainActivity.BACKGROUND_COLOR);
        setSupportActionBar(toolbar);

        fab.setScaleX(0f);
        fab.setScaleY(0f);
        fab.setVisibility(View.VISIBLE);

        mTeacherInfo = ((EasyTeacherApplication) PeopleForSendMessageActivity.this.getApplication()).getTeacherInfo();
        mSwip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllStudentsInfo(mTeacherInfo.getClass_name());
            }
        });

        initData();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除无用电话号码
                int before = mListCheck.size();
                for (int i = 0; i < mListCheck.size(); ) {
                    if (mListCheck.get(i).getNum().length() != 11) {
                        mListCheck.remove(i);
                        continue;
                    }
                    i += 1;
                }
                int change = before - mListCheck.size();
                if (mListCheck.size() == 0) {
                    if (change != 0) {
                        ToastUtils.Toast(PeopleForSendMessageActivity.this, "您选择了" + change + "个无用电话号码", 500);
                    }else ToastUtils.Toast(PeopleForSendMessageActivity.this, "还没选择要发送的人", 500);
                } else {
                    if (change != 0) {
                        ToastUtils.Toast(PeopleForSendMessageActivity.this, "已经帮您删除了" + change + "个无用电话号码", 500);
                    }
                    Intent intent = new Intent(PeopleForSendMessageActivity.this, SendMessageActivity.class);
                    intent.putExtra("list", (Serializable) mListCheck);
                    startActivity(intent);
                }
            }
        });
    }

    private void initData() {
        mListPeople = new ArrayList<>();
        mListCheck = new ArrayList<>();
        EasyTeacherApplication mApp = (EasyTeacherApplication) PeopleForSendMessageActivity.this.getApplication();
        mTeacherInfo = mApp.getTeacherInfo();
        getAllStudentsInfo(mTeacherInfo.getClass_name());
    }


    private void getAllStudentsInfo(String class_name) {
        HttpUtils.HttpCallback<StudentInfo> callback = new HttpUtils.HttpCallback<StudentInfo>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                DbManager db = x.getDb(DBUtils.getStutdentInfoDaoConfig());
                try {
                    mListStudent = db.findAll(StudentInfo.class);
                    initData(mListStudent);
                } catch (DbException e) {
                    ToastUtils.Toast(PeopleForSendMessageActivity.this, "获取学生信息失败，请检查网络或联系管理员", 500);
                    e.printStackTrace();
                }
                mSwip.setRefreshing(false);
            }

            @Override
            public void onSuccess(StudentInfo result) {
                Log.d("ian", "success");
            }

            @Override
            public void onSuccess(List<StudentInfo> result) {
                Log.d("ian", "success" + result.size());
                mListStudent = result;
                if (mListStudent == null || mListStudent.size() == 0) {
                    return;
                }
                DbManager db = x.getDb(DBUtils.getStutdentInfoDaoConfig());
                try {
                    db.delete(StudentInfo.class);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                for (StudentInfo studentInfo : result) {
                    try {
                        db.save(studentInfo);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
                mListStudent = result;
                Iterator<StudentInfo> iter = mListStudent.iterator();
                while(iter.hasNext()){
                    StudentInfo s = iter.next();
                    if(TextUtils.isEmpty(s.getFather_tel()) && TextUtils.isEmpty(s.getMother_tel())){
                        iter.remove();
                    }
                }

                Collections.sort(mListStudent, new Comparents());
                initData(mListStudent);
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                mSwip.setRefreshing(false);
            }
        };
        MHttpClient.getAllStudentsInfo(class_name, callback);
    }
    public class Comparents implements Comparator<StudentInfo> {
        @Override
        public int compare(StudentInfo arg0, StudentInfo arg1) {
            String one = arg0.getName();
            String two = arg1.getName();
            Collator ca = Collator.getInstance(Locale.CHINA);
            int flags = 0;
            if (ca.compare(one, two) < 0) {
                flags = -1;
            } else if (ca.compare(one, two) > 0) {
                flags = 1;
            } else {
                flags = 0;
            }
            return flags;
        }
    }
    private void initData(List<StudentInfo> mListStudent){
        for (int i = 0; i < mListStudent.size(); i++) {
            mListPeople.add(new MessageInfoUtils(mListStudent.get(i).getFather(), mListStudent.get(i).getFather_tel()));
            mListPeople.add(new MessageInfoUtils(mListStudent.get(i).getMother(), mListStudent.get(i).getMother_tel()));
        }
        mAdapter = new PeopleAdapter(PeopleForSendMessageActivity.this, mListStudent, mListPeople);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(PeopleForSendMessageActivity.this));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new PeopleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                name = (TextView) view.findViewById(R.id.people_name);
                mCheck = (ImageView) view.findViewById(R.id.check);
                if (mListPeople.get(position).getIsCheck()) {//该人已经被选择，点击后取消选中
                    mCheck.setVisibility(View.GONE);
                    mListPeople.get(position).setIsCheck(false);
                    //从选择list里删除该人
                    for (int j = 0; j < mListCheck.size(); j++) {
                        if (mListCheck.get(j).getNum().equals(mListPeople.get(position).getNum())) {
                            mListCheck.remove(j);
                        }
                    }
                } else {//选中某人
                    mCheck.setVisibility(View.VISIBLE);
                    mListPeople.get(position).setIsCheck(true);
                    mListCheck.add(mListPeople.get(position));
                }
            }
        });
        mTextAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListCheck = mListPeople;
                //把所有人的选中状态改为true
                for (int i = 0; i < mListPeople.size(); i++) {
                    mListPeople.get(i).setIsCheck(true);
                }
                mAdapter.notifyDataSetChanged();
            }
        });
        fab.animate().scaleX(1).scaleY(1).setDuration(500).start();
    }
}