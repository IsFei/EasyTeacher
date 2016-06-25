package com.ace.easyteacher.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.ace.easyteacher.Adapter.ClassCheckAdapter;
import com.ace.easyteacher.DataBase.Check;
import com.ace.easyteacher.DataBase.DBUtils;
import com.ace.easyteacher.EasyTeacherApplication;
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

/**
 * Created by lenovo on 2016-3-25.
 */
public class ClassCheckActivity extends AppCompatActivity{
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private String mSid="高二19班";
    private final List<Check> mList = new ArrayList<>();
    private ClassCheckAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_check);
        ButterKnife.bind(this);
        toolbar.setTitle("考勤记录");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_check);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        String class_name = EasyTeacherApplication.mApp.getWhichClass();
        mSid = class_name == null ? "高二19班" : class_name;
        if (mSid != null) {
            getCheckByClass(mSid);
        }
    }

    private void getCheckByClass(String sid) {
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
                    ToastUtils.Toast(ClassCheckActivity.this, "获取考勤记录失败，请检查网络或联系管理员", 500);
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
        MHttpClient.getCheckByClass(sid, callback);
    }

    private void initData() {
        if (mAdapter == null) {
            mAdapter = new ClassCheckAdapter(ClassCheckActivity.this, mList);
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
