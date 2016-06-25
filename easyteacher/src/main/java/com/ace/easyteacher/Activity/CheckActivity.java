package com.ace.easyteacher.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ace.easyteacher.Adapter.CheckAdapter;
import com.ace.easyteacher.DataBase.Check;
import com.ace.easyteacher.DataBase.DBUtils;
import com.ace.easyteacher.Http.HttpUtils;
import com.ace.easyteacher.Http.MHttpClient;
import com.ace.easyteacher.R;
import com.ace.easyteacher.Utils.ToastUtils;
import com.magicare.mutils.common.Callback;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CheckActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    private RecyclerView mRecyclerView;
    private String mSid;
    private final List<Check> mList = new ArrayList<>();
    private CheckAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        ButterKnife.bind(this);
        toolbar.setTitle("考勤记录");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckActivity.this, NewCheckActivity.class);
                intent.putExtra("sid",mSid);
                startActivity(intent);
            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_check);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        getSid();
        if (mSid != null) {
            getCheckBySid(mSid);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCheckBySid(mSid);
    }

    private void getSid() {
        mSid = getIntent().getStringExtra("sid");
    }

    private void getCheckBySid(String sid) {
        HttpUtils.HttpCallback<Check> callback = new HttpUtils.HttpCallback<Check>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                DbManager db = x.getDb(DBUtils.getStutdentInfoDaoConfig());
                try {
                    List<Check> result = db.findAll(Check.class);
                    mList.clear();
                    mList.addAll(result);
                    initData();
                } catch (DbException e) {
                    ToastUtils.Toast(CheckActivity.this, "获取考勤记录失败，请检查网络或联系管理员", 500);
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(Check result) {

            }

            @Override
            public void onSuccess(List<Check> result) {

                DbManager db = x.getDb(DBUtils.getStutdentInfoDaoConfig());
                try {
                    db.delete(Check.class);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                for (Check studentInfo : result) {
                    try {
                        db.save(studentInfo);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }

                mList.clear();
                mList.addAll(result);
                initData();
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        };
        MHttpClient.getCheckBySid(sid, callback);
    }

    private void initData() {
        if (mAdapter == null) {
            mAdapter = new CheckAdapter(CheckActivity.this, mList);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
