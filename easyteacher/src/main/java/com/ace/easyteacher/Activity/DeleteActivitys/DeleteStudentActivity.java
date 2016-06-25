package com.ace.easyteacher.Activity.DeleteActivitys;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.ace.easyteacher.Adapter.DeleteStudentAdapter;
import com.ace.easyteacher.DataBase.ClassInfo;
import com.ace.easyteacher.DataBase.DBUtils;
import com.ace.easyteacher.DataBase.StudentInfo;
import com.ace.easyteacher.Http.HttpUtils;
import com.ace.easyteacher.Http.MHttpClient;
import com.ace.easyteacher.R;
import com.ace.easyteacher.Utils.XToast;
import com.magicare.mutils.NetUtil;
import com.magicare.mutils.common.Callback;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.drakeet.materialdialog.MaterialDialog;

public class DeleteStudentActivity extends AppCompatActivity {
    @Bind(R.id.ds_by_job_number)
    Button mBtn_job_number;
    @Bind(R.id.ds_by_list)
    Button mBtn_list;
    @Bind(R.id.ds_tv_job_number)
    MaterialEditText mEdit_job_number;
    @Bind(R.id.ds_rv)
    RecyclerView mRecyclerView;
    @Bind(R.id.ds_confirm)
    Button mBtn_confirm;
    @Bind(R.id.ds_confirm_class)
    Button mBtn_confirm_class;
    @Bind(R.id.ds_sp)
    Spinner mSP;
    private DeleteStudentAdapter mAdapter;
    private String mClass_name;
    private String[] mClassArray;
    private final static int STU_DONE = 1;
    private final static int CLASS_DONE = 2;
    private final List<StudentInfo> mList = new ArrayList<>();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case STU_DONE:
                    if (mAdapter == null) {
                        mAdapter = new DeleteStudentAdapter(DeleteStudentActivity.this, mList);
                        mAdapter.setOnItemClickListener(new DeleteStudentAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                showConfirmDialog(position);
                            }
                        });
                        mRecyclerView.setAdapter(mAdapter);
                    } else {
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
                case CLASS_DONE:
                    ArrayAdapter<String> classAdapter = new ArrayAdapter<String>(DeleteStudentActivity.this, android.R.layout.simple_spinner_item, mClassArray);
                    classAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    mSP.setAdapter(classAdapter);
                    mSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            mClass_name = mClassArray[position];
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_student);
        ButterKnife.bind(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBtn_job_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEdit_job_number.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
                mBtn_confirm.setVisibility(View.VISIBLE);
                mBtn_confirm_class.setVisibility(View.GONE);
                mSP.setVisibility(View.GONE);
            }
        });
        mBtn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEdit_job_number.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                mBtn_confirm.setVisibility(View.GONE);
                mBtn_confirm_class.setVisibility(View.VISIBLE);
                mSP.setVisibility(View.VISIBLE);
                getClassInfo();
            }
        });
        mBtn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mEdit_job_number.getText().toString().trim().equals("")) {
                    deleteStu(mEdit_job_number.getText().toString().trim());
                } else {
                    XToast.showToast(DeleteStudentActivity.this, "学号不能为空");
                }
            }
        });
        mBtn_confirm_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStu();
            }
        });

    }

    private void showConfirmDialog(final int position) {
        final MaterialDialog dialog = new MaterialDialog(this);
        dialog.setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteStu(mList.get(position).getSid() + "");
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setTitle("确认删除");
        dialog.setMessage("确认删除姓名为:" + mList.get(position).getName() + ",学号为:" + mList.get(position).getSid() + ",班级:" + mList.get(position).getClass_name() + "的学生");
        dialog.show();
    }

    private void deleteStu(String sid) {
        if (!NetUtil.isNetWorkConnected(DeleteStudentActivity.this)) {
            XToast.showToast(DeleteStudentActivity.this, "请检查网络连接");
            return;
        }
        HttpUtils.HttpCallback<String> callback = new HttpUtils.HttpCallback<String>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                XToast.showToast(DeleteStudentActivity.this, "删除失败");
            }

            @Override
            public void onSuccess(String result) {
                XToast.showToast(DeleteStudentActivity.this, "删除成功");
                getStu();
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

    private void getStu() {
        HttpUtils.HttpCallback<StudentInfo> callback = new HttpUtils.HttpCallback<StudentInfo>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                String result = ex.getMessage();
                try {
                    JSONObject obj = new JSONObject(result);
                    String info = (String) obj.get("info");
                    XToast.showToast(DeleteStudentActivity.this, info);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(StudentInfo result) {

            }

            @Override
            public void onSuccess(List<StudentInfo> result) {
                mList.clear();
                mList.addAll(result);
                mHandler.sendEmptyMessage(STU_DONE);
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        };
        MHttpClient.getAllStudentsInfo(mClass_name, callback);
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
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                mHandler.sendEmptyMessage(CLASS_DONE);
            }
        };
        MHttpClient.getClassInfo(callback);
    }
}
