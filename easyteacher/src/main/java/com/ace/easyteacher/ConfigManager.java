package com.ace.easyteacher;

import android.content.Context;
import android.content.SharedPreferences;


public class ConfigManager {
    private final static String mXmlFile = "easyteacher_xml";
    public final static String PASSWORD = "password";
    public final static String JOB_NUMBER = "job_number";

    public static boolean getBooleanValue(Context context, String key, boolean defalut) {
        SharedPreferences store = context.getSharedPreferences(mXmlFile, 0);
        return store.getBoolean(key, defalut);
    }

    /* 设置一个布尔值 */
    public static void setBooleanValue(Context context, String key, boolean value) {
        SharedPreferences store = context.getSharedPreferences(mXmlFile, 0);
        SharedPreferences.Editor editor = store.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
    /* 获取一个字符 */
    public static String getStringValue(Context context, String key) {
        SharedPreferences store = context.getSharedPreferences(mXmlFile, 0);// 建立storexml.xml
        return store.getString(key, "");// 从codoon_config_xml中读取上次进度，存到string1中
    }

    /* 设置一个字符 */
    public static void setStringValue(Context context, String key, String value) {
        SharedPreferences store = context.getSharedPreferences(mXmlFile, 0);
        SharedPreferences.Editor editor = store.edit();
        editor.putString(key, value);
        editor.commit();

    }
}
