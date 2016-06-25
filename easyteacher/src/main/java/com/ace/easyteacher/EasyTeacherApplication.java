package com.ace.easyteacher;

import android.app.Application;

import com.ace.easyteacher.DataBase.TeacherInfo;
import com.ace.easyteacher.View.Calculator;
import com.magicare.mutils.MLog;

import org.xutils.x;

public class EasyTeacherApplication extends Application {

    private TeacherInfo teacherInfo;
    private String whichClass ;
    public static EasyTeacherApplication mApp;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        MLog.setEnable(BuildConfig.DEBUG);
        initXUtils();
        teacherInfo = new TeacherInfo();
    }

    private void initXUtils() {
        x.Ext.init(this);
        x.Ext.setDebug(true);
        Calculator.init(this);
    }

    public String getWhichClass() {
        return whichClass;
    }

    public void setWhichClass(String whichClass) {
        this.whichClass = whichClass;
    }

    public void setTeacherInfo(String className) {
        teacherInfo.setClass_name(className);
    }

    public TeacherInfo getTeacherInfo() {
        return teacherInfo;
    }

    public void setTeacherInfo(TeacherInfo teacherInfo) {
        this.teacherInfo = teacherInfo;
    }

}
