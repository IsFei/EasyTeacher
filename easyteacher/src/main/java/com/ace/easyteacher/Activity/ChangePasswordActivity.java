package com.ace.easyteacher.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ace.easyteacher.Bean.ResultBean;
import com.ace.easyteacher.ConfigManager;
import com.ace.easyteacher.Http.HttpUtils;
import com.ace.easyteacher.Http.MHttpClient;
import com.ace.easyteacher.R;
import com.ace.easyteacher.Utils.XToast;
import com.magicare.mutils.NetUtil;
import com.magicare.mutils.common.Callback;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChangePasswordActivity extends AppCompatActivity {
    @Bind(R.id.new_pwd_2)
    MaterialEditText mPwd2;
    private Button mBtn_change;
    private MaterialEditText mEdit_new_pwd, mEdit_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        mBtn_change = (Button) findViewById(R.id.change_pwd);
        mEdit_new_pwd = (MaterialEditText) findViewById(R.id.new_pwd);
        mEdit_user = (MaterialEditText) findViewById(R.id.job_number);
        mBtn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = mEdit_user.getText().toString().trim();
                String new_pwd = mEdit_new_pwd.getText().toString().trim();
                String new_pwd2 = mPwd2.getText().toString().trim();
                if (new_pwd.equals(new_pwd2)) {
                    if (NetUtil.isNetWorkConnected(getApplicationContext())) {
                        changePwd(user, new_pwd);
                        XToast.showProgressDialog(ChangePasswordActivity.this, "请等待");
                    } else {
                        showToast("请检查网络");
                    }
                } else {
                    mEdit_new_pwd.setText("");
                    mPwd2.setText("");
                    showToast("两次密码不同，请重新输入");
                }
            }
        });
        mEdit_user.setText(ConfigManager.getStringValue(this, ConfigManager.JOB_NUMBER));
    }

    private void changePwd(String user, String new_pwd) {
        HttpUtils.HttpCallback<ResultBean> callback = new HttpUtils.HttpCallback<ResultBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                XToast.closeProgressDialog();
                showToast("修改失败,请重试");
            }

            @Override
            public void onSuccess(ResultBean result) {
                XToast.closeProgressDialog();
                ConfigManager.setStringValue(ChangePasswordActivity.this, ConfigManager.PASSWORD, mEdit_new_pwd.getText().toString().trim());
                showToast("修改成功");
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
        MHttpClient.alterPassword(user, new_pwd, callback);
    }

    private void showToast(String info) {
        Toast.makeText(getApplicationContext(), info, Toast.LENGTH_SHORT).show();
    }
}
