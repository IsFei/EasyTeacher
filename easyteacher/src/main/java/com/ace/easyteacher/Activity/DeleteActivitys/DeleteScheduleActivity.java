package com.ace.easyteacher.Activity.DeleteActivitys;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.ace.easyteacher.Bean.ResultBean;
import com.ace.easyteacher.DataBase.ClassInfo;
import com.ace.easyteacher.DataBase.DBUtils;
import com.ace.easyteacher.Http.HttpUtils;
import com.ace.easyteacher.Http.MHttpClient;
import com.ace.easyteacher.R;
import com.ace.easyteacher.Utils.XToast;
import com.magicare.mutils.NetUtil;
import com.magicare.mutils.common.Callback;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DeleteScheduleActivity extends AppCompatActivity {
    @Bind(R.id.dsche_confirm)
    Button mBtn_confirm;
    @Bind(R.id.dsche_sp)
    Spinner mSp;
    private String[] mClass_name;
    private String mClass;
    private final static int CLASS_DONE = 1;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CLASS_DONE:
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(DeleteScheduleActivity.this, android.R.layout.simple_spinner_item, mClass_name);
                    adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    mSp.setAdapter(adapter);
                    mSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            mClass = mClass_name[position];
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_schedule);
        ButterKnife.bind(this);
        getAllClass();
        mBtn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteStu(mClass);
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
                mHandler.sendEmptyMessage(CLASS_DONE);
            }
        };
        MHttpClient.getClassInfo(callback);
    }

    private void deleteStu(String sid) {
        if (!NetUtil.isNetWorkConnected(DeleteScheduleActivity.this)) {
            XToast.showToast(DeleteScheduleActivity.this, "请检查网络连接");
            return;
        }
        HttpUtils.HttpCallback<ResultBean> callback = new HttpUtils.HttpCallback<ResultBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                XToast.showToast(DeleteScheduleActivity.this, "删除失败");
            }

            @Override
            public void onSuccess(ResultBean result) {
                XToast.showToast(DeleteScheduleActivity.this, "删除成功");
                finish();
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
        MHttpClient.deleteSchedule(sid, callback);
    }

}
