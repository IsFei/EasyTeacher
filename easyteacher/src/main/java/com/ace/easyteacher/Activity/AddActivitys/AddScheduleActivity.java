package com.ace.easyteacher.Activity.AddActivitys;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.ace.easyteacher.Adapter.ScheduleAdapter222;
import com.ace.easyteacher.DataBase.ClassInfo;
import com.ace.easyteacher.DataBase.DBUtils;
import com.ace.easyteacher.DataBase.TeacherInfo;
import com.ace.easyteacher.DataBase.TeacherSchedule;
import com.ace.easyteacher.Http.HttpUtils;
import com.ace.easyteacher.Http.MHttpClient;
import com.ace.easyteacher.R;
import com.ace.easyteacher.Utils.ToastUtils;
import com.alibaba.fastjson.JSON;
import com.magicare.mutils.NetUtil;
import com.magicare.mutils.common.Callback;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddScheduleActivity extends AppCompatActivity {
    @Bind(R.id.rv_note)
    RecyclerView mRecyclerView;
    @Bind(R.id.fab)
    FloatingActionButton floatingActionButton;
    private ScheduleAdapter222 mAdapter;
    private List<TeacherSchedule> mList = new ArrayList<>();
    private TeacherSchedule mSchedule = new TeacherSchedule();
    private String[] mSubjectList = {"语文", "数学", "外语", "物理", "化学", "生物", "地理", "历史", "体育", "政治"};
    private String[] mClassArray;
    private String[] mTeacherArray;
    private final List<TeacherInfo> mTeacherList = new ArrayList<>();
    private String class_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);
        ButterKnife.bind(this);

        getTeacher();
        getClassInfo();

    }

    private void showChooseDialog(final int schedulePosition) {
//        mSchedule.setWeek("" + schedulePosition % 5);
//        int number = schedulePosition % 5 == 0 ? schedulePosition / 5 : schedulePosition / 5 + 1;
//        mSchedule.setNumber(number + "");
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择课表");
        View view = LayoutInflater.from(AddScheduleActivity.this).inflate(R.layout.schedule_dialog, null);
//        Spinner class_name = (Spinner) view.findViewById(R.id.sp_class);
        Spinner subject = (Spinner) view.findViewById(R.id.sp_subject);
        Spinner teacher = (Spinner) view.findViewById(R.id.sp_teacher);
//        ArrayAdapter<String> classAdapter = new ArrayAdapter<String>(AddScheduleActivity.this, android.R.layout.simple_spinner_item, mClassArray);
//        classAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        ArrayAdapter<String> subjectAdapter = new ArrayAdapter<String>(AddScheduleActivity.this, android.R.layout.simple_spinner_item, mSubjectList);
        subjectAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        ArrayAdapter<String> teacherAdapter = new ArrayAdapter<String>(AddScheduleActivity.this, android.R.layout.simple_spinner_item, mTeacherArray);
        teacherAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
//        class_name.setAdapter(classAdapter);
        subject.setAdapter(subjectAdapter);
        teacher.setAdapter(teacherAdapter);
//        class_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                mSchedule.setRoom(mClassArray[position]);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
        subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSchedule.setSubject_name(mSubjectList[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        teacher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSchedule.setTeacher_name(mTeacherArray[position]);
                for (TeacherInfo info : mTeacherList) {
                    if (info.getName().equals(mTeacherArray[position])) {
                        mSchedule.setJob_number(info.getJob_number());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        builder.setView(view);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!NetUtil.isNetWorkConnected(AddScheduleActivity.this)) {
                    ToastUtils.Toast(AddScheduleActivity.this, "请检查网络连接");
                    return;
                }
                mList.get(schedulePosition).setSubject_name(mSchedule.getSubject_name());
                mList.get(schedulePosition).setTeacher_name(mSchedule.getTeacher_name());
//                addSchedule();
                mAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @OnClick(R.id.fab)
    public void add() {
        for (TeacherSchedule bean : mList) {
            if (TextUtils.isEmpty(bean.getTeacher_name())) {
                ToastUtils.Toast(getApplicationContext(), "老师信息不全", 1000);
                return;
            }
        }
        addSchedule();
    }

    private void makeList() {
        mList.clear();
        int pos = 1;
        int week = 1;
        for (int i = 0; i < 35; i++) {
            TeacherSchedule teacherSchedule = new TeacherSchedule();
            teacherSchedule.setNumber(pos + "");
            teacherSchedule.setWeek(week + "");
            teacherSchedule.setJob_number("1");
            teacherSchedule.setRoom("1");
            teacherSchedule.setSubject_name("课程");
            teacherSchedule.setTeacher_name(mTeacherList.get(0).getName());
            teacherSchedule.setClass_name(class_name);
            mList.add(teacherSchedule);
            week += 1;
            if (week == 6) {
                pos += 1;
                week = 1;
            }

        }

    }

    private void getTeacher() {
        HttpUtils.HttpCallback<TeacherInfo> callback = new HttpUtils.HttpCallback<TeacherInfo>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                DbManager db = x.getDb(DBUtils.getStutdentInfoDaoConfig());
                try {
                    List<TeacherInfo> result = db.selector(TeacherInfo.class).findAll();
                    if (result != null && result.size() != 0) {
                        mTeacherList.clear();
                        result.remove(0);
                        if (result.size() > 0) {
                            mTeacherList.addAll(result);
                        } else {
                            ToastUtils.Toast(AddScheduleActivity.this, "没有教师信息");
                        }
                        mTeacherArray = new String[result.size()];
                        for (int i = 0; i < result.size(); i++) {
                            mTeacherArray[i] = result.get(i).getName();
                        }
                        makeList();
                    }
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(TeacherInfo result) {

            }

            @Override
            public void onSuccess(List<TeacherInfo> result) {

                mTeacherList.clear();
                result.remove(0);
                if (result.size() > 0) {
                    mTeacherList.addAll(result);
                } else {
                    ToastUtils.Toast(AddScheduleActivity.this, "没有教师信息");
                }
                mTeacherArray = new String[result.size()];
                for (int i = 0; i < result.size(); i++) {
                    mTeacherArray[i] = result.get(i).getName();
                }
                makeList();

            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        };
        MHttpClient.getTeacherInfo(callback);
    }

    private void addSchedule() {
        HttpUtils.HttpCallback<String> callback = new HttpUtils.HttpCallback<String>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ToastUtils.Toast(AddScheduleActivity.this, "上传失败");
            }

            @Override
            public void onSuccess(String result) {
                ToastUtils.Toast(AddScheduleActivity.this, "上传成功");
                finish();
            }

            @Override
            public void onSuccess(List<String> result) {

            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        };
        MHttpClient.addSchedule(JSON.toJSONString(mList), callback);
    }

    private void getClassInfo() {
        HttpUtils.HttpCallback<ClassInfo> callback = new HttpUtils.HttpCallback<ClassInfo>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                DbManager db = x.getDb(DBUtils.getStutdentInfoDaoConfig());
                try {
                    List<ClassInfo> result = db.selector(ClassInfo.class).findAll();
                    if (result != null && result.size() != 0) {
                        mClassArray = new String[result.size()];
                        for (int i = 0; i < result.size(); i++) {
                            mClassArray[i] = result.get(i).getClass_name();
                        }
                    }
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(ClassInfo result) {

            }

            @Override
            public void onSuccess(List<ClassInfo> result) {
                mClassArray = new String[result.size()];
                for (int i = 0; i < result.size(); i++) {
                    mClassArray[i] = result.get(i).getClass_name();
                }
                android.support.v7.app.AlertDialog.Builder builder;
                builder = new android.support.v7.app.AlertDialog.Builder(AddScheduleActivity.this);
                builder.setTitle("请选择班级")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setItems(mClassArray, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //换班级
                                class_name = mClassArray[which];
                                makeList();
                                mRecyclerView.setLayoutManager(new GridLayoutManager(AddScheduleActivity.this, 5));
                                mAdapter = new ScheduleAdapter222(mList);
                                mRecyclerView.setAdapter(mAdapter);
                                mAdapter.setOnItemClickListener(new ScheduleAdapter222.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        showChooseDialog(position);
                                    }
                                });
                            }
                        })
                        .show();


            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        };
        MHttpClient.getClassInfo(callback);
    }

}
