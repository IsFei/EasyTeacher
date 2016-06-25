package com.ace.easyteacher.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ace.easyteacher.DataBase.TeacherInfo;
import com.ace.easyteacher.R;
import com.ace.easyteacher.Utils.MessageInfoUtils;
import com.ace.easyteacher.Utils.ToastUtils;

import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lenovo on 2016-4-8.
 */
public class SendMessageActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.text_message_content)
    EditText mMessageContent;
    @Bind(R.id.text_name)
    TextView mName;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    private TeacherInfo mTeacherInfo;
    private List<MessageInfoUtils> mListPeople;
    private TextWatcher watcher = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        ButterKnife.bind(this);
        toolbar.setTitle("编辑与发送短信");
        toolbar.setBackgroundColor(MainActivity.BACKGROUND_COLOR);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        fab.setScaleX(0f);
        fab.setScaleY(0f);
        fab.setVisibility(View.VISIBLE);

//        final Intent mIntent = new Intent();
//        //传回去结果  1为发送成功  2为没发送
//        mIntent.putExtra("result", "2");
        initData();
        watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fab.animate().scaleX(1).scaleY(1).setDuration(500).start();
                mMessageContent.removeTextChangedListener(watcher);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        mMessageContent.addTextChangedListener(watcher);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //短信内容
                String content = mMessageContent.getText().toString();

                if (content.length() == 0) {
                    ToastUtils.Toast(SendMessageActivity.this, "请输入短信内容", 500);
                } else {
                    //获取短信管理器
                    SmsManager smsManager = SmsManager.getDefault();
                    //如果汉字大于70个
                    if (content.length() > 70) {
                        //返回多条短信
                        List<String> contents = smsManager.divideMessage(content);
                        for (String sms : contents) {
                            //1.目标地址：电话号码 2.原地址：短信中心服号码3.短信内容4.意图
                            //数据的电话号码是编的，这里固定发到我手机号里，原代码是下面被注释的代码
                            smsManager.sendTextMessage("18721782151", null, sms, null, null);
//                            for (int i=0;i<mListPeople.size();i++){
//                                smsManager.sendTextMessage(mListPeople.get(i).getNum(), null, sms, null, null);
//                            }
                        }
                    } else {
                        smsManager.sendTextMessage("18721782151", null, content, null, null);
//                        for (int i=0;i<mListPeople.size();i++){
//                            smsManager.sendTextMessage(mListPeople.get(i).getNum(), null, content, null, null);
//                        }
                    }
                    ToastUtils.Toast(SendMessageActivity.this, "发送成功，观看结果请去手机系统自带的“短信”", 500);
//                    mIntent.putExtra("result", "1");
//                    // 设置结果，并进行传送
//                    SendMessageActivity.this.setResult(0, mIntent);
                    SendMessageActivity.this.finish();
                }
            }
        });

    }


    private void initData() {
        mListPeople = (List<MessageInfoUtils>) getIntent().getSerializableExtra("list");
        switch (mListPeople.size()) {
            case 1:
                mName.setText("收信人：" + mListPeople.get(0).getName());
                break;
            case 2:
                mName.setText("收信人：" + mListPeople.get(0).getName() + "、" + mListPeople.get(1).getName());
                break;
            default:
                mName.setText("收信人：" + mListPeople.get(0).getName() + "、" + mListPeople.get(1).getName() + "  等" + mListPeople.size() + "人");
                break;
        }
    }
}
