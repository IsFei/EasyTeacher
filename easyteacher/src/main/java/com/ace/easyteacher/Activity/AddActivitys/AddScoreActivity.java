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
import android.widget.Toast;

import com.ace.easyteacher.DataBase.ClassInfo;
import com.ace.easyteacher.DataBase.Exam;
import com.ace.easyteacher.DataBase.StudentGrade;
import com.ace.easyteacher.Http.HttpUtils;
import com.ace.easyteacher.Http.MHttpClient;
import com.ace.easyteacher.R;
import com.ace.easyteacher.Utils.XToast;
import com.alibaba.fastjson.JSON;
import com.magicare.mutils.NetUtil;
import com.magicare.mutils.common.Callback;

import org.xutils.ex.DbException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class AddScoreActivity extends AppCompatActivity {
    @Bind(R.id.as_sp_class)
    Spinner mSP_class;
    @Bind(R.id.as_sp_type)
    Spinner mSP_type;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private Button mBtn_confirm, mBtn_open;
    private TextView mTv_file_name;
    private final static int FILE_SELECT_CODE = 99;
    private List<StudentGrade> mList = new ArrayList<>();
    private String mType;
    private String mClass_name;
    private String[] mTimeArray;
    private String[] mClassArray;
    private String[] mTypeArray;
    private static final int INPUT_DONE = 2;
    private Uri mUri;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case INPUT_DONE:
                    addGrade(JSON.toJSONString(mList));
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_score);
        ButterKnife.bind(this);
        toolbar.setTitle("添加成绩");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        init();
    }

    private void init() {
        getClassArray();
        getTypeArray();
        mBtn_open = (Button) findViewById(R.id.btn_openFile);
        mTv_file_name = (TextView) findViewById(R.id.tv_file_name);
        mBtn_confirm = (Button) findViewById(R.id.confirm);
        mBtn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeList();
            }
        });
        mBtn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFile();
            }
        });
    }


    private void addGrade(String content) {
        if (!NetUtil.isNetWorkConnected(AddScoreActivity.this)) {
            XToast.showToast(AddScoreActivity.this, "请检查网络连接");
            return;
        }
        HttpUtils.HttpCallback<String> callback = new HttpUtils.HttpCallback<String>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                XToast.showToast(AddScoreActivity.this, "上传失败");
            }

            @Override
            public void onSuccess(String result) {
                Toast.makeText(getApplicationContext(), "上传完成", Toast.LENGTH_SHORT).show();
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
        MHttpClient.addGrade(content, callback);
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
            mTv_file_name.setText(getPath(this, mUri));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void makeList() {
        try {

            /**
             * 后续考虑问题,比如Excel里面的图片以及其他数据类型的读取
             **/
            if (mUri != null) {
                Workbook book = Workbook
                        .getWorkbook(new File(getPath(this, mUri)));
                book.getNumberOfSheets();
                Sheet sheet = book.getSheet(0);
                int Rows = sheet.getRows();
                int Cols = sheet.getColumns();
                for (int i = 1; i < Rows; i++) {
                    StudentGrade studentGrade = new StudentGrade();
                    studentGrade.setClass_name(mClass_name);
                    studentGrade.setType_id(mType);
                    for (int j = 0; j < Cols; j++) {
                        Cell cell = sheet.getCell(j, i);
                        switch (sheet.getCell(j, 0).getContents()) {
                            case "学号":
                                studentGrade.setSid(Long.parseLong(cell.getContents()));
                                break;
                            case "语文":
                                studentGrade.setYw(Float.parseFloat(cell.getContents()));
                                break;
                            case "数学":
                                studentGrade.setSx(Float.parseFloat(cell.getContents()));
                                break;
                            case "外语":
                                studentGrade.setWy(Float.parseFloat(cell.getContents()));
                                break;
                            case "物理":
                                studentGrade.setWl(Float.parseFloat(cell.getContents()));
                                break;
                            case "化学":
                                studentGrade.setHx(Float.parseFloat(cell.getContents()));
                                break;
                            case "生物":
                                studentGrade.setSw(Float.parseFloat(cell.getContents()));
                                break;
                            case "历史":
                                studentGrade.setLs(Float.parseFloat(cell.getContents()));
                                break;
                            case "地理":
                                studentGrade.setDl(Float.parseFloat(cell.getContents()));
                                break;
                            case "政治":
                                studentGrade.setZz(Float.parseFloat(cell.getContents()));
                                break;
                            case "体育":
                                studentGrade.setTy(Float.parseFloat(cell.getContents()));
                                break;
                            case "班级排名":
                                studentGrade.setRank_class(cell.getContents());
                                break;
                            case "学校排名":
                                studentGrade.setRank_school(cell.getContents());
                                break;
                            default:
                                break;
                        }
                    }
                    studentGrade.setType_id(mType);
                    studentGrade.setClass_name(mClass_name);
                    mList.add(studentGrade);
                }
                book.close();
                mHandler.sendEmptyMessage(INPUT_DONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void getClassArray() {
        HttpUtils.HttpCallback<ClassInfo> callback = new HttpUtils.HttpCallback<ClassInfo>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onSuccess(ClassInfo result) {

            }

            @Override
            public void onSuccess(List<ClassInfo> result) throws DbException {
                mClassArray = new String[result.size()];
                for (int i = 0; i < result.size(); i++) {
                    mClassArray[i] = result.get(i).getClass_name();
                }
                ArrayAdapter<String> classAdapter = new ArrayAdapter<String>(AddScoreActivity.this, android.R.layout.simple_spinner_item, mClassArray);
                classAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                mSP_class.setAdapter(classAdapter);
                mSP_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        mClass_name = mClassArray[position];
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
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

    private void getTypeArray() {
        HttpUtils.HttpCallback<Exam> callback = new HttpUtils.HttpCallback<Exam>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onSuccess(Exam result) {

            }

            @Override
            public void onSuccess(List<Exam> result) throws DbException {
                mTypeArray = new String[result.size()];
                mTimeArray = new String[result.size()];
                for (int i = 0; i < result.size(); i++) {
                    mTypeArray[i] = result.get(i).getType_id() + "";
                    mTimeArray[i] = result.get(i).getTime();
                }
                ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(AddScoreActivity.this, android.R.layout.simple_spinner_item, mTimeArray);
                typeAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                mSP_type.setAdapter(typeAdapter);
                mSP_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        mType = mTypeArray[position];
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

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


}
