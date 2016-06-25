package com.ace.easyteacher.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ImageView;

import com.ace.easyteacher.Bean.ResultBean;
import com.ace.easyteacher.ConfigManager;
import com.ace.easyteacher.DataBase.Check;
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

/**
 * Created by lenovo on 2016-3-25.
 */
public class NewCheckActivity extends AppCompatActivity {
    private String mSid;
    @Bind(R.id.cancle)
    ImageView mCancle;
    @Bind(R.id.ok)
    ImageView mOk;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private int mJob_Number;
    @Bind(R.id.et_addnote)
    EditText mEt_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_note);
        getSid();
        ButterKnife.bind(this);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

    }

    @OnClick(R.id.ok)
    public void ok() {
        String note = mEt_add.getText().toString();
        Check bean = new Check();
        bean.setContent(note);
        bean.setTime(DateUtil.getCurrentTime());
        bean.setSid(Long.parseLong(mSid));
        addCheck(JSON.toJSONString(bean));
        // TODO: 2016/3/25 上传日志
    }
    private void getSid(){
        mSid = getIntent().getStringExtra("sid");
    }

    private void addCheck(String content) {
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
        MHttpClient.addCheck(content, callback);
    }


    @OnClick(R.id.cancle)
    public void cancle() {
        finish();
    }

}
