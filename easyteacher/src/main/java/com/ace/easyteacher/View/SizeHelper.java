package com.ace.easyteacher.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ace.easyteacher.R;

/**
 * Created by Xman on 2016/3/15
 */
public class SizeHelper {


    private static final String TAG = "SizeHelper";
    private ViewGroup mHost;

    public SizeHelper(ViewGroup host) {
        mHost = host;

    }


    public void adjustChildren(int widthMeasureSpec, int heightMeasureSpec) {

        int widthHint = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightHint = View.MeasureSpec.getSize(heightMeasureSpec);

        Log.d(TAG, "widthHint = " + widthHint + " , heightHint = "
                + heightHint);

        for (int i = 0, N = mHost.getChildCount(); i < N; i++) {
            View view = mHost.getChildAt(i);

            ViewGroup.LayoutParams params = view.getLayoutParams();
            if (params instanceof XLayoutParams) {

                adjustParams(view, params);
            }
        }
    }

    private void adjustParams(View view, ViewGroup.LayoutParams params) {
        LayoutInfo info = ((XLayoutParams) params).getLayoutInfo();

        params.width = info.mWidth > 0 ? (int) info.mWidth : params.width;
        params.height = info.mHeight > 0 ? (int) info.mHeight : params.height;
        if (params instanceof ViewGroup.MarginLayoutParams) {
            if (info.mLeftMargin > 0) {
                ((ViewGroup.MarginLayoutParams) params).leftMargin = (int) (info.mLeftMargin);
            }
            if (info.mTopMargin > 0) {
                ((ViewGroup.MarginLayoutParams) params).topMargin = (int) (info.mTopMargin);
            }
            if (info.mRightMargin > 0) {
                ((ViewGroup.MarginLayoutParams) params).rightMargin = (int) (info.mRightMargin);
            }
            if (info.mBottomMargin > 0) {
                ((ViewGroup.MarginLayoutParams) params).bottomMargin = (int) (info.mBottomMargin);
            }
            if (info.mLeftMargin > 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                ((ViewGroup.MarginLayoutParams) params).setMarginStart((int) (info.mStartMargin ));
            }
            if (info.mEndMargin > 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                ((ViewGroup.MarginLayoutParams) params).setMarginEnd((int) (info.mEndMargin ));
            }
        }

        view.setPadding((int) (info.mLeftPadding ), (int) (info.mTopPadding ),
                (int) (info.mRightPadding ), (int) (info.mBottomPadding ));

        if (info.mStartPadding != 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            view.setPaddingRelative((int) (info.mStartPadding ), (int) (info.mTopPadding ),
                    (int) (info.mRightPadding ), (int) (info.mEndMargin ));

        }

        if (view instanceof TextView) {
            if (info.mTextSize > 0) {
                ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        info.mTextSize );
            } else if (info.mTextSize == 0) {
                info.mTextSize = ((TextView) view).getTextSize();
                ((TextView) view).setTextSize(info.mTextSize );
            }
        }
    }

    public static class LayoutInfo {


        public float mLeftMargin;

        public float mTopMargin;

        public float mRightMargin;

        public float mBottomMargin;

        public float mStartMargin;
        public float mEndMargin;

        public float mLeftPadding;

        public float mRightPadding;

        public float mTopPadding;

        public float mBottomPadding;

        public float mStartPadding;
        public float mEndPadding;


        public float mWidth;
        public float mHeight;

        public float mTextSize;

        public Calculator.FIT_STYLE fit_style;


        final static String MATERIALDESIGNXML = "http://schemas.android.com/apk/res-auto";
        final static String ANDROIDXML = "http://schemas.android.com/apk/res/android";

        public LayoutInfo(Context context, AttributeSet attrs) {
            TypedArray array = context.obtainStyledAttributes(attrs,
                    R.styleable.X_Layout);

            String style = attrs.getAttributeValue(MATERIALDESIGNXML, "fit_style");
            fit_style = Calculator.getFitStyle(style);

            mTextSize = parse(context, attrs, "textSize");
            mWidth = parse(context, attrs, "layout_width");
            mHeight = parse(context, attrs, "layout_height");
            mLeftMargin = parse(context, attrs, "layout_marginLeft");
            mRightMargin = parse(context, attrs, "layout_marginRight");
            mTopMargin = parse(context, attrs, "layout_marginTop");
            mBottomMargin = parse(context, attrs, "layout_marginBottom");
            mStartMargin = parse(context, attrs, "layout_marginStart");
            mEndMargin = parse(context, attrs, "layout_marginEnd");
            mLeftPadding = parse(context, attrs, "paddingLeft");
            mRightPadding = parse(context, attrs, "paddingRight");
            mTopPadding = parse(context, attrs, "paddingTop");
            mBottomPadding = parse(context, attrs, "paddingBottom");
            mStartPadding = parse(context, attrs, "paddingStart");
            mEndPadding = parse(context, attrs, "paddingEnd");

            array.recycle();
        }

        public float parse(Context context, AttributeSet attrs, String type) {
            String text;
            int textResource = attrs.getAttributeResourceValue(ANDROIDXML, type, -1);
            if (textResource != -1) {
                text = context.getResources().getString(textResource);
            } else {
                text = attrs.getAttributeValue(ANDROIDXML, type);
            }
            return Calculator.parseSize(text, fit_style);
        }
    }

    public interface XLayoutParams {
        LayoutInfo getLayoutInfo();
    }
}
