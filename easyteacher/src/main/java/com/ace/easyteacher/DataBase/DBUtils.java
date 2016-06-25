package com.ace.easyteacher.DataBase;


import android.os.Environment;

import org.xutils.DbManager;

import java.io.File;

public class DBUtils {
    static DbManager.DaoConfig daoConfig;
    public static DbManager.DaoConfig getStutdentInfoDaoConfig(){
        File file = new File(Environment.getExternalStorageDirectory().getPath());
        daoConfig = new DbManager.DaoConfig();
        daoConfig.setDbName("studentInfo.db").setDbVersion(1).setDbDir(file).setAllowTransaction(true);
        return daoConfig;
    }
//    public static DbManager.DaoConfig getTeacherScheduleDaoConfig(){
//        File file = new File(Environment.getExternalStorageDirectory().getPath());
//        daoConfig = new DbManager.DaoConfig();
//        daoConfig.setDbName("teacherSchedule.db").setDbVersion(1).setDbDir(file).setAllowTransaction(true);
//        return daoConfig;
//    }
//    public static DbManager.DaoConfig getnoteDaoConfig(){
//        File file = new File(Environment.getExternalStorageDirectory().getPath());
//        daoConfig = new DbManager.DaoConfig();
//        daoConfig.setDbName("note.db").setDbVersion(1).setDbDir(file).setAllowTransaction(true);
//        return daoConfig;
//    }
//    public static DbManager.DaoConfig getTeacherInfoDaoConfig(){
//        File file = new File(Environment.getExternalStorageDirectory().getPath());
//        daoConfig = new DbManager.DaoConfig();
//        daoConfig.setDbName("teacherInfo.db").setDbVersion(1).setDbDir(file).setAllowTransaction(true);
//        return daoConfig;
//    }
//    public static DbManager.DaoConfig getCheckDaoConfig(){
//        File file = new File(Environment.getExternalStorageDirectory().getPath());
//        daoConfig = new DbManager.DaoConfig();
//        daoConfig.setDbName("check.db").setDbVersion(1).setDbDir(file).setAllowTransaction(true);
//        return daoConfig;
//    }
//    public static DbManager.DaoConfig getStudentGradeConfig(){
//        File file = new File(Environment.getExternalStorageDirectory().getPath());
//        daoConfig = new DbManager.DaoConfig();
//        daoConfig.setDbName("studentGrade.db").setDbVersion(1).setDbDir(file).setAllowTransaction(true);
//        return daoConfig;
//    }
//    public static DbManager.DaoConfig getExamConfig(){
//        File file = new File(Environment.getExternalStorageDirectory().getPath());
//        daoConfig = new DbManager.DaoConfig();
//        daoConfig.setDbName("exam.db").setDbVersion(1).setDbDir(file).setAllowTransaction(true);
//        return daoConfig;
//    }
}
