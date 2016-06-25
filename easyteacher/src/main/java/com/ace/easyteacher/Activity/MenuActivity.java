package com.ace.easyteacher.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ace.easyteacher.Activity.AddActivitys.AddClassActivity;
import com.ace.easyteacher.Activity.AddActivitys.AddExamActivity;
import com.ace.easyteacher.Activity.AddActivitys.AddScheduleActivity;
import com.ace.easyteacher.Activity.AddActivitys.AddScoreActivity;
import com.ace.easyteacher.Activity.AddActivitys.AddStuActivity;
import com.ace.easyteacher.Activity.AddActivitys.AddTeacherActivity;
import com.ace.easyteacher.Activity.DeleteActivitys.DeleteScheduleActivity;
import com.ace.easyteacher.Adapter.OtherTeacherAdapter;
import com.ace.easyteacher.Adapter.StudentAdapter;
import com.ace.easyteacher.DataBase.ClassInfo;
import com.ace.easyteacher.DataBase.DBUtils;
import com.ace.easyteacher.DataBase.Exam;
import com.ace.easyteacher.DataBase.StudentInfo;
import com.ace.easyteacher.DataBase.TeacherInfo;
import com.ace.easyteacher.Http.HttpUtils;
import com.ace.easyteacher.Http.MHttpClient;
import com.ace.easyteacher.R;
import com.ace.easyteacher.Utils.ToastUtils;
import com.ace.easyteacher.Utils.XToast;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.magicare.mutils.NetUtil;
import com.magicare.mutils.common.Callback;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.text)
    TextView hTextView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recycler_view_menu)
    RecyclerView mRecyclerView;
    @Bind(R.id.navigation_view)
    NavigationView mNavigationView;
    @Bind(R.id.drawer)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.swipe)
    SwipeRefreshLayout mSwip;

    private AlertDialog.Builder builder, builderSelected;
    private String[] mClass_name;
    private String[] mSelected = {"修改信息", "删除信息"};
    private static String WHICH_CLASS_SHOW;
    private static int WHICH_SHOW = 0;//0为学生 1为老师
    private List<StudentInfo> mListStudent;
    private StudentAdapter mAdapter;
    private OtherTeacherAdapter mAdapterT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);
//        getClassInfo();
//        getExamInfo();
//        getTeacherInfo();
//        mBtn_add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MenuActivity.this, AddMenuActivity.class);
////                startActivity(intent);
//                ActivityTransitionLauncher.with(MenuActivity.this).from(mBtn_add).launch(intent);
//            }
//        });
//        mBtn_delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MenuActivity.this, DeleteMenuActivity.class);
//                ActivityTransitionLauncher.with(MenuActivity.this).from(mBtn_delete).launch(intent);
//            }
//        });

        toolbar.setTitle("");
        setSupportActionBar(toolbar);

//        hTextView.setAnimateType(HTextViewType.SCALE);
        hTextView.setText("学生信息");
        hTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (WHICH_SHOW == 0) {
                    WHICH_SHOW = 1;
                    hTextView.setText("教师信息");
                    getTeacherInfo();
                } else if (WHICH_SHOW == 1) {
                    WHICH_SHOW = 0;
                    getAllClass();
                }
            }
        });

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(MenuActivity.this, mDrawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.syncState();//初始化状态
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mNavigationView.setNavigationItemSelectedListener(this);

        mSwip.setRefreshing(true);
        mSwip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllStudentsInfo(WHICH_CLASS_SHOW);
            }
        });
        getAllClass();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (WHICH_SHOW == 0) {
                    Intent intent = new Intent(MenuActivity.this, AddStuActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MenuActivity.this, AddTeacherActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void getTeacherInfo() {
        HttpUtils.HttpCallback<TeacherInfo> callback = new HttpUtils.HttpCallback<TeacherInfo>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d("ian", "fail");
                DbManager db = x.getDb(DBUtils.getStutdentInfoDaoConfig());
                try {
                    List<TeacherInfo> result = db.findAll(TeacherInfo.class);
                    initTeacherData(result);
                } catch (DbException e) {
                    ToastUtils.Toast(MenuActivity.this, "获取教师信息失败，请检查网络或联系管理员", 500);
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
                initTeacherData(result);
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

    private void initTeacherData(List<TeacherInfo> result) {
        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).getName().equals("管理员")) {
                result.remove(i);
                i = i - 1;
            }
        }
        mAdapterT = new OtherTeacherAdapter(MenuActivity.this, result);
        mRecyclerView.setAdapter(mAdapterT);
        mAdapterT.setOnItemClickListener(new OtherTeacherAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                builderSelected = new AlertDialog.Builder(MenuActivity.this);
                builderSelected.setTitle("选择操作")
                        .setNegativeButton("取消", null)
                        .setItems(mSelected, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    //修改信息

                                } else {
                                    //删除信息
                                    deleteStu(String.valueOf(mListStudent.get(position).getSid()));
                                }
                            }
                        })
                        .show();
            }
        });
    }

    private void getAllClass() {
        HttpUtils.HttpCallback<ClassInfo> callback = new HttpUtils.HttpCallback<ClassInfo>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                DbManager db = x.getDb(DBUtils.getStutdentInfoDaoConfig());
                try {
                    List<ClassInfo> result = db.selector(ClassInfo.class).findAll();
                    if (result != null && result.size() != 0) {
                        mClass_name = new String[result.size()];
                        for (int i = 0; i < result.size(); i++) {
                            mClass_name[i] = result.get(i).getClass_name();
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
                mClass_name = new String[result.size()];
                for (int i = 0; i < result.size(); i++) {
                    mClass_name[i] = result.get(i).getClass_name();
                }
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                hTextView.setText(mClass_name[0]);
                getAllStudentsInfo(mClass_name[0]);
                WHICH_CLASS_SHOW = mClass_name[0];
            }
        };
        MHttpClient.getClassInfo(callback);
    }

    private void getAllStudentsInfo(String class_name) {
        HttpUtils.HttpCallback<StudentInfo> callback = new HttpUtils.HttpCallback<StudentInfo>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ToastUtils.Toast(MenuActivity.this, "获取学生信息失败，请检查网络或联系管理员", 500);
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
        mAdapter = new StudentAdapter(MenuActivity.this, mListStudent);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MenuActivity.this));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new StudentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                builderSelected = new AlertDialog.Builder(MenuActivity.this);
                builderSelected.setTitle("选择操作")
                        .setNegativeButton("取消", null)
                        .setItems(mSelected, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    //修改信息

                                } else {
                                    //删除信息
                                    deleteStu(String.valueOf(mListStudent.get(position).getSid()));
                                }
                            }
                        })
                        .show();
            }
        });
    }

    private void deleteStu(String sid) {
        if (!NetUtil.isNetWorkConnected(MenuActivity.this)) {
            XToast.showToast(MenuActivity.this, "请检查网络连接");
            return;
        }
        HttpUtils.HttpCallback<String> callback = new HttpUtils.HttpCallback<String>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                XToast.showToast(MenuActivity.this, "删除失败");
            }

            @Override
            public void onSuccess(String result) {
                XToast.showToast(MenuActivity.this, "删除成功");
                getAllStudentsInfo(WHICH_CLASS_SHOW);
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
        MHttpClient.deleteStudent(sid, callback);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_class) {
            if (WHICH_SHOW == 0) {
                builder = new AlertDialog.Builder(MenuActivity.this);
                builder.setTitle("选择班级")
                        .setNegativeButton("取消", null)
                        .setItems(mClass_name, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //换班级
                                hTextView.setText(mClass_name[which]);
                                WHICH_CLASS_SHOW = mClass_name[which];
                                getAllStudentsInfo(WHICH_CLASS_SHOW);

                            }
                        })
                        .show();
            } else {
                ToastUtils.Toast(MenuActivity.this, "已是所有教师信息");
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_add_class:
                Intent intent = new Intent(MenuActivity.this, AddClassActivity.class);
                startActivity(intent);
                break;
            case R.id.item_add_grade:
                intent = new Intent(MenuActivity.this, AddScoreActivity.class);
                startActivity(intent);
                break;
            case R.id.item_add_exam:
                intent = new Intent(MenuActivity.this, AddExamActivity.class);
                startActivity(intent);
                break;
            case R.id.item_kebiao:
                String[] s = {"添加课表", "删除课表"};
                builder = new AlertDialog.Builder(MenuActivity.this);
                builder.setTitle("选择班级")
                        .setNegativeButton("取消", null)
                        .setItems(s, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    Intent intent = new Intent(MenuActivity.this, AddScheduleActivity.class);
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(MenuActivity.this, DeleteScheduleActivity.class);
                                    startActivity(intent);
                                }
                            }
                        })
                        .show();
                break;
        }
        item.setChecked(false);//点击了把它设为选中状态
        mDrawerLayout.closeDrawers();//关闭抽屉
        return true;
    }
}
