package com.chinafeisite.tianbu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Admin on 2016/7/16.
 */
public class DownLoadCircleView extends View {


    //圆点XY坐标
    private int centreX;
    private int centreY;

    //半径
    private int raius;

    //进度圆画笔
    private Paint mPaint;

    //内部圆画笔
    private Paint mCirclePaint;

    //内部进度画笔
    private Paint mTextPaint;


    private int progress;

    public DownLoadCircleView(Context context) {
        this(context, null);
    }

    public DownLoadCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);

        raius = 20;
        centreX = 50;
        centreY = 50;


        init();
    }

    private void init() {

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4);
        mPaint.setColor(Color.parseColor("#7CDFF7"));


        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(Color.parseColor("#303741"));

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(20);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? width : 0, heightMode == MeasureSpec.EXACTLY ? height : 0);


        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(centreX +getPaddingLeft(), centreY , raius - 2, mCirclePaint);


        RectF rectF = new RectF();
        rectF.left = centreX - raius + getPaddingLeft();
        rectF.top =  centreY - raius;
        rectF.right = rectF.left + raius * 2;
        rectF.bottom = rectF.top + raius * 2;



        canvas.drawArc(rectF, -90, ((float)progress/100)*360, false, mPaint);
        canvas.drawText(progress + "%", centreX - mTextPaint.measureText(progress + "%") / 2 + getPaddingLeft(), centreY + 20/2, mTextPaint);

    }

    public void setProgress(int progress) {

        this.progress = progress;
        invalidate();

    }


}