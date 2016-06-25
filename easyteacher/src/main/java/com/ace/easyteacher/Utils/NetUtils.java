package com.ace.easyteacher.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.magicare.mutils.MLog;


public class NetUtils {
    private static final String TAG = "NetUtil";

    public static final int NETWORN_NONE = 0;
    public static final int NETWORN_WIFI = 1;
    public static final int NETWORN_MOBILE = 2;

    public static boolean isNetConnect(Context context) {
        int status = getNetworkState(context);
        return status == NETWORN_NONE ? false : true;
    }

    public static int getNetworkState(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        // Wifi
        NetworkInfo.State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();
        if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
            return NETWORN_WIFI;
        }

        // 3G
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (networkInfo != null) {
            try {
                NetworkInfo.State state1 = networkInfo.getState();
                if (state1 == NetworkInfo.State.CONNECTED || state1 == NetworkInfo.State.CONNECTING) {
                    return NETWORN_MOBILE;
                }
            } catch (Exception e) {
                MLog.e(TAG, "获取3g状态出问题,该PAD无法使用sim卡");
            }
        } else {
            MLog.e(TAG, "获取3g状态出问题,该PAD无法使用sim卡");
        }
        return NETWORN_NONE;
    }


    public static boolean is3GConnectivity(Context ctx) {
        ConnectivityManager manager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        // WIFI的描述信息：NetworkInfo
        NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (networkInfo != null)
            return networkInfo.isConnected();
        return false;
    }

    /**
     * WIFI是否处于连接状态
     *
     * @param context
     * @return
     */
    public static boolean isWIFIConnectivity(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // WIFI的描述信息：NetworkInfo
        NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (networkInfo != null)
            return networkInfo.isConnected();
        return false;
    }

    /**
     * Mobile(apn)是否处于连接状态
     *
     * @param context
     * @return
     */
    public static boolean isAPNConnectivity(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (networkInfo != null)
            return networkInfo.isConnected();
        return false;
    }
}
