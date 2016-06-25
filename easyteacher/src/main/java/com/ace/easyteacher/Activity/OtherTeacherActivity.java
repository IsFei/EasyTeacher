package com.ace.easyteacher.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.ace.easyteacher.Adapter.OtherTeacherAdapter;
import com.ace.easyteacher.DataBase.DBUtils;
import com.ace.easyteacher.DataBase.TeacherInfo;
import com.ace.easyteacher.Http.HttpUtils;
import com.ace.easyteacher.Http.MHttpClient;
import com.ace.easyteacher.R;
import com.ace.easyteacher.Utils.ToastUtils;
import com.magicare.mutils.common.Callback;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lenovo on 2016-4-11.
 */
public class OtherTeacherActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recycler_view_teacher)
    RecyclerView mRecyclerView;
    @Bind(R.id.swip)
    SwipeRefreshLayout mSwip;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    private OtherTeacherAdapter mAdapter;
    private android.support.v7.app.AlertDialog.Builder builder,buiderCall;
    private String[] mTel=null;
    private String[] mClassName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_teacher);
        ButterKnife.bind(this);
        toolbar.setTitle("其它教师信息");
        toolbar.setBackgroundColor(MainActivity.BACKGROUND_COLOR);
        setSupportActionBar(toolbar);
        fab.setScaleX(0f);
        fab.setScaleY(0f);
        fab.setVisibility(View.VISIBLE);
        mSwip.setRefreshing(true);
        getTeacherInfo();
        mSwip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTeacherInfo();
            }
        });
    }

    private void getTeacherInfo() {
        HttpUtils.HttpCallback<TeacherInfo> callback = new HttpUtils.HttpCallback<TeacherInfo>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback){
                Log.d("ian", "fail");
                DbManager db = x.getDb(DBUtils.getStutdentInfoDaoConfig());
                try {
                    List<TeacherInfo> result = db.findAll(TeacherInfo.class);
                    initData(result);
                } catch (DbException e) {
                    ToastUtils.Toast(OtherTeacherActivity.this, "获取教师信息失败，请检查网络或联系管理员", 500);
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(TeacherInfo result) {
                Log.d("ian", "success");
            }

            @Override
            public void onSuccess(final List<TeacherInfo> result) {
                Log.d("ian", "success" + result.size());
                DbManager db = x.getDb(DBUtils.getStutdentInfoDaoConfig());
                try {
                    db.delete(TeacherInfo.class);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                for (TeacherInfo exam : result) {
                    try {
                        db.save(exam);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
                initData(result);
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                mSwip.setRefreshing(false);
            }
        };
        MHttpClient.getTeacherInfo(callback);
    }

    private void initData(final List<TeacherInfo> result) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(OtherTeacherActivity.this));
        //删除管理员信息
        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).getName().equals("管理员")) {
                result.remove(i);
                i = i - 1;
            }
        }
        mAdapter = new OtherTeacherAdapter(OtherTeacherActivity.this, result);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new OtherTeacherAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                mClassName = result.get(position).getClass_name().split(",");
                String s = mClassName[0];
                mClassName[0] = s + "(主要班级)";
                builder = new android.support.v7.app.AlertDialog.Builder(OtherTeacherActivity.this);
                builder.setTitle("授课情况")
                        .setNegativeButton("关闭", null)
                        .setItems(mClassName, null)
                        .show();
            }
        });
        //添加数组
        mTel = new String[result.size()];
        for (int i = 0; i < result.size(); i++) {
            mTel[i] = result.get(i).getName() + "老师:" + result.get(i).getTel();
        }
        //数据加载完显示fab
        fab.animate().scaleX(1).scaleY(1).setDuration(500).start();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buiderCall = new android.support.v7.app.AlertDialog.Builder(OtherTeacherActivity.this);
                buiderCall.setTitle("拨打电话")
                        .setNegativeButton("关闭", null)
                        .setItems(mTel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_CALL);
                                intent.setData(Uri.parse("tel:" + result.get(which).getTel()));
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        });
    }
}
