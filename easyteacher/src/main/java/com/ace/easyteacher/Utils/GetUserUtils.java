package com.ace.easyteacher.Utils;

import android.util.Log;

import com.ace.easyteacher.DataBase.Exam;
import com.ace.easyteacher.DataBase.Note;
import com.ace.easyteacher.DataBase.StudentGrade;
import com.ace.easyteacher.DataBase.StudentInfo;
import com.ace.easyteacher.DataBase.TeacherInfo;
import com.ace.easyteacher.DataBase.TeacherSchedule;
import com.ace.easyteacher.Http.HttpUtils;
import com.ace.easyteacher.Http.MHttpClient;
import com.magicare.mutils.common.Callback;

import java.util.List;

/**
 * 网络访问的工具类
 */
public class GetUserUtils {

//        login("500323000", "123456");
//        getAllStudentsInfo("高二19班");
//        getNotes("500323000");
//        getGradeBySid("20160218001");
//        getGradeByClass("高二19班","2");
//        getTeacherInfo();
//        getTeacherInfoByJob("500323000");
//        getExam();
//        getScheduleByClass("高二19班");
//        getScheduleByWeek("1");
//        write();
//        read();

//    private void login(String user, String pwd) {
//        HttpUtils.HttpCallback<ResultBean> callback = new HttpUtils.HttpCallback<ResultBean>() {
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                Log.d("ian", "fail");
//            }
//
//            @Override
//            public void onSuccess(ResultBean result) {
//                Log.d("ian", "success");
//            }
//
//            @Override
//            public void onSuccess(List<ResultBean> result) {
//                Log.d("ian", "success");
//            }
//
//            @Override
//            public void onCancelled(Callback.CancelledException cex) {
//
//            }
//
//            @Override
//            public void onFinished() {
//
//            }
//        };
//        MHttpClient.login(user, pwd, callback);
//    }

    private void getAllStudentsInfo(String class_name) {
        HttpUtils.HttpCallback<StudentInfo> callback = new HttpUtils.HttpCallback<StudentInfo>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d("ian", "fail");
            }

            @Override
            public void onSuccess(StudentInfo result) {
                Log.d("ian", "success");
            }

            @Override
            public void onSuccess(List<StudentInfo> result) {
                Log.d("ian", "success" + result.size());
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        };
        MHttpClient.getAllStudentsInfo(class_name, callback);
    }

    private void getNotes(String job_number) {
        HttpUtils.HttpCallback<Note> callback = new HttpUtils.HttpCallback<Note>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d("ian", "fail");
            }

            @Override
            public void onSuccess(Note result) {
                Log.d("ian", "success");
            }

            @Override
            public void onSuccess(List<Note> result) {
                Log.d("ian", "success" + result.size());
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        };
        MHttpClient.getNotes(job_number, callback);
    }

    private void getGradeBySid(String sid) {
        HttpUtils.HttpCallback<StudentGrade> callback = new HttpUtils.HttpCallback<StudentGrade>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d("ian", "fail");
            }

            @Override
            public void onSuccess(StudentGrade result) {
                Log.d("ian", "success");
            }

            @Override
            public void onSuccess(List<StudentGrade> result) {
                Log.d("ian", "success" + result.size());
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        };
        MHttpClient.getGradeBySid(sid, callback);
    }

    private void getGradeByClass(String class_name, String type_id) {
        HttpUtils.HttpCallback<StudentGrade> callback = new HttpUtils.HttpCallback<StudentGrade>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d("ian", "fail");
            }

            @Override
            public void onSuccess(StudentGrade result) {
                Log.d("ian", "success");
            }

            @Override
            public void onSuccess(List<StudentGrade> result) {
                Log.d("ian", "success" + result.size());
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        };
        MHttpClient.getGradeByClass(class_name, type_id, callback);
    }

    private void getTeacherInfoByJob(String job_number) {
        HttpUtils.HttpCallback<TeacherInfo> callback = new HttpUtils.HttpCallback<TeacherInfo>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d("ian", "fail");
            }

            @Override
            public void onSuccess(TeacherInfo result) {
                Log.d("ian", "success");
            }

            @Override
            public void onSuccess(List<TeacherInfo> result) {
                Log.d("ian", "success" + result.size());
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        };
        MHttpClient.getTeacherInfoByJob(job_number, callback);
    }

    private void getTeacherInfo() {
        HttpUtils.HttpCallback<TeacherInfo> callback = new HttpUtils.HttpCallback<TeacherInfo>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d("ian", "fail");
            }

            @Override
            public void onSuccess(TeacherInfo result) {
                Log.d("ian", "success");
            }

            @Override
            public void onSuccess(List<TeacherInfo> result) {
                Log.d("ian", "success" + result.size());
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        };
        MHttpClient.getTeacherInfo(callback);
    }

    private void getExam() {
        HttpUtils.HttpCallback<Exam> callback = new HttpUtils.HttpCallback<Exam>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d("ian", "fail");
            }

            @Override
            public void onSuccess(Exam result) {
                Log.d("ian", "success");
            }

            @Override
            public void onSuccess(List<Exam> result) {
                Log.d("ian", "success" + result.size());
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
    private void getScheduleByWeek(String week) {
        HttpUtils.HttpCallback<TeacherSchedule> callback = new HttpUtils.HttpCallback<TeacherSchedule>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d("ian", "fail");
            }

            @Override
            public void onSuccess(TeacherSchedule result) {
                Log.d("ian", "success");
            }

            @Override
            public void onSuccess(List<TeacherSchedule> result) {
                Log.d("ian", "success" + result.size());
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        };
        MHttpClient.getScheduleByWeek(week, callback);
    }
    private void getScheduleByClass(String class_name) {
        HttpUtils.HttpCallback<TeacherSchedule> callback = new HttpUtils.HttpCallback<TeacherSchedule>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d("ian", "fail");
            }

            @Override
            public void onSuccess(TeacherSchedule result) {
                Log.d("ian", "success");
            }

            @Override
            public void onSuccess(List<TeacherSchedule> result) {
                Log.d("ian", "success" + result.size());
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        };
        MHttpClient.getScheduleByClass(class_name, callback);
    }
}
