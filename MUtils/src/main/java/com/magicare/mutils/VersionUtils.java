package com.magicare.mutils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

/**
 * @author justin on 2015/12/10 11:15
 *         justin@magicare.me
 * @version V1.0
 */
public class VersionUtils {

    private static int mVersionCode = -1;
    private static String mVersionName = null;

    public static String getVersionName(Context context) {
        if (TextUtils.isEmpty(mVersionName)) {
            PackageInfo info = getPackageInfo(context);
            mVersionName = info == null ? null : info.versionName;
            mVersionCode = info == null ? -1 : info.versionCode;
        }
        return mVersionName;
    }

    //版本号
    public static int getVersionCode(Context context) {
        if (mVersionCode == -1) {
            PackageInfo info = getPackageInfo(context);
            mVersionName = info == null ? null : info.versionName;
            mVersionCode = info == null ? -1 : info.versionCode;
        }
        return mVersionCode;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }
}
