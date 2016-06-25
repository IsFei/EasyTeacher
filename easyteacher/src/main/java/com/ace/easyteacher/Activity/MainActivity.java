package com.ace.easyteacher.Activity;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ace.easyteacher.ConfigManager;
import com.ace.easyteacher.DataBase.DBUtils;
import com.ace.easyteacher.DataBase.TeacherInfo;
import com.ace.easyteacher.EasyTeacherApplication;
import com.ace.easyteacher.Fragment.DailyFragment;
import com.ace.easyteacher.Fragment.MineFragment;
import com.ace.easyteacher.Fragment.ScoreFragment;
import com.ace.easyteacher.Fragment.StudentFragment;
import com.ace.easyteacher.Http.HttpUtils;
import com.ace.easyteacher.Http.MHttpClient;
import com.ace.easyteacher.R;
import com.ace.easyteacher.Utils.ToastUtils;
import com.ace.easyteacher.View.MyFragmentTab;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.magicare.mutils.common.Callback;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabSelectedListener;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.drakeet.materialdialog.MaterialDialog;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.bottom_bar)
    BottomBar mBottomBar;
    @Bind(R.id.myCoordinator)
    CoordinatorLayout myCoordinator;

    private EasyTeacherApplication mApp;

    public static int BACKGROUND_COLOR = 0xFF03a9f4;
    private static int WHICH_CHECKED = R.string.student;//0代表学生  1代表日志   2代表成绩  3我的
    private ValueAnimator colorAnim;//变色动画
    private String[] mClassName = null;
    private android.support.v7.app.AlertDialog.Builder builder;

    private StudentFragment mStu;
    private DailyFragment mDaily;
    private ScoreFragment mScore;
    private MineFragment mMine;
    private MyFragmentTab tabhost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mToolbar.setTitle("EasyTeacher");
        mStu = new StudentFragment();
        mMine = new MineFragment();
        mScore = new ScoreFragment();
        mDaily = new DailyFragment();
        getTeacherInfoByJob(ConfigManager.getStringValue(MainActivity.this, ConfigManager.JOB_NUMBER), savedInstanceState);
        checkPassword();
        setSupportActionBar(mToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_message) {
            //发短信
            Intent intent = new Intent(MainActivity.this, PeopleForSendMessageActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_class) {
            builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
            builder.setTitle("选择班级")
                    .setNegativeButton("取消", null)
                    .setItems(mClassName, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //换班级
                            mApp.setWhichClass(mClassName[which]);
                            mApp.setTeacherInfo(mClassName[which]);
                            mStu.refresh();
                            mMine.refresh();
//                            ToastUtils.Toast(MainActivity.this, "所有信息已换至" + mClassName[which] + "，请刷新", 500);
                        }
                    })
                    .show();
        } else if (id == R.id.menu_teacher) {
            Intent intent = new Intent(MainActivity.this, OtherTeacherActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void getTeacherInfoByJob(String job_number, final Bundle savedInstanceState) {
        HttpUtils.HttpCallback<TeacherInfo> callback = new HttpUtils.HttpCallback<TeacherInfo>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d("ian", "fail");
                DbManager db = x.getDb(DBUtils.getStutdentInfoDaoConfig());
                try {
                    List<TeacherInfo> result = db.findAll(TeacherInfo.class);
                    mApp = (EasyTeacherApplication) MainActivity.this.getApplication();
                    TeacherInfo teacherInfo = result.get(0);
                    mClassName = teacherInfo.getClass_name().split(",");
                    teacherInfo.setClass_name(mClassName[0]);
                    mApp.setTeacherInfo(teacherInfo);
                    initView(savedInstanceState);
                } catch (DbException e) {
                    ToastUtils.Toast(MainActivity.this, "获取教师信息失败，请检查网络或联系管理员", 500);
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(TeacherInfo result) {
                Log.d("ian", "success" + result);
            }

            @Override
            public void onSuccess(List<TeacherInfo> result) {
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
                mApp = (EasyTeacherApplication) MainActivity.this.getApplication();
                TeacherInfo teacherInfo = result.get(0);
                mClassName = teacherInfo.getClass_name().split(",");
                teacherInfo.setClass_name(mClassName[0]);
                mApp.setTeacherInfo(teacherInfo);
                mApp.setWhichClass(mClassName[0]);
                initView(savedInstanceState);
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        };
        MHttpClient.getTeacherInfoByJob(job_number, callback);
    }

    private void initView(Bundle savedInstanceState) {

        tabhost = new MyFragmentTab();
        tabhost.setup(this, getSupportFragmentManager(), R.id.frame_main);
        tabhost.addTab(mStu, "student");
        tabhost.addTab(mDaily,"mDaily");
        tabhost.addTab(mScore,"mScore");
        tabhost.addTab(mMine,"mMine");

        mBottomBar = BottomBar.attachShy(myCoordinator, findViewById(R.id.frame_main), savedInstanceState);
        mBottomBar.setItemsFromMenu(R.menu.bottombar_menu, new OnMenuTabSelectedListener() {
            @Override
            public void onMenuItemSelected(int resId) {
                if (resId == R.id.bottomBarItemOne) {
                    tabhost.setCurrentTab("student");
                    changeFragment(R.string.student, mStu, 0xFF03a9f4);
                }
                if (resId == R.id.bottomBarItemTwo) {
                    tabhost.setCurrentTab("mDaily");
                    changeFragment(R.string.daily, mDaily, 0xFFe91e63);
                }
                if (resId == R.id.bottomBarItemThree) {
                    tabhost.setCurrentTab("mScore");
                    changeFragment(R.string.score, mScore, 0xFF0277bd);
                }
                if (resId == R.id.bottomBarItemFour) {
                    tabhost.setCurrentTab("mMine");
                    changeFragment(R.string.mine, mMine, 0xFFcddc39);
                }
            }
        });
        mBottomBar.mapColorForTab(0, "#03a9f4");
        mBottomBar.mapColorForTab(1, "#e91e63");
        mBottomBar.mapColorForTab(2, "#0277bd");
        mBottomBar.mapColorForTab(3, "#cddc39");
        tabhost.setCurrentTab("student");
    }


    private void changeFragment(final int name, final Fragment fragment, int newColor) {
        if (name == WHICH_CHECKED) {
            //刷新
            if (name == R.string.student) {
                mStu.refresh();
            }
        } else {
            colorAnim = ObjectAnimator.ofInt(mToolbar, "backgroundColor", BACKGROUND_COLOR, newColor);
            colorAnim.setDuration(200);
            colorAnim.setEvaluator(new ArgbEvaluator());
            colorAnim.start();
            BACKGROUND_COLOR = newColor;
            WHICH_CHECKED = name;
//            mFragmentManager = getSupportFragmentManager();
//            mTransaction = mFragmentManager.beginTransaction();
//            mTransaction.replace(R.id.frame_main, fragment);
//            mTransaction.commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mBottomBar.onSaveInstanceState(outState);
    }
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
////        login("500323000", "123456");
////        getAllStudentsInfo("高二19班");
////        getNotes("500323000");
////        getGradeBySid("20160218001");
////        getGradeByClass("高二19班","2");
////        getTeacherInfo();
////        getTeacherInfoByJob("500323000");
////        getExam();
////        getScheduleByClass("高二19班");
////        getScheduleByWeek("1");
////        write();
////        read();
//        checkPassword();
//    }

//    private void login(String user, String pwd) {
//        HttpUtils.HttpCallback<ResultBean> callback = new HttpUtils.HttpCallback<ResultBean>() {
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                Log.d("ian", "fail");
//            }
//
//            @Override
//            public void onSuccess(ResultBean result) {
//                Log.d("ian", "success");
//            }
//
//            @Override
//            public void onSuccess(List<ResultBean> result) {
//                Log.d("ian", "success");
//            }
//
//            @Override
//            public void onCancelled(Callback.CancelledException cex) {
//
//            }
//
//            @Override
//            public void onFinished() {
//
//            }
//        };
//        MHttpClient.login(user, pwd, callback);
//    }
//
//    private void getAllStudentsInfo(String class_name) {
//        HttpUtils.HttpCallback<StudentInfo> callback = new HttpUtils.HttpCallback<StudentInfo>() {
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                Log.d("ian", "fail");
//            }
//
//            @Override
//            public void onSuccess(StudentInfo result) {
//                Log.d("ian", "success");
//            }
//
//            @Override
//            public void onSuccess(List<StudentInfo> result) {
//                Log.d("ian", "success" + result.size());
//            }
//
//            @Override
//            public void onCancelled(Callback.CancelledException cex) {
//
//            }
//
//            @Override
//            public void onFinished() {
//
//            }
//        };
//        MHttpClient.getAllStudentsInfo(class_name, callback);
//    }
//
//    private void getNotes(String job_number) {
//        HttpUtils.HttpCallback<Note> callback = new HttpUtils.HttpCallback<Note>() {
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                Log.d("ian", "fail");
//            }
//
//            @Override
//            public void onSuccess(Note result) {
//                Log.d("ian", "success");
//            }
//
//            @Override
//            public void onSuccess(List<Note> result) {
//                Log.d("ian", "success" + result.size());
//            }
//
//            @Override
//            public void onCancelled(Callback.CancelledException cex) {
//
//            }
//
//            @Override
//            public void onFinished() {
//
//            }
//        };
//        MHttpClient.getNotes(job_number, callback);
//    }
//
//    private void getGradeBySid(String sid) {
//        HttpUtils.HttpCallback<StudentGrade> callback = new HttpUtils.HttpCallback<StudentGrade>() {
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                Log.d("ian", "fail");
//            }
//
//            @Override
//            public void onSuccess(StudentGrade result) {
//                Log.d("ian", "success");
//            }
//
//            @Override
//            public void onSuccess(List<StudentGrade> result) {
//                Log.d("ian", "success" + result.size());
//            }
//
//            @Override
//            public void onCancelled(Callback.CancelledException cex) {
//
//            }
//
//            @Override
//            public void onFinished() {
//
//            }
//        };
//        MHttpClient.getGradeBySid(sid, callback);
//    }
//
//    private void getGradeByClass(String class_name, String type_id) {
//        HttpUtils.HttpCallback<StudentGrade> callback = new HttpUtils.HttpCallback<StudentGrade>() {
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                Log.d("ian", "fail");
//            }
//
//            @Override
//            public void onSuccess(StudentGrade result) {
//                Log.d("ian", "success");
//            }
//
//            @Override
//            public void onSuccess(List<StudentGrade> result) {
//                Log.d("ian", "success" + result.size());
//            }
//
//            @Override
//            public void onCancelled(Callback.CancelledException cex) {
//
//            }
//
//            @Override
//            public void onFinished() {
//
//            }
//        };
//        MHttpClient.getGradeByClass(class_name, type_id, callback);
//    }
//
//    private void getTeacherInfoByJob(String job_number) {
//        HttpUtils.HttpCallback<TeacherInfo> callback = new HttpUtils.HttpCallback<TeacherInfo>() {
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                Log.d("ian", "fail");
//            }
//
//            @Override
//            public void onSuccess(TeacherInfo result) {
//                Log.d("ian", "success");
//            }
//
//            @Override
//            public void onSuccess(List<TeacherInfo> result) {
//                Log.d("ian", "success" + result.size());
//            }
//
//            @Override
//            public void onCancelled(Callback.CancelledException cex) {
//
//            }
//
//            @Override
//            public void onFinished() {
//
//            }
//        };
//        MHttpClient.getTeacherInfoByJob(job_number, callback);
//    }
//
//    private void getTeacherInfo() {
//        HttpUtils.HttpCallback<TeacherInfo> callback = new HttpUtils.HttpCallback<TeacherInfo>() {
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                Log.d("ian", "fail");
//            }
//
//            @Override
//            public void onSuccess(TeacherInfo result) {
//                Log.d("ian", "success");
//            }
//
//            @Override
//            public void onSuccess(List<TeacherInfo> result) {
//                Log.d("ian", "success" + result.size());
//            }
//
//            @Override
//            public void onCancelled(Callback.CancelledException cex) {
//
//            }
//
//            @Override
//            public void onFinished() {
//
//            }
//        };
//        MHttpClient.getTeacherInfo(callback);
//    }
//
//    private void getExam() {
//        HttpUtils.HttpCallback<Exam> callback = new HttpUtils.HttpCallback<Exam>() {
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                Log.d("ian", "fail");
//            }
//
//            @Override
//            public void onSuccess(Exam result) {
//                Log.d("ian", "success");
//            }
//
//            @Override
//            public void onSuccess(List<Exam> result) {
//                Log.d("ian", "success" + result.size());
//            }
//
//            @Override
//            public void onCancelled(Callback.CancelledException cex) {
//
//            }
//
//            @Override
//            public void onFinished() {
//
//            }
//        };
//        MHttpClient.getExam(callback);
//    }
//    private void getScheduleByWeek(String week) {
//        HttpUtils.HttpCallback<TeacherSchedule> callback = new HttpUtils.HttpCallback<TeacherSchedule>() {
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                Log.d("ian", "fail");
//            }
//
//            @Override
//            public void onSuccess(TeacherSchedule result) {
//                Log.d("ian", "success");
//            }
//
//            @Override
//            public void onSuccess(List<TeacherSchedule> result) {
//                Log.d("ian", "success" + result.size());
//            }
//
//            @Override
//            public void onCancelled(Callback.CancelledException cex) {
//
//            }
//
//            @Override
//            public void onFinished() {
//
//            }
//        };
//        MHttpClient.getScheduleByWeek(week, callback);
//    }
//    private void getScheduleByClass(String class_name) {
//        HttpUtils.HttpCallback<TeacherSchedule> callback = new HttpUtils.HttpCallback<TeacherSchedule>() {
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                Log.d("ian", "fail");
//            }
//
//            @Override
//            public void onSuccess(TeacherSchedule result) {
//                Log.d("ian", "success");
//            }
//
//            @Override
//            public void onSuccess(List<TeacherSchedule> result) {
//                Log.d("ian", "success" + result.size());
//            }
//
//            @Override
//            public void onCancelled(Callback.CancelledException cex) {
//
//            }
//
//            @Override
//            public void onFinished() {
//
//            }
//        };
//        MHttpClient.getScheduleByClass(class_name, callback);
//    }
//    private void write(){
//        DbManager db = x.getDb(DBUtils.getnoteDaoConfig());
//        Note note = new Note();
//        note.setContent("a");
//        note.setCreate_time("1");
//        note.setJob_name("2");
//        try {
//            db.save(note);
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
//    }
//    private void read(){
//        DbManager db = x.getDb(DBUtils.getnoteDaoConfig());
//        try {
//            Note note= db.selector(Note.class).findFirst();
//            Log.d("ian", "list:" + note.getJob_name());
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
//    }

    private void checkPassword() {
        if (ConfigManager.getStringValue(MainActivity.this, ConfigManager.PASSWORD).equals("123456")) {
            final MaterialDialog materialDialog = new MaterialDialog(this);
            materialDialog.setTitle("温馨提示")
                    .setMessage("检测到您的密码为初始密码,请修改")
                    .setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, ChangePasswordActivity.class);
                            startActivity(intent);
                            materialDialog.dismiss();
                        }
                    });
            materialDialog.setNegativeButton("取消", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    materialDialog.dismiss();
                }
            });
            materialDialog.show();
        }
    }

    public EasyTeacherApplication getmApp() {
        return mApp;
    }

    private void showDialog(String info) {
        final MaterialDialog materialDialog = new MaterialDialog(this);
        materialDialog.setTitle("温馨提示")
                .setMessage(info)
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        materialDialog.dismiss();
                    }
                });
        materialDialog.show();
    }
}
