package com.ace.easyteacher.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by Xman on 2016/3/17
 */
public class XFrameLayout extends FrameLayout {

    private final SizeHelper mHelper = new SizeHelper(this);


    public XFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mHelper.adjustChildren(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    public static class LayoutParams extends FrameLayout.LayoutParams implements SizeHelper.XLayoutParams {


        private SizeHelper.LayoutInfo info;

        public LayoutParams(Context context, AttributeSet attrs) {
            super(context, attrs);
            info = new SizeHelper.LayoutInfo(context,attrs);

        }

        @Override
        protected void setBaseAttributes(TypedArray a, int widthAttr, int heightAttr) {
            super.setBaseAttributes(a, widthAttr, heightAttr);
        }

        @Override
        public SizeHelper.LayoutInfo getLayoutInfo() {
            return info;
        }

    }
}
