package com.ace.easyteacher.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ace.easyteacher.Activity.ScoreActivity;
import com.ace.easyteacher.Adapter.ScoreAdapter;
import com.ace.easyteacher.DataBase.DBUtils;
import com.ace.easyteacher.DataBase.Exam;
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
 * Created by lenovo on 2016-3-23.
 */
public class ScoreFragment extends Fragment {

    @Bind(R.id.recycler_view_score)
    RecyclerView mRecyclerView;

    private ScoreAdapter mAdapter;
    private List<Exam> result;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.score_fragment, container, false);
        ButterKnife.bind(this, view);
        getExam();
        return view;
    }

    private void getExam() {
        HttpUtils.HttpCallback<Exam> callback = new HttpUtils.HttpCallback<Exam>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d("ian", "fail");
                Log.d("ian", "fail");
                DbManager db = x.getDb(DBUtils.getStutdentInfoDaoConfig());
                try {
                    result = db.findAll(Exam.class);
                    initData(result);
                } catch (DbException e) {
                    ToastUtils.Toast(getActivity(), "获取考试信息失败，请检查网络或联系管理员", 500);
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(Exam result) {
                Log.d("ian", "success");
            }

            @Override
            public void onSuccess(final List<Exam> result) {
                DbManager db = x.getDb(DBUtils.getStutdentInfoDaoConfig());
                try {
                    db.delete(Exam.class);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                for (Exam exam : result) {
                    try {
                        db.save(exam);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
                Log.d("ian", "success" + result.size());
                initData(result);
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        };
        MHttpClient.getExam(callback);
    }
    private void initData(final List<Exam> result){
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ScoreAdapter(getActivity(), result);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new ScoreAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), ScoreActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("exam", result.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
