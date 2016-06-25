package com.ace.easyteacher.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.ace.easyteacher.Adapter.ScheduleAdapter;
import com.ace.easyteacher.DataBase.DBUtils;
import com.ace.easyteacher.DataBase.TeacherSchedule;
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
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by Xman on 2016/3/25
 */
public class TeacherScheduleActivity extends Activity {
    @Bind(R.id.rv_note)
    RecyclerView recyclerView;
    ScheduleAdapter adapter;
    List<TeacherSchedule> mList = new ArrayList<>();
    List<TeacherSchedule> newList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_schedule);
        ButterKnife.bind(this);
        recyclerView.setLayoutManager(new GridLayoutManager(TeacherScheduleActivity.this, 5));
        adapter = new ScheduleAdapter(newList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ScheduleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                final MaterialDialog mMaterialDialog = new MaterialDialog(TeacherScheduleActivity.this);
                mMaterialDialog.setTitle("课程信息")
                        .setMessage("教师: " + newList.get(position).getTeacher_name() + "\n"
                                + "教室： " + newList.get(position).getRoom() + "\n"
                                + "课程名称: " + newList.get(position).getSubject_name())
                        .setPositiveButton("关闭", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mMaterialDialog.dismiss();
                            }
                        });
                mMaterialDialog.show();
            }
        });
        initDatas();
    }

    private void initDatas() {
        newList.clear();

        String class_name = EasyTeacherApplication.mApp.getWhichClass();
        class_name = class_name == null ? "高二19班" : class_name;
        getScheduleByClass(class_name);

    }

    private void getScheduleByClass(String class_name) {
        HttpUtils.HttpCallback<TeacherSchedule> callback = new HttpUtils.HttpCallback<TeacherSchedule>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d("ian", "fail");
                Log.d("ian", "fail");
                DbManager db = x.getDb(DBUtils.getStutdentInfoDaoConfig());
                try {
                    List<TeacherSchedule> result = db.findAll(TeacherSchedule.class);
                    mList = result;
                    for (int i = 1; i <= 7; i++) {
                        for (TeacherSchedule teacher : mList) {
                            if (Integer.valueOf(teacher.getNumber()) == i) {
                                newList.add(teacher);
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                } catch (DbException e) {
                    ToastUtils.Toast(TeacherScheduleActivity.this, "获取课表信息失败，请检查网络或联系管理员", 500);
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(TeacherSchedule result) {
                Log.d("ian", "success");
            }

            @Override
            public void onSuccess(List<TeacherSchedule> result) {
                DbManager db = x.getDb(DBUtils.getStutdentInfoDaoConfig());
                try {
                    db.delete(TeacherSchedule.class);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                for (TeacherSchedule studentInfo : result) {
                    try {
                        db.save(studentInfo);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
                mList = result;
                for (int i = 1; i <= 7; i++) {
                    for (TeacherSchedule teacher : mList) {
                        if (Integer.valueOf(teacher.getNumber()) == i) {
                            newList.add(teacher);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        };
        MHttpClient.getScheduleByClass(class_name, callback);
    }

    private void getList(List<TeacherSchedule> mList, int i) {
        TeacherSchedule ss = new TeacherSchedule();
        ss.setNumber("" + i);
        ss.setSubject_name("物理");
        mList.add(ss);
        TeacherSchedule ss1 = new TeacherSchedule();
        ss1.setNumber("" + i);
        ss1.setSubject_name("化学");
        mList.add(ss1);
        TeacherSchedule ss2 = new TeacherSchedule();
        ss2.setNumber("" + i);
        ss2.setSubject_name("生物");
        mList.add(ss2);
        TeacherSchedule ss3 = new TeacherSchedule();
        ss3.setNumber("" + i);
        ss3.setSubject_name("政治");
        mList.add(ss3);
        TeacherSchedule ss4 = new TeacherSchedule();
        ss4.setNumber("" + i);
        ss4.setSubject_name("历史");
        mList.add(ss4);
    }
}
