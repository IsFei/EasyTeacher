package com.ace.easyteacher.Activity.AddActivitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ace.easyteacher.Adapter.ChooseAdapter;
import com.ace.easyteacher.Bean.ResultBean;
import com.ace.easyteacher.DataBase.ClassInfo;
import com.ace.easyteacher.DataBase.DBUtils;
import com.ace.easyteacher.DataBase.TeacherInfo;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AddTeacherActivity extends AppCompatActivity {

    @Bind(R.id.bt_save)
    Button mBtn_save;
    @Bind(R.id.et_name)
    MaterialEditText mEdit_name;

    @Bind(R.id.et_tel)
    MaterialEditText mEdit_tel;

    @Bind(R.id.et_job)
    MaterialEditText mEdit_job;

    @Bind(R.id.et_adr)
    MaterialEditText mEdit_adr;

    @Bind(R.id.et_password)
    MaterialEditText mEdit_password;
    @Bind(R.id.btn_choose_class)
    Button mBtn_class;
    @Bind(R.id.btn_teacher_head)
    Button mBtn_choose;
    @Bind(R.id.tv_teacher_head)
    TextView mTv_uri;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private TeacherInfo mTeacher = new TeacherInfo();
    private final List<ClassInfo> mList = new ArrayList<>();
    private OSS oss;
    private static final String endpoint = "oss-cn-shenzhen.aliyuncs.com";
    private static final String accessKeyId = "tMs31AL2Z2IHa5q4";
    private static final String accessKeySecret = "uz3ZW59qlkAGilPQvnp9yXmFmk1Gcu";
    private static final String testBucket = "com-ace-teacher";
    private final static int FILE_SELECT_CODE = 99;
    private String mChoose = null;
    private ChooseAdapter mAdapter;
    private Uri mUri;
    private static final int CLASS_DONE = 1;
    private static final int INPUT_DONE = 2;
    private RecyclerView mRecyclerView;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CLASS_DONE:
                    mAdapter = new ChooseAdapter(AddTeacherActivity.this, mList);
                    mRecyclerView.setAdapter(mAdapter);
                    break;
                case INPUT_DONE:
                    if (mTeacher.getClass_name() == null) {
                        XToast.showToast(AddTeacherActivity.this, "请选择班级");
                    } else if (mTeacher.getJob_number().equals("")) {
                        XToast.showToast(AddTeacherActivity.this, "工号不能为空");
                    } else if (mTeacher.getName().equals("")) {
                        XToast.showToast(AddTeacherActivity.this, "姓名不能为空");
                    } else {
                        addTeacher(JSON.toJSONString(mTeacher));
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
        setContentView(R.layout.add_teacher);
        ButterKnife.bind(this);
        toolbar.setTitle("添加教师信息");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        makeHead();
        mBtn_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChooseDialog();
            }
        });
        mBtn_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFile();
            }
        });
        mBtn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
                if (mUri.toString() != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            new PutObjectSamples(oss, testBucket, mEdit_job.getText().toString().trim(), getPath(AddTeacherActivity.this, mUri)).asyncPutObjectFromLocalFile();
                        }
                    }).start();
                }
            }
        });

    }

    @OnClick(R.id.bt_save)
    public void save() {
        mTeacher.setName(mEdit_name.getText().toString().trim());
        mTeacher.setTel(mEdit_tel.getText().toString().trim());
        mTeacher.setJob_number(mEdit_job.getText().toString().trim());
        mTeacher.setAdr(mEdit_adr.getText().toString().trim());
        mTeacher.setPassword(mEdit_password.getText().toString().trim());
        mTeacher.setUrl("");
        mHandler.sendEmptyMessage(INPUT_DONE);
    }

    private void addTeacher(String content) {
        if (!NetUtil.isNetWorkConnected(AddTeacherActivity.this)) {
            XToast.showToast(AddTeacherActivity.this, "请检查网络连接");
            return;
        }
        HttpUtils.HttpCallback<ResultBean> callback = new HttpUtils.HttpCallback<ResultBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(getApplicationContext(), "上传失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(ResultBean result) {
                Toast.makeText(getApplicationContext(), "上传完成", Toast.LENGTH_SHORT).show();
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
        MHttpClient.addTeacher(content, callback);
    }

    private void getAllClass() {
        HttpUtils.HttpCallback<ClassInfo> callback = new HttpUtils.HttpCallback<ClassInfo>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                DbManager db = x.getDb(DBUtils.getStutdentInfoDaoConfig());
                try {
                    List<ClassInfo> result = db.selector(ClassInfo.class).findAll();
                    if (result != null && result.size() != 0) {
                        mList.clear();
                        mList.addAll(result);
                        mHandler.sendEmptyMessage(CLASS_DONE);
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
                mList.clear();
                mList.addAll(result);
                mHandler.sendEmptyMessage(CLASS_DONE);
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        };
        MHttpClient.getClassInfo(callback);
    }

    private void showChooseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择教师所属班级");
        View view = LayoutInflater.from(this).inflate(R.layout.rv_item, null);
        builder.setView(view);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.at_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        getAllClass();
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<ClassInfo> result = mAdapter.getList();
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < result.size(); i++) {
                    stringBuilder.append(result.get(i).getClass_name());
                    if (i != result.size() - 1) {
                        stringBuilder.append(",");
                    }
                }
                mTeacher.setClass_name(stringBuilder.toString());
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
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
            mTv_uri.setText(getPath(this, mUri));
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
