package com.ace.easyteacher.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ace.easyteacher.R;


public class XToast {
    private static Toast toast;
    private static TextView mTv;
    private static ImageView mIv;
    private static ProgressDialog dialog;

    public static void showToast(Context context, String msg) {
        ToastUtils.Toast(context,msg);
    }

    public static void showToast(Context context, String msg, boolean isSuccess) {
        if (toast != null) {
            toast.cancel();
        }
        initToast(context);
        if (isSuccess) {
            mIv.setImageResource(R.drawable.tost_ic_02);
        } else {
            mIv.setImageResource(R.drawable.tost_ic_01);
        }
        mTv.setText(msg);
        toast.show();
    }

    public static void showAlarmToast(Context context, String msg) {
        if (toast != null) {
            toast.cancel();
        }
        initToast(context);
        mTv.setText(msg);
        mIv.setImageResource(R.drawable.tost_ic_01);
        toast.show();
    }


    private static void initToast(Context context) {
        toast = new Toast(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.toast, null);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        mTv = (TextView) view.findViewById(R.id.tv_content);
        mIv = (ImageView) view.findViewById(R.id.iv_icon);
    }

    public static void showNoneNetToast(Context context) {
        showToast(context, "请检查你的网络", false);
    }
    public static void showProgressDialog(Context context, String message) {
        if (dialog != null) {
            if (!dialog.isShowing()) {
                dialog = new ProgressDialog(context);
                dialog.setMessage(message);
                try {
                    dialog.show();
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        } else {
            dialog = new ProgressDialog(context);
            dialog.setMessage(message);
            try {
                dialog.show();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

    }
    public static void closeProgressDialog() {
        if (dialog != null && dialog.isShowing()) {
            try {
                dialog.dismiss();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }


}
