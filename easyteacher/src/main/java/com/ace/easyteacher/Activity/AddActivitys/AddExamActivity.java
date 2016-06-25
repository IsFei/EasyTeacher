package com.ace.easyteacher.Activity.AddActivitys;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ace.easyteacher.DataBase.DBUtils;
import com.ace.easyteacher.DataBase.Exam;
import com.ace.easyteacher.Http.HttpUtils;
import com.ace.easyteacher.Http.MHttpClient;
import com.ace.easyteacher.R;
import com.ace.easyteacher.Utils.XToast;
import com.alibaba.fastjson.JSON;
import com.magicare.mutils.NetUtil;
import com.magicare.mutils.common.Callback;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddExamActivity extends AppCompatActivity {
    @Bind(R.id.ae_name)
    MaterialEditText name;
    @Bind(R.id.ae_time)
    MaterialEditText time;
    @Bind(R.id.ae_ok)
    Button ok;
    @Bind(R.id.ae_type)
    TextView type;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private Exam mExam = new Exam();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exam);
        ButterKnife.bind(this);
        toolbar.setTitle("添加考试");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getType();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetUtil.isNetWorkConnected(AddExamActivity.this)) {
                    XToast.showToast(AddExamActivity.this, "请检查网络连接");
                    return;
                }
                String name_string = name.getText().toString().trim();
                String time_string = time.getText().toString().trim();
                mExam.setTime(time_string);
                mExam.setName(name_string);
//                mExam.setType_id(Integer.parseInt(type.getText().toString()));
                addExam();
            }
        });
    }

    private void getType() {
        HttpUtils.HttpCallback<Exam> callback = new HttpUtils.HttpCallback<Exam>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                DbManager db = x.getDb(DBUtils.getStutdentInfoDaoConfig());
                try {
                    List<Exam> list = db.selector(Exam.class).findAll();
                    if (list != null && list.size() != 0) {
                        type.setText(list.get(list.size() - 1).getType_id());
                    }

                } catch (DbException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(Exam result) {

            }

            @Override
            public void onSuccess(List<Exam> result) {
                type.setText(result.get(result.size() - 1).getType_id() + 1 + "");
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        };
        MHttpClient.getExam(callback);
    }

    private void addExam() {
        HttpUtils.HttpCallback<String> callback = new HttpUtils.HttpCallback<String>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                XToast.showToast(AddExamActivity.this, "添加成功");
            }

            @Override
            public void onSuccess(String result) {
                XToast.showToast(AddExamActivity.this, "添加成功");
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
        MHttpClient.addExam(JSON.toJSONString(mExam), callback);
    }
}
