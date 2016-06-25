package com.ace.easyteacher.View;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

/**
 * Created by Xman on 2016/3/16
 */
// TODO: 2016/4/8 修改为不需要fit_style
public class Calculator {

    private final static int baseHeight = 1920;
    private final static int baseWidth = 1080;
    private final static int baseDensity = 3;
//    private final static float mOldAspect_ratio = 1920/1080;

    private static float mHScaleSize;
    private static float mWScaleSize;
    private static float mBothSize;


    private static float mScrHeight;
    private static float mScrWidth;
    private static float mDensity;
    private static float mScaleDensity;
    private static int mDpi;
    private static float mScale;
//    private static float mNewAspect_ratio;
    private static String TAG = "size";
    private volatile static Calculator instance = null;
    private static Context context;

    public static FIT_STYLE mDefaultStyle = FIT_STYLE.Y;


    private static Calculator getInstance(Context host) {
        if (instance == null) {
            synchronized (Calculator.class) {
                if (instance == null) {
                    instance = new Calculator(host);
                }
            }
        }
        return instance;
    }

    public static Calculator init(Context host) {
        if (!(host instanceof Application)) {
            throw new IllegalArgumentException("error host must be application");
        }
        context = host;
        return getInstance(host);
    }

    private Calculator(Context host) {
        WindowManager wm = (WindowManager) host.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);

        mScrWidth = outMetrics.widthPixels;  // 屏幕宽度（像素）
        mScrHeight = outMetrics.heightPixels;  // 屏幕高度（像素）
//        mNewAspect_ratio = mScrHeight/mScrWidth;
        mDensity = outMetrics.density;  // 屏幕密度（0.75 / 1.0 / 1.5/2/3）
        mDpi = outMetrics.densityDpi;  // 屏幕密度DPI（120 / 160 / 240/320..）
        mScaleDensity = outMetrics.scaledDensity;

        mHScaleSize = mScrHeight / baseHeight;//高度比例
        mWScaleSize = mScrWidth / baseWidth;//宽度比例
        mBothSize = (mHScaleSize + mWScaleSize) / 2;

        mScale = baseDensity / mDensity;


        Log.i(TAG, "mHScaleSize " + mHScaleSize);
        Log.i(TAG, "mScrHeight " + mScrHeight);
        Log.i(TAG, "mScrWidth " + mScrWidth);
        Log.i(TAG, "mDpi " + mDpi);
        Log.i(TAG, "mBothSize " + mBothSize);
        Log.i(TAG, "mWScaleSize " + mWScaleSize);
        Log.i(TAG, "mDensity " + mDensity);
//
    }


    private static float getScaleSize(FIT_STYLE style, TYPE type) {
        switch (style) {
            case BOTH:
                return type == TYPE.DP ? mBothSize * mScale : mBothSize;
            case Y:
                return type == TYPE.DP ? mHScaleSize * mScale : mHScaleSize;
            case X:
                return type == TYPE.DP ? mWScaleSize * mScale : mWScaleSize;
            case NO:
                return 1;
        }
        return getScaleSize(FIT_STYLE.X, TYPE.DP);
    }

    public static float parseSize(String str, FIT_STYLE style) {

        if (str != null) {
            Log.i("str", str);
            String strs[] = str.split("\\.");
            if (strs.length > 1 && Integer.valueOf(strs[0]) > 0) {
                float mScaleSize;
                String unit = str.substring(str.indexOf(".") + 2);
                float value = Float.parseFloat(str.substring(0, str.indexOf(".") + 1));
                switch (unit) {
                    case "px":
                        mScaleSize = getScaleSize(style, TYPE.PX);
                        return value * mScaleSize;
                    case "dip":
                        mScaleSize = getScaleSize(style, TYPE.DP);
                        return value * mDensity * mScaleSize + 0.5f;
                    case "sp":
                        mScaleSize = getScaleSize(style, TYPE.DP);
                        return value * mScaleDensity * mScaleSize + 0.5f;
                    case "pi":
                        return value;
                    case "pt":
                        return value;
                    case "in":
                        return value;
                    case "mm":
                        return value;
                }
            }
        }
        return 0;
    }

    public static void setDefaultFitStyle(FIT_STYLE style) {
        mDefaultStyle = style;
    }

    public static FIT_STYLE getFitStyle(String style) {

        if (style != null && TextUtils.isEmpty(style)) {
            switch (style) {
                case "both":
                case "BOTH":
                    return FIT_STYLE.BOTH;
                case "x":
                case "X":
                    return FIT_STYLE.X;
                case "y":
                case "Y":
                    return FIT_STYLE.Y;
                case "no":
                case "NO":
                    return FIT_STYLE.NO;
            }
        }
        return mDefaultStyle;
    }

    public static float getSize(int resId) {
        return getSize(resId, FIT_STYLE.X);
    }

    public static float getSize(int resId, FIT_STYLE style) {
        String res = context.getResources().getString(resId);
        return parseSize(res, style);
    }

    /**
     * BOTH,这种模式下，无论屏幕宽高比例是怎么样，都会刚好铺满，如果有wrap_content的控件则不可控制
     * X或者Y这种模式下，需要旋转适配X轴还是适配Y轴，可以完整的保持原控件比例
     */
    enum FIT_STYLE {
        X, Y, BOTH, NO
    }

    private enum TYPE {
        DP, PX, SP
    }

}
