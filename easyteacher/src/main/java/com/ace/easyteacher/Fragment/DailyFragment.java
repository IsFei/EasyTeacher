package com.ace.easyteacher.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ace.easyteacher.Activity.NewNoteFActivity;
import com.ace.easyteacher.Adapter.NoteAdapter;
import com.ace.easyteacher.Bean.ResultBean;
import com.ace.easyteacher.ConfigManager;
import com.ace.easyteacher.DataBase.DBUtils;
import com.ace.easyteacher.DataBase.Note;
import com.ace.easyteacher.Http.HttpUtils;
import com.ace.easyteacher.Http.MHttpClient;
import com.ace.easyteacher.R;
import com.ace.easyteacher.Utils.ToastUtils;
import com.alibaba.fastjson.JSON;
import com.magicare.mutils.common.Callback;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.drakeet.materialdialog.MaterialDialog;

public class DailyFragment extends Fragment {

    private int mJob_Number;
    private View mRootView;
    private RecyclerView mRv;
    private NoteAdapter mAdapter;
    private final List<Note> mList = new ArrayList<>();
    @Bind(R.id.fab)
    FloatingActionButton button;
    @Bind(R.id.swip)
    SwipeRefreshLayout swip;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_note, container, false);
        ButterKnife.bind(this, mRootView);
        initView();
        return mRootView;
    }

    private void initDatas() {
        swip.setRefreshing(true);
        HttpUtils.HttpCallback<Note> callback = new HttpUtils.HttpCallback<Note>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d("ian", "fail");
                DbManager db = x.getDb(DBUtils.getStutdentInfoDaoConfig());
                try {
                    List<Note> result = db.findAll(Note.class);
                    mList.clear();
                    mList.addAll(result);
                    refresh();
                } catch (DbException e) {
                    ToastUtils.Toast(getActivity(), "获取日志信息失败，请检查网络或联系管理员", 500);
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(Note result) {
                Log.d("ian", "success");
            }

            @Override
            public void onSuccess(List<Note> result) {
                DbManager db = x.getDb(DBUtils.getStutdentInfoDaoConfig());
                try {
                    db.delete(Note.class);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                for (Note note : result) {
                    try {
                        db.save(note);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
                mList.clear();
                mList.addAll(result);
                refresh();

            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        };
        MHttpClient.getNotes(ConfigManager.getStringValue(getActivity(), ConfigManager.JOB_NUMBER), callback);

    }

    @Override
    public void onResume() {
        super.onResume();
        initDatas();

    }

    private void refresh() {
        swip.setRefreshing(true);
        mAdapter.notifyDataSetChanged();

    }

    public void setJobNumber(int job_number) {
        mJob_Number = job_number;
    }

    private void initView() {
        mRv = (RecyclerView) mRootView.findViewById(R.id.rv_note);
        mRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new NoteAdapter(mList);
        mAdapter.setOnItemClickListener(new NoteAdapter.itemListener() {
            @Override
            public void onItemClick(View view, int position) {
                showDialog(mList.get(position));
            }
        });
        mRv.setAdapter(mAdapter);
    }

    private void showDialog(final Note note) {
        final MaterialDialog dialog = new MaterialDialog(getActivity());
        dialog.setTitle(note.getCreate_time())
                .setMessage(note.getContent())
                .setPositiveButton("删除", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteNote(JSON.toJSONString(note));
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("关闭", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                    }
                });

        dialog.show();
    }

    @OnClick(R.id.fab)
    public void add() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), NewNoteFActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private void deleteNote(String content) {
        swip.setRefreshing(true);
        HttpUtils.HttpCallback<ResultBean> callback = new HttpUtils.HttpCallback<ResultBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onSuccess(ResultBean result) {
                initDatas();
            }

            @Override
            public void onSuccess(List<ResultBean> result) {
                initDatas();
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                initDatas();
            }
        };
        MHttpClient.deleteNote(content, callback);
    }

}
