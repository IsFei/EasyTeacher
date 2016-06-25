package com.ace.easyteacher.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ace.easyteacher.Activity.MainActivity;
import com.ace.easyteacher.Activity.StudentInfoActivity;
import com.ace.easyteacher.Adapter.StudentAdapter;
import com.ace.easyteacher.DataBase.DBUtils;
import com.ace.easyteacher.DataBase.StudentInfo;
import com.ace.easyteacher.Http.HttpUtils;
import com.ace.easyteacher.Http.MHttpClient;
import com.ace.easyteacher.R;
import com.ace.easyteacher.Utils.DestinyUtils;
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
 * Created by lenovo on 2016-3-23.
 */
public class StudentFragment extends Fragment {

    @Bind(R.id.recycler_view_student)
    RecyclerView mRecyclerView;
    //    @Bind(R.id.rotateloading)
//    RotateLoading mLoading;
    @Bind(R.id.swip)
    SwipeRefreshLayout mSwip;

    private StudentAdapter mAdapter;
    private List<StudentInfo> mListStudent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student, container, false);
        ButterKnife.bind(this, view);
//        mLoading.start();
        mSwip.setRefreshing(true);
        mListStudent = new ArrayList<StudentInfo>();
        mSwip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllStudentsInfo(((MainActivity) getActivity()).getmApp().getTeacherInfo().getClass_name());
            }
        });
        try {
            String s=((MainActivity)getActivity()).getmApp().getTeacherInfo().getClass_name();
            getAllStudentsInfo(s);
        }catch (Exception e){
            Log.e("main",e.toString());
        }

        return view;
    }

    public void startAnim() {
        mRecyclerView.setTranslationY(DestinyUtils.getScreenHeight(getActivity()));
        mRecyclerView.animate().translationY(0).setDuration(500).start();
    }

    private void getAllStudentsInfo(String class_name) {
        HttpUtils.HttpCallback<StudentInfo> callback = new HttpUtils.HttpCallback<StudentInfo>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d("ian", "fail");
                DbManager db = x.getDb(DBUtils.getStutdentInfoDaoConfig());
                try {
                    mListStudent = db.findAll(StudentInfo.class);
                    initData(mListStudent);
                } catch (DbException e) {
                    ToastUtils.Toast(getActivity(), "获取学生信息失败，请检查网络或联系管理员", 500);
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
//                mLoading.stop();
                Log.d("ian", "success" + result.size());
                mListStudent = result;
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
    private void initData(final List<StudentInfo> mListStudent) {
        mAdapter = new StudentAdapter(getActivity(), mListStudent);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new StudentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), StudentInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", mListStudent.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
    public void refresh() {
//        mRecyclerView.scrollToPosition(0);
        mSwip.setRefreshing(true);
        getAllStudentsInfo(((MainActivity)getActivity()).getmApp().getTeacherInfo().getClass_name());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
