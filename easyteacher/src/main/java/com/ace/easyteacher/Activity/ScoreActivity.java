package com.ace.easyteacher.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.ace.easyteacher.Adapter.EndScoreAdapter;
import com.ace.easyteacher.DataBase.DBUtils;
import com.ace.easyteacher.DataBase.Exam;
import com.ace.easyteacher.DataBase.StudentGrade;
import com.ace.easyteacher.EasyTeacherApplication;
import com.ace.easyteacher.Http.HttpUtils;
import com.ace.easyteacher.Http.MHttpClient;
import com.ace.easyteacher.R;
import com.ace.easyteacher.Utils.ToastUtils;
import com.magicare.mutils.common.Callback;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class ScoreActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private final List<StudentGrade> mList = new ArrayList<>();
    private EndScoreAdapter mAdapter;
    private Exam mExam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_score);
        mExam = (Exam) getIntent().getSerializableExtra("exam");
        init();
        String class_name = EasyTeacherApplication.mApp.getWhichClass();
        class_name = class_name == null ? "高二19班" : class_name;
        getGradeByClass(class_name, String.valueOf(mExam.getType_id()));
    }

    private void init() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_end);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    private void getGradeByClass(String class_name, String type_id) {
        HttpUtils.HttpCallback<StudentGrade> callback = new HttpUtils.HttpCallback<StudentGrade>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d("ian", "fail");
                DbManager db = x.getDb(DBUtils.getStutdentInfoDaoConfig());
                try {
                    List<StudentGrade> result = db.findAll(StudentGrade.class);
                    if (result != null) {
                        mList.clear();
                        mList.addAll(result);
                        Collections.sort(mList, new Comparents());
                        setData();
                    }
                } catch (DbException e) {
                    ToastUtils.Toast(ScoreActivity.this, "获取考勤记录失败，请检查网络或联系管理员", 500);
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(StudentGrade result) {
                Log.d("ian", "success");
            }

            @Override
            public void onSuccess(List<StudentGrade> result) {
                Log.d("ian", "success" + result.size());
                DbManager db = x.getDb(DBUtils.getStutdentInfoDaoConfig());
                try {
                    db.delete(StudentGrade.class);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                for (StudentGrade studentInfo : result) {
                    try {
                        db.save(studentInfo);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
                mList.clear();
                mList.addAll(result);
                Collections.sort(mList, new Comparents());
                setData();
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        };
        MHttpClient.getGradeByClass(class_name, type_id, callback);
    }

    private void setData() {
        if (mAdapter == null) {
            mAdapter = new EndScoreAdapter(getApplicationContext(), mList);
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

    public class Comparents implements Comparator<StudentGrade> {
        @Override
        public int compare(StudentGrade arg0, StudentGrade arg1) {
            String one = arg0.getSid() + "";
            String two = arg1.getSid() + "";
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


}
