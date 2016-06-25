package com.ace.easyteacher.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ace.easyteacher.Activity.ChangePasswordActivity;
import com.ace.easyteacher.Activity.ClassCheckActivity;
import com.ace.easyteacher.Activity.MainActivity;
import com.ace.easyteacher.Activity.TeacherScheduleActivity;
import com.ace.easyteacher.ConfigManager;
import com.ace.easyteacher.DataBase.DBUtils;
import com.ace.easyteacher.DataBase.TeacherInfo;
import com.ace.easyteacher.Http.HttpUtils;
import com.ace.easyteacher.Http.MHttpClient;
import com.ace.easyteacher.R;
import com.ace.easyteacher.Utils.ToastUtils;
import com.ace.easyteacher.View.MaterialRippleLayout;
import com.facebook.drawee.view.SimpleDraweeView;
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
public class MineFragment extends Fragment {

    @Bind(R.id.student_head)
    SimpleDraweeView mHead;
    @Bind(R.id.teacher_name)
    TextView mTeacherName;
    @Bind(R.id.teacher_number)
    TextView mTeacherNumber;
    @Bind(R.id.teacher_tel)
    TextView mTeacherTel;
    @Bind(R.id.teacher_address)
    TextView mTeacherAddress;
    @Bind(R.id.kechengbiao)
    MaterialRippleLayout mSchedule;
    @Bind(R.id.check)
    MaterialRippleLayout mCheck;
    @Bind(R.id.change_password)
    MaterialRippleLayout mPassword;
    @Bind(R.id.class_name)
    TextView mClassName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mine_fragment, container, false);
        ButterKnife.bind(this, view);
        getTeacherInfoByJob(ConfigManager.getStringValue(getActivity(), ConfigManager.JOB_NUMBER));
        mSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TeacherScheduleActivity.class);
                startActivity(intent);
            }
        });
        mCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ClassCheckActivity.class);
                startActivity(intent);
            }
        });
        mPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void getTeacherInfoByJob(String job_number) {
        HttpUtils.HttpCallback<TeacherInfo> callback = new HttpUtils.HttpCallback<TeacherInfo>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d("ian", "fail");
                DbManager db = x.getDb(DBUtils.getStutdentInfoDaoConfig());
                try {
                    List<TeacherInfo> result = db.findAll(TeacherInfo.class);
                    initView(result.get(0));
                } catch (DbException e) {
                    ToastUtils.Toast(getActivity(), "获取个人信息失败，请检查网络或联系管理员", 500);
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(TeacherInfo result) {
                Log.d("ian", "success");
                initView(result);
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
                Log.d("ian", "success" + result.size());
                initView(result.get(0));
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

    private TeacherInfo info;

    private synchronized void initView(TeacherInfo teacherInfo) {
        info = teacherInfo;
        Uri uri = Uri.parse("http://com-ace-teacher.oss-cn-shenzhen.aliyuncs.com/" + teacherInfo.getJob_number() + ".jpg");
        mHead.setImageURI(uri);
        String class_name = ((MainActivity) getActivity()).getmApp().getWhichClass();
        class_name = class_name == null ? "高二19班" : class_name;
        mClassName.setText(class_name);
        mTeacherAddress.setText(teacherInfo.getAdr());
        mTeacherName.setText(teacherInfo.getName());
        mTeacherNumber.setText(teacherInfo.getJob_number());
        mTeacherTel.setText(teacherInfo.getTel());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (isVisible() && info != null) {
            initView(info);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void refresh() {
        if (isVisible() && info != null) {
            initView(info);
        }
    }
}
