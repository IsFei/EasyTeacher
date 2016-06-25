package com.ace.easyteacher.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ace.easyteacher.Activity.CheckActivity;
import com.ace.easyteacher.Activity.NewCheckActivity;
import com.ace.easyteacher.Activity.StudentInfoActivity;
import com.ace.easyteacher.DataBase.Check;
import com.ace.easyteacher.DataBase.DBUtils;
import com.ace.easyteacher.DataBase.StudentInfo;
import com.ace.easyteacher.Http.HttpUtils;
import com.ace.easyteacher.Http.MHttpClient;
import com.ace.easyteacher.R;
import com.ace.easyteacher.Utils.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.magicare.mutils.common.Callback;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by lenovo on 2016-3-23.
 */
public class StudentInfoFragment extends Fragment {

    @Bind(R.id.info_name)
    TextView mStudentName;
    @Bind(R.id.info_number)
    TextView mStudentId;
    @Bind(R.id.info_sex)
    TextView mStudentSex;
    @Bind(R.id.info_birth)
    TextView mStudentBirth;
    @Bind(R.id.info_position)
    TextView mStudentPosition;
    @Bind(R.id.info_hobby)
    TextView mStudentHobby;
    @Bind(R.id.info_father_name)
    TextView mFatherName;
    @Bind(R.id.info_father_number)
    TextView mFatherNumber;
    @Bind(R.id.info_mother_name)
    TextView mMotherName;
    @Bind(R.id.info_mother_number)
    TextView mMotherNumber;
    @Bind(R.id.info_check)
    TextView mCheck;
    @Bind(R.id.student_head)
    SimpleDraweeView mStudentHead;
    @Bind(R.id.relay_check)
    RelativeLayout mRelativeLayoutCheck;

    private StudentInfo mStudent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_info, container, false);
        ButterKnife.bind(this, view);
        mStudent = ((StudentInfoActivity) getActivity()).getStudent();
        initView();
        mFatherNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ringUp(mFatherNumber.getText().toString());
            }
        });
        mMotherNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ringUp(mMotherNumber.getText().toString());
            }
        });
        return view;
    }

    private void initView() {
        Uri uri = Uri.parse("http://com-ace-teacher.oss-cn-shenzhen.aliyuncs.com/" + mStudent.getSid() + ".jpg");
        mStudentHead.setImageURI(uri);
        mFatherName.setText(mStudent.getFather());
        mStudentBirth.setText(mStudent.getBirthday());
        mStudentHobby.setText(mStudent.getHobby());
        mStudentId.setText(mStudent.getSid() + "");
        mStudentSex.setText(mStudent.getSex());
        if (mStudent.getPosition().length() != 0) {
            mStudentPosition.setText(mStudent.getPosition());
        } else mStudentPosition.setText("普通学生");
        mFatherNumber.setText(mStudent.getFather_tel());
        mMotherName.setText(mStudent.getMother());
        mMotherNumber.setText(mStudent.getMother_tel());
        mStudentName.setText(mStudent.getName());
        getCheckBySid(mStudent.getSid() + "");
//        if (mStudent.get)

    }

    private void ringUp(final String number) {
        final MaterialDialog mMaterialDialog = new MaterialDialog(getActivity());
        mMaterialDialog.setTitle("拨打电话")
                .setMessage(number)
                .setPositiveButton("拨打", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + number));
                        startActivity(intent);
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                    }
                });
        mMaterialDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void getCheckBySid(String sid) {
        HttpUtils.HttpCallback<Check> callback = new HttpUtils.HttpCallback<Check>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                DbManager db = x.getDb(DBUtils.getStutdentInfoDaoConfig());
                try {
                    List<Check> result = db.findAll(Check.class);
                    check(result);
                } catch (DbException e) {
                    ToastUtils.Toast(getActivity(), "获取考勤记录失败，请检查网络或联系管理员", 500);
                    e.printStackTrace();
                }

            }

            @Override
            public void onSuccess(Check result) {

            }

            @Override
            public void onSuccess(List<Check> result) {
                DbManager db = x.getDb(DBUtils.getStutdentInfoDaoConfig());
                try {
                    db.delete(Check.class);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                for (Check studentInfo : result) {
                    try {
                        db.save(studentInfo);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }

                check(result);
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        };
        MHttpClient.getCheckBySid(sid, callback);
    }
    private void check(List<Check> result) {

        if(result == null){
            return;
        }
        if (result.size() != 0) {
            mCheck.setText("点击查看");
            mRelativeLayoutCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), CheckActivity.class);
                    intent.putExtra("sid", mStudent.getSid() + "");
                    startActivity(intent);
                }
            });
        } else {
            mCheck.setText("全勤");
            mRelativeLayoutCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final MaterialDialog mMaterialDialog = new MaterialDialog(getActivity());
                    mMaterialDialog.setTitle("提示")
                            .setMessage("该学生无考勤记录，是否添加？")
                            .setPositiveButton("是", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mMaterialDialog.dismiss();
                                    Intent intent = new Intent(getActivity(), NewCheckActivity.class);
                                    intent.putExtra("sid", mStudent.getSid() + "");
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("不", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mMaterialDialog.dismiss();
                                }
                            });
                    mMaterialDialog.show();
                }
            });
        }
    }

}
