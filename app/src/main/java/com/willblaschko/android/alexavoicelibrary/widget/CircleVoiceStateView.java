package com.willblaschko.android.alexavoicelibrary.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.willblaschko.android.alexavoicelibrary.R;


public class CircleVoiceStateView extends View {

    private final static int COLOR_CYAN = Color.CYAN;
    private final static int COLOR_BLUE = Color.BLUE;
    private final static int COLOR_RED = Color.RED;
    private final static int COLOR_YELLOW = Color.YELLOW;

    private int mCenterX;
    private int mCenterY;

    private int[] colors = new int[]{COLOR_CYAN, COLOR_BLUE, COLOR_BLUE, COLOR_CYAN};
    private Paint mPaint;

    private float mAnimVal;

    private State mCurrentState = State.IDLE;
    private ValueAnimator mValueAnimator;
    private Bitmap mIcon;

    public enum State {
        IDLE,
        LISTENING,
        ACTIVE_LISTENING,
        THINKING,
        SPEAKING,
        MICRO_OFF,
        SYSTEM_ERR
    }

    public CircleVoiceStateView(Context context) {
        this(context, null);
    }

    public CircleVoiceStateView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleVoiceStateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        setupAnimation();

        /*setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = mCurrentState.ordinal();
                index++;
                if (index >= State.values().length) {
                    index = 0;
                }
                setCurrentState(State.values()[index]);
            }
        });*/
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w / 2;
        mCenterY = h / 2;
    }

    private void init() {
        mIcon = BitmapFactory.decodeResource(getResources(), R.drawable.alexa_logo);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(6);
    }

    public void setCurrentState(State state) {
        mCurrentState = state;
        stopAnim();
        setupAnimation();
        if (mValueAnimator != null) {
            mValueAnimator.start();
        }
    }

    private void setupAnimation() {
        switch (mCurrentState) {
            case LISTENING:
                mAnimVal = 0.33F;
                invalidate();
                break;
            case ACTIVE_LISTENING:
                mValueAnimator = ValueAnimator.ofFloat(0.33F, 0.4F);
                mValueAnimator.setInterpolator(new DecelerateInterpolator());
                mValueAnimator.setDuration(300);
                mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
                mValueAnimator.setRepeatMode(ValueAnimator.REVERSE);
                mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        mAnimVal = (float) valueAnimator.getAnimatedValue();
                        invalidate();
                    }
                });
                break;
            case THINKING:
                mValueAnimator = ValueAnimator.ofFloat(0, 0.3F);
                mValueAnimator.setDuration(800);
                mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
                mValueAnimator.setRepeatMode(ValueAnimator.RESTART);
                mValueAnimator.setInterpolator(new DecelerateInterpolator());
                mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        mAnimVal = (float) valueAnimator.getAnimatedValue();
                        invalidate();
                    }
                });
                break;
            case SPEAKING:
                mValueAnimator = ValueAnimator.ofFloat(0.5F, 1);
                mValueAnimator.setDuration(600);
                mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
                mValueAnimator.setRepeatMode(ValueAnimator.REVERSE);
                mValueAnimator.setInterpolator(new DecelerateInterpolator());
                mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        mAnimVal = (float) valueAnimator.getAnimatedValue();

                        invalidate();
                    }
                });
                break;
            case MICRO_OFF:
            case SYSTEM_ERR:
                mValueAnimator = ValueAnimator.ofFloat(0.5F, 1);
                mValueAnimator.setDuration(300);
                mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        mAnimVal = (float) animation.getAnimatedValue();
                        invalidate();
                    }
                });
                break;
            default:
                invalidate();
                mValueAnimator = null;
                break;
        }

    }

    public void stopAnim() {
        if (mValueAnimator != null) {
            mValueAnimator.cancel();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(mIcon, mCenterX - mIcon.getWidth() / 2, mCenterY - mIcon.getHeight() / 2, null);

        switch (mCurrentState) {
            case LISTENING:
            case ACTIVE_LISTENING:
            case THINKING:
                mPaint.setAlpha(255);
                canvas.rotate(-90, mCenterX, mCenterY);
                SweepGradient sweepGradient = new SweepGradient(mCenterX, mCenterY, colors, new float[]{0.08F, 0.5F - mAnimVal, 0.5F + mAnimVal, 0.92F});
                mPaint.setShader(sweepGradient);
                canvas.drawCircle(mCenterX, mCenterY, Math.min(getWidth(), getHeight()) / 2 - 50, mPaint);
                canvas.rotate(90, mCenterX, mCenterY);
                break;
            case SPEAKING:
            case MICRO_OFF:
            case SYSTEM_ERR:
                if (mCurrentState == State.SPEAKING) {
                    mPaint.setColor(COLOR_CYAN);
                } else if (mCurrentState == State.MICRO_OFF) {
                    mPaint.setColor(COLOR_YELLOW);
                } else {
                    mPaint.setColor(COLOR_RED);
                }
                mPaint.setAlpha((int) (255 * mAnimVal));
                mPaint.setShader(null);
                canvas.drawCircle(mCenterX, mCenterY, Math.min(getWidth(), getHeight()) / 2 - 50, mPaint);
                break;
            default:
                break;
        }
    }
}
