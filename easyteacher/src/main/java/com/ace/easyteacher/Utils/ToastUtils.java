package com.ace.easyteacher.Utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by lenovo on 2015-12-21.
 */
public class ToastUtils {
    private static Toast mToast;

    public static void Toast(Context context, String s, int t) {
        if ((TextUtils.isEmpty(s)) || t <= 0) {
            return;
        }

        if (mToast == null) {
            mToast = Toast.makeText(context, s, t);
        } else {
            mToast.setText(s);
            mToast.setDuration(t);
        }
        mToast.show();
    }

    public static void Toast(Context context, String s) {
        Toast(context,s,1000);
    }
}
