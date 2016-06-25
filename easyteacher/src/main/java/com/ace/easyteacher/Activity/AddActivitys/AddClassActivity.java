package com.ace.easyteacher.Activity.AddActivitys;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.ace.easyteacher.DataBase.ClassInfo;
import com.ace.easyteacher.Http.HttpUtils;
import com.ace.easyteacher.Http.MHttpClient;
import com.ace.easyteacher.R;
import com.ace.easyteacher.Utils.XToast;
import com.alibaba.fastjson.JSON;
import com.magicare.mutils.NetUtil;
import com.magicare.mutils.common.Callback;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddClassActivity extends AppCompatActivity {
    @Bind(R.id.ac_class_name)
    MaterialEditText class_name;
    @Bind(R.id.ac_room_name)
    MaterialEditText room_name;
    @Bind(R.id.ac_ok)
    Button ok;
    @Bind(R.id.ac_sp)
    Spinner mSp;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private ClassInfo mClass = new ClassInfo();
    private String[] mSubjectList = {"语文", "数学", "外语", "物理", "化学", "生物", "地理", "历史", "体育", "政治"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);
        ButterKnife.bind(this);
        toolbar.setTitle("添加班级");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        ArrayAdapter<String> subjectAdapter = new ArrayAdapter<String>(AddClassActivity.this, android.R.layout.simple_spinner_item, mSubjectList);
        subjectAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mSp.setAdapter(subjectAdapter);
        mSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mClass.setSubject(mSubjectList[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetUtil.isNetWorkConnected(AddClassActivity.this)) {
                    XToast.showToast(AddClassActivity.this, "请检查网络连接");
                    return;
                }
                String class_name_string = class_name.getText().toString().trim();
                mClass.setClass_name(class_name_string);
                String room_name_string = room_name.getText().toString().trim();
                mClass.setRoom_name(room_name_string);
                addClass();
            }
        });
    }

    private void addClass() {
        HttpUtils.HttpCallback<String> callback = new HttpUtils.HttpCallback<String>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                XToast.showToast(AddClassActivity.this, "添加失败");
            }

            @Override
            public void onSuccess(String result) {
                XToast.showToast(AddClassActivity.this, "上传成功");
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
        MHttpClient.addClass(JSON.toJSONString(mClass), callback);
    }
}
