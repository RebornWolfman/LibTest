package com.wanjian.sak.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by wanjian on 2016/11/10.
 */

public class CornerMeasureView extends View {
    public CornerMeasureView(Context context) {
        super(context);
        init();
    }

    public CornerMeasureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CornerMeasureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("NewApi")
    public CornerMeasureView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int twoDP;
    private int fourDP;
    private int sixDP;
    private int eightDP;
    private int tenDP;

    private int gap;

    private int textSize;

    private void init() {
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(1);
        twoDP = dp2px(2);
        fourDP = dp2px(4);
        sixDP = dp2px(6);
        eightDP = dp2px(8);
        tenDP = dp2px(10);
        gap = dp2px(8);
        textSize = dp2px(6);
        mPaint.setTextSize(textSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int w = getWidth();
        int h = getHeight();
        mPaint.setStyle(Paint.Style.STROKE);
        drawRound(canvas, 0, 0, w, h, twoDP);
        drawRound(canvas, gap * 1, gap * 1, w - gap * 1, h - gap * 1, fourDP);
        drawRound(canvas, gap * 2, gap * 2, w - gap * 2, h - gap * 2, sixDP);
        drawRound(canvas, gap * 3, gap * 3, w - gap * 3, h - gap * 3, eightDP);
        drawRound(canvas, gap * 4, gap * 4, w - gap * 4, h - gap * 4, tenDP);


        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawText("2", w / 2, 0, mPaint);
        canvas.drawText("4", w / 2, gap * 1, mPaint);
        canvas.drawText("6", w / 2, gap * 2, mPaint);
        canvas.drawText("8", w / 2, gap * 3, mPaint);
        canvas.drawText("10", w / 2, gap * 4, mPaint);

    }

    private void drawRound(Canvas canvas, int l, int t, int r, int b, int corner) {

        Path path = new Path();
        path.arcTo(new RectF(l, t, l + corner * 2, t + corner * 2), 180, 90);

        path.arcTo(new RectF(r - corner * 2, t, r, t + corner * 2), -90, 90);

        path.arcTo(new RectF(r - corner * 2, b - corner * 2, r, b), 0, 90);

        path.arcTo(new RectF(l, b - corner * 2, l + corner * 2, b), 90, 90);

        path.close();
        canvas.drawPath(path, mPaint);

    }


    private int dp2px(int dip) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5);
    }
}
