package com.ace.easyteacher.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ImageView;

import com.ace.easyteacher.Bean.ResultBean;
import com.ace.easyteacher.ConfigManager;
import com.ace.easyteacher.DataBase.Note;
import com.ace.easyteacher.Http.HttpUtils;
import com.ace.easyteacher.Http.MHttpClient;
import com.ace.easyteacher.R;
import com.ace.easyteacher.Utils.DateUtil;
import com.alibaba.fastjson.JSON;
import com.magicare.mutils.common.Callback;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class NewNoteFActivity extends AppCompatActivity {

    @Bind(R.id.cancle)
    ImageView mCancle;
    @Bind(R.id.ok)
    ImageView mOk;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private int mJob_Number;
    @Bind(R.id.et_addnote)
    EditText mEt_add;

    private int job_number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_note);
        ButterKnife.bind(this);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

    }


    public void setmJob_Number(int number) {
        job_number = number;
    }


    @OnClick(R.id.ok)
    public void ok() {
        String note = mEt_add.getText().toString();
        Note bean = new Note();
        bean.setContent(note);
        bean.setCreate_time(DateUtil.getCurrentTime());
        bean.setJob_number(ConfigManager.getStringValue(this, ConfigManager.JOB_NUMBER));
        addNote(JSON.toJSONString(bean));
        // TODO: 2016/3/25 上传日志
    }

    private void addNote(String str) {
        HttpUtils.HttpCallback<ResultBean> callback = new HttpUtils.HttpCallback<ResultBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onSuccess(ResultBean result) {
                finish();
            }

            @Override
            public void onSuccess(List<ResultBean> result) {
                finish();
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                finish();
            }
        };
        MHttpClient.addNote(str, callback);
    }

    @OnClick(R.id.cancle)
    public void cancle() {
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}

