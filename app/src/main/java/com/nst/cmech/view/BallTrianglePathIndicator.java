package com.nst.cmech.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.nst.cmech.R;


/**
 * Created by luowenwen on 2015/12/4.
 */
public class BallTrianglePathIndicator extends View {

    public static final int DEFAULT_SIZE=45;

    //attrs
    int mIndicatorColor;

    Paint mPaint;

    private boolean mHasAnimation;

    float[] translateX=new float[3],translateY=new float[3];

    public BallTrianglePathIndicator(Context context) {
        super(context);
        init(null, 0);
    }

    public BallTrianglePathIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public BallTrianglePathIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }


    private void init(AttributeSet attrs, int defStyle) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.LineScaleIndicator);
        mIndicatorColor=a.getColor(R.styleable.LineScaleIndicator_indicatorColr, Color.WHITE);
        a.recycle();
        mPaint=new Paint();
        mPaint.setColor(mIndicatorColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width  = measureDimension(dp2px(DEFAULT_SIZE), widthMeasureSpec);
        int height = measureDimension(dp2px(DEFAULT_SIZE), heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    private int measureDimension(int defaultSize,int measureSpec){
        int result = defaultSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(defaultSize, specSize);
        } else {
            result = defaultSize;
        }
        return result;
    }

    @Override
    public void onDraw(Canvas canvas) {
        mPaint.setStrokeWidth(3);
//        mPaint.setStyle(Paint.Style.STROKE);
        for (int i = 0; i < 3; i++) {
            canvas.save();
            canvas.translate(translateX[i], translateY[i]);
            canvas.drawCircle(0, 0, getWidth() / 10, mPaint);
            canvas.restore();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (!mHasAnimation){
            mHasAnimation=true;
            applyAnimation();
        }
    }

    public void applyAnimation() {
        float startX=getWidth()/5;
        float startY=getWidth()/5;
        for (int i = 0; i < 3; i++) {
            final int index=i;
            ValueAnimator translateXAnim= ValueAnimator.ofFloat(getWidth()/2,getWidth()-startX,startX,getWidth()/2);
            if (i==1){
                translateXAnim= ValueAnimator.ofFloat(getWidth()-startX,startX,getWidth()/2,getWidth()-startX);
            }else if (i==2){
                translateXAnim= ValueAnimator.ofFloat(startX,getWidth()/2,getWidth()-startX,startX);
            }
            ValueAnimator translateYAnim= ValueAnimator.ofFloat(startY,getHeight()-startY,getHeight()-startY,startY);
            if (i==1){
                translateYAnim= ValueAnimator.ofFloat(getHeight()-startY,getHeight()-startY,startY,getHeight()-startY);
            }else if (i==2){
                translateYAnim= ValueAnimator.ofFloat(getHeight()-startY,startY,getHeight()-startY,getHeight()-startY);
            }

            translateXAnim.setDuration(2000);
            translateXAnim.setInterpolator(new LinearInterpolator());
            translateXAnim.setRepeatCount(-1);
            translateXAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    translateX [index]= (Float) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            translateXAnim.start();

            translateYAnim.setDuration(2000);
            translateYAnim.setInterpolator(new LinearInterpolator());
            translateYAnim.setRepeatCount(-1);
            translateYAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    translateY [index]= (Float) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            translateYAnim.start();
        }
    }

    private int dp2px(int dpValue) {
        return (int) getContext().getResources().getDisplayMetrics().density * dpValue;
    }
}
