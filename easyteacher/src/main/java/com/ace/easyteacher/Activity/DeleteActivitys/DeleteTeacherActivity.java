package com.ace.easyteacher.Activity.DeleteActivitys;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.ace.easyteacher.Adapter.DeleteTeacherAdapter;
import com.ace.easyteacher.Bean.ResultBean;
import com.ace.easyteacher.DataBase.DBUtils;
import com.ace.easyteacher.DataBase.TeacherInfo;
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

public class DeleteTeacherActivity extends AppCompatActivity {
    @Bind(R.id.dt_by_job_number)
    Button mBtn_job_number;
    @Bind(R.id.dt_by_list)
    Button mBtn_list;
    @Bind(R.id.dt_tv_job_number)
    MaterialEditText mEdit_job_number;
    @Bind(R.id.dt_rv)
    RecyclerView mRecyclerView;
    @Bind(R.id.dt_confirm)
    Button mBtn_confirm;
    private DeleteTeacherAdapter mAdapter;
    private final static int TEACHER_DONE = 1;
    private final List<TeacherInfo> mList = new ArrayList<>();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TEACHER_DONE:
                    if (mAdapter == null) {
                        mAdapter = new DeleteTeacherAdapter(DeleteTeacherActivity.this, mList);
                        mAdapter.setOnItemClickListener(new DeleteTeacherAdapter.OnItemClickListener() {
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
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_teacher);
        ButterKnife.bind(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        getTeacher();
        mBtn_job_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEdit_job_number.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
                mBtn_confirm.setVisibility(View.VISIBLE);
            }
        });
        mBtn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEdit_job_number.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                mBtn_confirm.setVisibility(View.GONE);
            }
        });
        mBtn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mEdit_job_number.getText().toString().trim().equals("")) {
                    deleteTeacher(mEdit_job_number.getText().toString().trim());
                } else {
                    XToast.showToast(DeleteTeacherActivity.this, "工号不能为空");
                }
            }
        });

    }

    private void getTeacher() {
        HttpUtils.HttpCallback<TeacherInfo> callback = new HttpUtils.HttpCallback<TeacherInfo>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                DbManager db = x.getDb(DBUtils.getStutdentInfoDaoConfig());
                try {
                    List<TeacherInfo> result = db.selector(TeacherInfo.class).findAll();
                    if (result != null) {
                        result.remove(0);
                        mList.clear();
                        mList.addAll(result);
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
                if (result != null) {
                    result.remove(0);
                    mList.clear();
                    mList.addAll(result);
                }
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                mHandler.sendEmptyMessage(TEACHER_DONE);
            }
        };
        MHttpClient.getTeacherInfo(callback);
    }

    private void showConfirmDialog(final int position) {
        final MaterialDialog dialog = new MaterialDialog(this);
        dialog.setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTeacher(mList.get(position).getJob_number());
            }
        });
        dialog.setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setTitle("确认删除");
        dialog.setMessage("确认删除姓名为:" + mList.get(position).getName() + ",工号为:" + mList.get(position).getJob_number() + "的教师");
        dialog.show();
    }

    private void deleteTeacher(String job_number) {
        if (!NetUtil.isNetWorkConnected(DeleteTeacherActivity.this)) {
            XToast.showToast(DeleteTeacherActivity.this, "请检查网络连接");
            return;
        }
        HttpUtils.HttpCallback<ResultBean> callback = new HttpUtils.HttpCallback<ResultBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                String result = ex.getMessage();
                try {
                    JSONObject obj = new JSONObject(result);
                    String info = (String) obj.get("info");
                    XToast.showToast(DeleteTeacherActivity.this, info);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(ResultBean result) {
                XToast.showToast(DeleteTeacherActivity.this, "删除成功");
                getTeacher();
            }

            @Override
            public void onSuccess(List<ResultBean> result) {

            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        };
        MHttpClient.deleteTeacher(job_number, callback);

    }
}
