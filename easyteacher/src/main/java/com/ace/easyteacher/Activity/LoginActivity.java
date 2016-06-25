package com.ace.easyteacher.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ace.easyteacher.ConfigManager;
import com.ace.easyteacher.Http.HttpUtils;
import com.ace.easyteacher.Http.MHttpClient;
import com.ace.easyteacher.R;
import com.ace.easyteacher.Utils.XToast;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.magicare.mutils.NetUtil;
import com.magicare.mutils.common.Callback;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.List;

public class LoginActivity extends Activity {
    private MaterialEditText mEdit_user, mEdit_pwd;
    private Button mBtn_login;
    private String strs[] = new String[]{"123456","111111","222222","333333"};
    private String mKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        mEdit_pwd = (MaterialEditText) findViewById(R.id.pwd);
        mEdit_user = (MaterialEditText) findViewById(R.id.user);
        mBtn_login = (Button) findViewById(R.id.login);
        mBtn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = mEdit_user.getText().toString().trim();
                mKey = user;
                ConfigManager.setStringValue(LoginActivity.this,"saveuser",mKey);
                String pwd = mEdit_pwd.getText().toString().trim();
                if(TextUtils.isEmpty(pwd)){
                    showToast("请输入密码");
                    return;
                }
                if (NetUtil.isNetWorkConnected(getApplicationContext())) {
                    login(user, pwd);
                    XToast.showProgressDialog(LoginActivity.this, "请等待");
                } else {
                    showToast("请检查网络");
                }
            }
        });
        mEdit_user.setText(ConfigManager.getStringValue(LoginActivity.this,"saveuser"));
//        mEdit_pwd.setText("1234567");
    }

    private void login(String user, String pwd) {
        HttpUtils.HttpCallback<String> callback = new HttpUtils.HttpCallback<String>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                XToast.closeProgressDialog();
//                showToast(ex.getMessage());
                try {
                    JSONObject obj = JSON.parseObject(ex.getMessage());
                    if(obj != null){
                        String info = (String) obj.get("info");
                        showToast(info);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onSuccess(String result) {
                for(String str :strs){
                    if(mKey.equals(str)){
                        Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                        startActivity(intent);
                        LoginActivity.this.finish();
                        return;
                    }
                }
                Log.d("ian", "success");
                XToast.closeProgressDialog();
                ConfigManager.setStringValue(LoginActivity.this, ConfigManager.PASSWORD, mEdit_pwd.getText().toString().trim());
                ConfigManager.setStringValue(LoginActivity.this, ConfigManager.JOB_NUMBER, mEdit_user.getText().toString().trim());
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
            }

            @Override
            public void onSuccess(List<String> result) {
                Log.d("ian", "successList");
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        };
        MHttpClient.login(user, pwd, callback);
    }

    private void showToast(String info) {
        Toast.makeText(getApplicationContext(), info, Toast.LENGTH_SHORT).show();
    }
}
