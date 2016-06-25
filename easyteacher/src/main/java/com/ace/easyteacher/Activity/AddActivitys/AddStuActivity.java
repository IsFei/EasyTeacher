package com.ace.easyteacher.Activity.AddActivitys;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.ace.easyteacher.DataBase.ClassInfo;
import com.ace.easyteacher.DataBase.DBUtils;
import com.ace.easyteacher.DataBase.StudentInfo;
import com.ace.easyteacher.Http.HttpUtils;
import com.ace.easyteacher.Http.MHttpClient;
import com.ace.easyteacher.R;
import com.ace.easyteacher.Upload.PutObjectSamples;
import com.ace.easyteacher.Utils.XToast;
import com.alibaba.fastjson.JSON;
import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.magicare.mutils.NetUtil;
import com.magicare.mutils.common.Callback;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class AddStuActivity extends AppCompatActivity {
    @Bind(R.id.name)
    MaterialEditText mName;
    @Bind(R.id.sid)
    MaterialEditText mSid;
    @Bind(R.id.sex)
    MaterialEditText mSex;
    @Bind(R.id.birth)
    MaterialEditText birth;
    @Bind(R.id.position)
    MaterialEditText position;
    @Bind(R.id.hobby)
    MaterialEditText hobby;
    @Bind(R.id.father_name)
    MaterialEditText fatherName;
    @Bind(R.id.father_number)
    MaterialEditText fatherNumber;
    @Bind(R.id.mother_name)
    MaterialEditText motherName;
    @Bind(R.id.mother_number)
    MaterialEditText motherNumber;
    @Bind(R.id.ok)
    Button ok;
    @Bind(R.id.sp_class_name)
    Spinner mSp;
    @Bind(R.id.tv_head)
    TextView mTv_head;
    @Bind(R.id.btn_stu_head)
    Button mBtn_head;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private StudentInfo mStudent = new StudentInfo();
    private String[] mClass_name;
    private ArrayAdapter<String> mAdapter;
    private Uri mUri;
    private static final int CLASS_DONE = 1;
    private final static int FILE_SELECT_CODE = 99;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CLASS_DONE:
                    mAdapter = new ArrayAdapter<String>(AddStuActivity.this, android.R.layout.simple_spinner_item, mClass_name);
                    mAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    mSp.setAdapter(mAdapter);
                    mSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            mStudent.setClass_name(mClass_name[position]);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    break;
                default:
                    break;
            }
        }
    };
    private OSS oss;
    private static final String endpoint = "oss-cn-shenzhen.aliyuncs.com";
    private static final String accessKeyId = "tMs31AL2Z2IHa5q4";
    private static final String accessKeySecret = "uz3ZW59qlkAGilPQvnp9yXmFmk1Gcu";
    private static final String testBucket = "com-ace-teacher";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        ButterKnife.bind(this);
        toolbar.setTitle("添加学生信息");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getAllClass();
        mBtn_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFile();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSid.getText().toString().trim().equals("")) {
                    mStudent.setSid(0);
                } else {
                    mStudent.setSid(Long.parseLong(mSid.getText().toString().trim()));
                }
                mStudent.setName(mName.getText().toString());
                mStudent.setSex(mSex.getText().toString());
                mStudent.setBirthday(birth.getText().toString());
                mStudent.setPosition(position.getText().toString());
                mStudent.setHobby(hobby.getText().toString());
                mStudent.setFather(fatherName.getText().toString());
                mStudent.setFather_tel(fatherNumber.getText().toString());
                mStudent.setMother(motherName.getText().toString());
                mStudent.setMother_tel(motherNumber.getText().toString());
                mStudent.setUrl("");
                if (mStudent.getSid() == 0) {
                    XToast.showToast(AddStuActivity.this, "学号不能为空");
                } else if (mStudent.getName().equals("")) {
                    XToast.showToast(AddStuActivity.this, "姓名不能为空");
                } else {
                    makeHead();
                    addStudent(JSON.toJSONString(mStudent));
                    if (mUri != null) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                new PutObjectSamples(oss, testBucket, mSid.getText().toString().trim(), getPath(AddStuActivity.this, mUri)).asyncPutObjectFromLocalFile();
                            }
                        }).start();
                    }
                }
            }
        });
    }

    private void addStudent(String content) {
        if (!NetUtil.isNetWorkConnected(AddStuActivity.this)) {
            XToast.showToast(AddStuActivity.this, "请检查网络连接");
            return;
        }
        HttpUtils.HttpCallback<String> callback = new HttpUtils.HttpCallback<String>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                XToast.showToast(AddStuActivity.this, "上传失败");
            }

            @Override
            public void onSuccess(String result) {
                XToast.showToast(AddStuActivity.this, "上传成功");
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
        MHttpClient.addStudent(content, callback);
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

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    private void openFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "请选择一个要上传的文件"),
                    FILE_SELECT_CODE);
        } catch (ActivityNotFoundException ex) {
            XToast.showToast(this, "请安装文件管理器");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            mUri = data.getData();
            mTv_head.setText(getPath(this, mUri));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void makeHead() {
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(accessKeyId, accessKeySecret);
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        OSSLog.enableLog();
        oss = new OSSClient(getApplicationContext(), endpoint, credentialProvider, conf);
    }

    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }


    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }


    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
