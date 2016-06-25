package com.magicare.mutils;

import android.text.TextUtils;
import android.util.Log;

/**
 * @author justin on 2015/12/11 11:15
 *         justin@magicare.me
 * @version V1.0
 */
public class MLog {

    public static final int VERBOSE = 2;

    public static final int DEBUG = 3;

    public static final int INFO = 4;
    public static final int WARN = 5;
    public static final int ERROR = 6;
    private static final String customTagPrefix = "";
    private static boolean isEnable = false;
    private static int LOG_LEVEL = VERBOSE;

    public static boolean isEnable() {
        return isEnable;
    }

    public static void setEnable(boolean enable) {
        isEnable = enable;
    }

    public static String generateTag() {
        String tag = "%s#%s";
        try {
            StackTraceElement stack[] = Thread.currentThread().getStackTrace();
            StackTraceElement caller = stack[4];
            String callerClazzName = caller.getClassName();
            callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
            tag = String.format(tag, callerClazzName, caller.getMethodName());
            tag = TextUtils.isEmpty(customTagPrefix) ? tag : customTagPrefix + ":" + tag;
        } catch (Exception e) {
            tag = "Magicare";
        }
        return tag;
    }
    private static String codeLine(String msg) {

        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        int index = 2;
        int size = stackTrace.length;
        StackTraceElement last = null;
        while(index <size){
            last = stackTrace[index];
            if(!"MLog.java".equals(last.getFileName())){
                break;
            }
            index++;
        }
        if(last == null){
            return msg;
        }
        String className = last.getFileName();
        String methodName = last.getMethodName();
        int lineNumber = last.getLineNumber();
        String methodNameShort = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);

        return "[ (" + className + ":" + lineNumber + ")#" + methodNameShort + " ] "+ msg;
    }
    public static void v(String msg) {

        v(generateTag(), msg);
    }

    public static void v(Class<?> cls, String msg) {
        v(cls.getName(), msg, null);
    }

    public static void v(Class<?> cls, Throwable throwable) {
        v(cls.getName(), null, throwable);
    }

    public static void v(Class<?> cls, String msg, Throwable throwable) {
        v(cls.getName(), msg, throwable);
    }

    public static void v(String tag, String msg) {
        v(tag, msg, null);
    }

    public static void v(String tag, Throwable throwable) {
        v(tag, null, throwable);
    }

    public static void v(String tag, String msg, Throwable throwable) {
        if (isEnable && LOG_LEVEL <= VERBOSE) {
            Log.v(tag, codeLine(msg), throwable);
        }
    }

    public static void d(String msg) {

        d(generateTag(), msg);
    }

    public static void d(Class<?> cls, String msg) {
        d(cls.getName(), msg, null);
    }


    public static void d(Class<?> cls, Throwable throwable) {
        d(cls.getName(), null, throwable);
    }

    public static void d(Class<?> cls, String msg, Throwable throwable) {
        d(cls.getName(), msg, throwable);
    }

    public static void d(String tag, String msg) {
        d(tag, msg, null);
    }

    public static void d(String tag, Throwable throwable) {
        d(tag, null, throwable);
    }

    public static void d(String tag, String msg, Throwable throwable) {
        if (isEnable && LOG_LEVEL <= DEBUG) {
            Log.d(tag, codeLine(msg), throwable);
        }
    }

    public static void i(String msg) {

        i(generateTag(), msg);
    }

    public static void i(Class<?> cls, String msg) {
        i(cls.getName(), msg, null);
    }

    public static void i(Class<?> cls, Throwable throwable) {
        i(cls.getName(), null, throwable);
    }

    public static void i(Class<?> cls, String msg, Throwable throwable) {
        i(cls.getName(), msg, throwable);
    }

    public static void i(String tag, String msg) {
        i(tag, msg, null);
    }

    public static void i(String tag, Throwable throwable) {
        i(tag, null, throwable);
    }

    public static void i(String tag, String msg, Throwable throwable) {

        if (isEnable && LOG_LEVEL <= INFO) {
            Log.i(tag, codeLine(msg), throwable);
        }
    }

    public static void w(String msg) {

        w(generateTag(), msg);
    }

    public static void w(Class<?> cls, String msg) {
        w(cls.getName(), msg, null);
    }

    public static void w(Class<?> cls, Throwable throwable) {
        w(cls.getName(), null, throwable);
    }

    public static void w(Class<?> cls, String msg, Throwable throwable) {
        w(cls.getName(), msg, throwable);
    }

    public static void w(String tag, String msg) {
        w(tag, msg, null);
    }

    public static void w(String tag, Throwable throwable) {
        w(tag, null, throwable);
    }

    public static void w(String tag, String msg, Throwable throwable) {
        if (isEnable && LOG_LEVEL <= WARN) {
            Log.w(tag, codeLine(msg), throwable);
        }
    }

    public static void e(String msg) {

        e(generateTag(), msg);
    }

    public static void e(Class<?> cls, String msg) {
        e(cls.getName(), msg, null);
    }

    public static void e(Class<?> cls, Throwable throwable) {
        e(cls.getName(), null, throwable);
    }

    public static void e(Class<?> cls, String msg, Throwable throwable) {
        e(cls.getName(), msg, throwable);
    }

    public static void e(String tag, String msg) {
        e(tag, msg, null);
    }

    public static void e(String tag, Throwable throwable) {
        e(tag, null, throwable);
    }

    public static void e(String tag, String msg, Throwable throwable) {
        if (isEnable && LOG_LEVEL <= ERROR) {
            Log.e(tag, codeLine(msg), throwable);
        }
    }

}
