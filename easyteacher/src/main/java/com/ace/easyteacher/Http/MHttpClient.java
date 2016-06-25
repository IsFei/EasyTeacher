package com.ace.easyteacher.Http;

import com.ace.easyteacher.Bean.ResultBean;
import com.ace.easyteacher.DataBase.Check;
import com.ace.easyteacher.DataBase.ClassInfo;
import com.ace.easyteacher.DataBase.Exam;
import com.ace.easyteacher.DataBase.Note;
import com.ace.easyteacher.DataBase.StudentGrade;
import com.ace.easyteacher.DataBase.StudentInfo;
import com.ace.easyteacher.DataBase.TeacherInfo;
import com.ace.easyteacher.DataBase.TeacherSchedule;
import com.magicare.mutils.common.Callback;

public class MHttpClient {
    public static Callback.Cancelable login(String user, String pwd,
                                            HttpUtils.HttpCallback<String> callback) {
        String url ="http://112.74.31.135:8080/easyteacher2/Login";
        HttpValues params = new HttpValues();
        params.add("job_number", user);
        params.add("password", pwd);
        return HttpUtils.post(url, params, callback, String.class);
    }
    public static Callback.Cancelable getAllStudentsInfo(String class_name,
                                            HttpUtils.HttpCallback<StudentInfo> callback) {
        String url ="http://112.74.31.135:8080/easyteacher2/GetAllStudentsInfo";
        HttpValues params = new HttpValues();
        params.add("class_name",class_name);
        return HttpUtils.post(url, params, callback, StudentInfo.class);
    }
    public static Callback.Cancelable alterPassword(String user,String new_pwd,
                                                         HttpUtils.HttpCallback<ResultBean> callback ) {
        String url ="http://112.74.31.135:8080/easyteacher2/AlterPassword";
        HttpValues params = new HttpValues();
        params.add("job_number", user);
        params.add("new_password", new_pwd);
        return HttpUtils.post(url, params,callback, ResultBean.class);
    }
    public static Callback.Cancelable getNotes(String job_number,
                                                    HttpUtils.HttpCallback<Note> callback ) {
        String url ="http://112.74.31.135:8080/easyteacher2/GetNotes";
        HttpValues params = new HttpValues();
        params.add("job_number", job_number);
        return HttpUtils.post(url, params,callback, Note.class);
    }
    public static Callback.Cancelable getGradeBySid(String sid,HttpUtils.HttpCallback<StudentGrade> callback){
        String url ="http://112.74.31.135:8080/easyteacher2/GetGrade";
        HttpValues params = new HttpValues();
        params.add("sid",sid);
        return HttpUtils.post(url,params,callback,StudentGrade.class);
    }
    public static Callback.Cancelable getGradeByClass(String class_name,String type_id,HttpUtils.HttpCallback<StudentGrade> callback){
        String url ="http://112.74.31.135:8080/easyteacher2/GetGrade";
        HttpValues params = new HttpValues();
        params.add("class_name",class_name);
        params.add("type_id",type_id);
        return HttpUtils.post(url,params,callback,StudentGrade.class);
    }
    public static Callback.Cancelable getTeacherInfoByJob(String job_number,HttpUtils.HttpCallback<TeacherInfo> callback){
        String url ="http://112.74.31.135:8080/easyteacher2/GetTeacherInfo";
        HttpValues params = new HttpValues();
        params.add("job_number",job_number);
        return HttpUtils.post(url,params,callback,TeacherInfo.class);
    }
    public static Callback.Cancelable getTeacherInfo(HttpUtils.HttpCallback<TeacherInfo> callback){
        String url ="http://112.74.31.135:8080/easyteacher2/GetTeacherInfo";
        HttpValues params = new HttpValues();
        return HttpUtils.post(url,params,callback,TeacherInfo.class);
    }
    public static Callback.Cancelable getExam(HttpUtils.HttpCallback<Exam> callback){
        String url ="http://112.74.31.135:8080/easyteacher2/GetExam";
        HttpValues params = new HttpValues();
        return HttpUtils.post(url,params,callback,Exam.class);
    }
    public static Callback.Cancelable getScheduleByWeek(String week,HttpUtils.HttpCallback<TeacherSchedule> callback){
        String url ="http://112.74.31.135:8080/easyteacher2/GetSchedule";
        HttpValues params = new HttpValues();
        params.add("week",week);
        return HttpUtils.post(url,params,callback,TeacherSchedule.class);
    }
    public static Callback.Cancelable getScheduleByClass(String class_name,HttpUtils.HttpCallback<TeacherSchedule> callback){
        String url ="http://112.74.31.135:8080/easyteacher2/GetSchedule";
        HttpValues params = new HttpValues();
        params.add("class_name",class_name);
        return HttpUtils.post(url,params,callback,TeacherSchedule.class);
    }
    public static Callback.Cancelable addCheck(String content,HttpUtils.HttpCallback<ResultBean> callback){
        String url ="http://112.74.31.135:8080/easyteacher2/AddCheck";
        HttpValues params = new HttpValues();
        params.add("content",content);
        return HttpUtils.post(url,params,callback,ResultBean.class);
    }
    public static Callback.Cancelable getCheckBySid(String sid,HttpUtils.HttpCallback<Check> callback){
        String url ="http://112.74.31.135:8080/easyteacher2/GetCheck";
        HttpValues params = new HttpValues();
        params.add("sid",sid);
        return HttpUtils.post(url,params,callback,Check.class);
    }
    public static Callback.Cancelable getCheckByClass(String class_name,HttpUtils.HttpCallback<Check> callback){
        String url ="http://112.74.31.135:8080/easyteacher2/GetCheck";
        HttpValues params = new HttpValues();
        params.add("class_name",class_name);
        return HttpUtils.post(url,params,callback,Check.class);
    }
    public static Callback.Cancelable addNote(String content,HttpUtils.HttpCallback<ResultBean> callback){
        String url ="http://112.74.31.135:8080/easyteacher2/AddNote";
        HttpValues params = new HttpValues();
        params.add("content",content);
        return HttpUtils.post(url,params,callback,ResultBean.class);
    }
    public static Callback.Cancelable deleteNote(String content,HttpUtils.HttpCallback<ResultBean> callback){
        String url ="http://112.74.31.135:8080/easyteacher2/DeleteNote";
        HttpValues params = new HttpValues();
        params.add("content",content);
        return HttpUtils.post(url,params,callback,ResultBean.class);
    }

    public static Callback.Cancelable addStudent(String content, HttpUtils.HttpCallback<String> callback) {
        String url = "http://112.74.31.135:8080/easyteacher2/XStudent";
//        String url = "http://xiaqiuming.xicp.net/easyteacher2/XStudent";
        HttpValues params = new HttpValues();
        params.add("content", content);
        params.add("type", "");
        return HttpUtils.post(url, params, callback, String.class);
    }

    public static Callback.Cancelable deleteStudent(String content, HttpUtils.HttpCallback<String> callback) {
        String url = "http://112.74.31.135:8080/easyteacher2/XStudent";
//        String url = "http://xiaqiuming.xicp.net/easyteacher2/XStudent";
        HttpValues params = new HttpValues();
        params.add("sid", content);
        params.add("type", "delete");
        return HttpUtils.post(url, params, callback, String.class);
    }

    public static Callback.Cancelable addTeacher(String content, HttpUtils.HttpCallback<ResultBean> callback) {
//        String url = "http://xiaqiuming.xicp.net/easyteacher2/XTeacher";
        String url = "http://112.74.31.135:8080/easyteacher2/XTeacher";
        HttpValues params = new HttpValues();
        params.add("content", content);
        params.add("type", "");
        return HttpUtils.post(url, params, callback, ResultBean.class);
    }

    public static Callback.Cancelable addGrade(String content, HttpUtils.HttpCallback<String> callback) {
        String url = "http://112.74.31.135:8080/easyteacher2/XGrade";
        HttpValues params = new HttpValues();
        params.add("content", content);
        return HttpUtils.post(url, params, callback, String.class);
    }

    public static Callback.Cancelable addClass(String content, HttpUtils.HttpCallback<String> callback) {
        String url = "http://112.74.31.135:8080/easyteacher2/XClass";
        HttpValues params = new HttpValues();
        params.add("content", content);
        return HttpUtils.post(url, params, callback, String.class);
    }

    public static Callback.Cancelable addSchedule(String content, HttpUtils.HttpCallback<String> callback) {
        String url = "http://112.74.31.135:8080/easyteacher2/XSchedule";
//        String url = "http://xiaqiuming.xicp.net:20062/easyteacher2/XSchedule";
        HttpValues params = new HttpValues();
        params.add("content", content);
        params.add("type", "");
        return HttpUtils.post(url, params, callback, String.class);
    }

    public static Callback.Cancelable deleteSchedule(String content, HttpUtils.HttpCallback<ResultBean> callback) {
        String url = "http://112.74.31.135:8080/easyteacher2/XSchedule";
//        String url = "http://xiaqiuming.xicp.net/easyteacher2/XSchedule";
        HttpValues params = new HttpValues();
        params.add("class_name", content);
        params.add("type", "delete");
        return HttpUtils.post(url, params, callback, ResultBean.class);
    }

    public static Callback.Cancelable deleteTeacher(String job_number, HttpUtils.HttpCallback<ResultBean> callback) {
        String url = "http://112.74.31.135:8080/easyteacher2/XTeacher";
//        String url = "http://xiaqiuming.xicp.net/easyteacher2/XTeacher";
        HttpValues params = new HttpValues();
        params.add("type", "delete");
        params.add("job_number", job_number);
        return HttpUtils.post(url, params, callback, ResultBean.class);
    }

    public static Callback.Cancelable addExam(String content, HttpUtils.HttpCallback<String> callback) {
//        String url = "http://xiaqiuming.xicp.net/easyteacher2/XExam";
        String url = "http://112.74.31.135:8080/easyteacher2/XExam";
        HttpValues params = new HttpValues();
        params.add("content", content);
        params.add("type", "");
        return HttpUtils.post(url, params, callback, String.class);
    }

    public static Callback.Cancelable getClassInfo(HttpUtils.HttpCallback<ClassInfo> callback) {
//        String url = "http://xiaqiuming.xicp.net/easyteacher2/GetClass";
        String url = "http://112.74.31.135:8080/easyteacher2/GetClass";
        HttpValues params = new HttpValues();
        return HttpUtils.post(url, params, callback, ClassInfo.class);
    }

}
