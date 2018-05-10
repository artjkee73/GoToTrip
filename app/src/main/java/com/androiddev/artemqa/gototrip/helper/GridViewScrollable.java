package com.androiddev.artemqa.gototrip.helper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

public class GridViewScrollable extends GridView {
    public GridViewScrollable(Context context) {
        super(context);
    }

    public GridViewScrollable(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridViewScrollable(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Max Height
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);


        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
