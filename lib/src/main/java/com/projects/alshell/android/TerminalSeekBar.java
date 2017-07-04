package com.projects.alshell.android;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Alshell7 @(Ashraf Khan Workstation)
 * 09:48 PM.
 * 05/Jul/2016
 */
public class TerminalSeekBar extends View
{

    public static final int DEFAULT_BACKGROUND_COLOR  = Color.TRANSPARENT;
    public static final int DEFAULT_HEIGHT_THUMB  = 20;
    public static final int DEFAULT_HEIGHT_BAR  = 3;
    public static final int DEFAULT_MARGIN_BAR  = 5;
    public static final int DEFAULT_MAX_VALUE  = 100;
    public static final float TERMINAL_BAR_PADDING = 10f;

    private int mBackgroundColor = DEFAULT_BACKGROUND_COLOR;
    private int mBarColor = Color.BLACK;
    private int mPaddingSize;
    private int mViewWidth;
    private int mViewHeight;

    private LinearGradient mSeekBarGradient;
    private Paint mBarRectPaint;

    private TerminalChangedListener mTerminalChangedListener;
    private SeekBarValueChangedListener mSeekBarValueChangedListener;

    private Context mContext;
    private Bitmap mTransparentBitmap;
    private Rect mSeekBarRect;

    private boolean isBarMoving;
    private int thumbHeight = DEFAULT_HEIGHT_THUMB;
    private int barHeight = DEFAULT_HEIGHT_BAR;
    private int barWidth;
    private int mMaxValue;
    private int barCurrentValue;
    private float thumbRadius;
    private int barMargin = DEFAULT_MARGIN_BAR;

    private int absLeft;
    private int absRight;
    private int absTop;
    private int absBottom  ;

    private float terminalRadius;
    private ArrayList<Terminal> mTerminals = null;
    private ArrayList<TerminalBackDrawable> terminalBackDrawables = null;

    private boolean isBlinkActiveAnimEnabled = false;
    Handler mHandler = new Handler(Looper.getMainLooper());
    Timer timerUpdateFrame;

    class BlinkActiveRepeatTask extends TimerTask {
        @Override
        public void run()
        {

            mHandler.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    invalidate();
                }
            }, 0);
        }
    }

    public TerminalSeekBar(Context context) {
        super(context);
        initialize(context, null, 0, 0);
    }

    public TerminalSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0, 0);
    }

    public TerminalSeekBar(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TerminalSeekBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * getting the fields initialized ...
     */
    private void initialize() {

        absLeft = getPaddingLeft() + mPaddingSize;
        absRight = getWidth() - getPaddingRight() - mPaddingSize;
        absTop = getPaddingTop() + mPaddingSize;
        absBottom = getHeight() - getPaddingBottom() - mPaddingSize;

        thumbRadius = thumbHeight / 2;
        mPaddingSize = (int) thumbRadius;
        barWidth = absRight - absLeft;

        mSeekBarRect = new Rect(absLeft, absTop, absRight, absTop + barHeight);

        mSeekBarGradient = new LinearGradient(0, 0, mSeekBarRect.width(), 0, mBarColor, mBarColor, Shader.TileMode.MIRROR);
        mBarRectPaint = new Paint();
        mBarRectPaint.setShader(mSeekBarGradient);
        mBarRectPaint.setAntiAlias(true);

        terminalRadius = barHeight / 2 + 5;

    }

    protected void initialize(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
        applyStyle(context, attrs, defStyleAttr, defStyleRes);
    }


    protected void applyStyle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
        mContext = context;

        TypedArray barAttributes = context.obtainStyledAttributes(attrs, R.styleable.TerminalSeekBar, defStyleAttr, defStyleRes);
        mBarColor = barAttributes.getColor(R.styleable.TerminalSeekBar_bar_color, Color.BLACK);
        mMaxValue = barAttributes.getInteger(R.styleable.TerminalSeekBar_max_value, DEFAULT_MAX_VALUE);
        barCurrentValue = barAttributes.getInteger(R.styleable.TerminalSeekBar_current_value, 0);
        mBackgroundColor = barAttributes.getColor(R.styleable.TerminalSeekBar_background_color, Color.TRANSPARENT);
        barHeight = (int)barAttributes.getDimension(R.styleable.TerminalSeekBar_bar_height, (float) dp2px(DEFAULT_HEIGHT_BAR, mContext));
        thumbHeight = (int)barAttributes.getDimension(R.styleable.TerminalSeekBar_thumb_height, (float) dp2px(DEFAULT_HEIGHT_THUMB, mContext));
        barMargin = (int)barAttributes.getDimension(R.styleable.TerminalSeekBar_bar_margin, (float) dp2px(DEFAULT_MARGIN_BAR, mContext));
        barAttributes.recycle();

        setBackgroundColor(mBackgroundColor);
        initialize();

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewWidth = widthMeasureSpec;
        mViewHeight = heightMeasureSpec;

        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        if(specMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(mViewWidth, thumbHeight + barHeight);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTransparentBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);
        mTransparentBitmap.eraseColor(Color.TRANSPARENT);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        initialize();
        float colorPosition = (float) barCurrentValue / mMaxValue * barWidth;

        Paint colorPaint = new Paint();
        colorPaint.setAntiAlias(true);
        colorPaint.setColor(mBarColor);

        //required for the thumb (inner radiant to be transparent when the seek bar thumb is on the terminal)
        int[] toAlpha = new int[]{Color.argb(0, 0, 0, 0),Color.argb(255, 0, 0, 0),Color.argb(0, 0, 0, 0)};

        //refreshing the background
        canvas.drawBitmap(mTransparentBitmap,0,0,null);

        //drawing the seek bar
        canvas.drawRect(mSeekBarRect, mBarRectPaint);

        //drawing the seek bar thumb
        float thumbX = colorPosition + absLeft;
        float thumbY = mSeekBarRect.top + mSeekBarRect.height() / 2;
        canvas.drawCircle(thumbX, thumbY, barHeight / 2 + 5, colorPaint);


        Paint terminalColor = new Paint();
        terminalColor.setAntiAlias(true);
        terminalColor.setColor(Color.RED);

        if (mTerminals != null)
        {
            int i = 0;
            //drawing all the terminals
            for (Terminal terminal : mTerminals) {

                float terminalPosX = getTerminalAbsoluteX(terminal.getPosition());
                float terminalPosY = mSeekBarRect.top + mSeekBarRect.height() / 2;

                //drawing the outer blinking glow of the terminal
                if (terminalBackDrawables !=null) {
                    terminalBackDrawables.get(i).setCx(terminalPosX);
                    terminalBackDrawables.get(i).setCy(terminalPosY);
                    terminalBackDrawables.get(i).setRadius(terminalRadius + TERMINAL_BAR_PADDING);
                    terminalBackDrawables.get(i).setColor(terminal.getColor());
                    //logout("Color on the view from terminal " + terminal.getColor());
                    terminalBackDrawables.get(i).draw(canvas);
                    i++;
                }
                ////drawing the clear Terminal
                canvas.drawCircle(terminalPosX, terminalPosY, terminalRadius, terminal.getPaint());
            }
        }


        RadialGradient thumbShader  = new RadialGradient(thumbX, thumbY, thumbRadius, toAlpha, null, Shader.TileMode.MIRROR);
        Paint thumbGradientPaint = new Paint();
        thumbGradientPaint.setAntiAlias(true);
        thumbGradientPaint.setShader(thumbShader);

        //drawing the seek bar thumb
        canvas.drawCircle(thumbX, thumbY, (thumbHeight / 2), thumbGradientPaint);

        super.onDraw(canvas);
        animateBlink();
    }

    private void animateBlink()
    {
        if (isBlinkActiveAnimEnabled && terminalBackDrawables != null) {
            if(timerUpdateFrame != null){
                timerUpdateFrame.cancel();
            }
            timerUpdateFrame = new Timer();
            //animatorActiveTask = new AnimatorRepeatTask();
            timerUpdateFrame.schedule(new BlinkActiveRepeatTask(), 50);
        }
    }


    /**
     * Configure the terminals that has to be plotted over the TerminalSeekBar
     * @param terminals list of all Terminals to be plotted
     * <p>(Make sure the position of the Terminal is within the bounds of TerminalSeekBar's Max Value)
     */
    public void setTerminals(ArrayList<Terminal> terminals)
    {
        this.mTerminals = terminals;
        terminalBackDrawables = new ArrayList<>();

        for(Terminal terminal: terminals)
        {
            if ((terminal.getPosition() > mMaxValue) || terminal.getPosition() < 0) {
                throw new IllegalArgumentException("Terminal position is out of bounds : " + terminal.getPosition());
            }
            TerminalBackDrawable terminalBackDrawable = new TerminalBackDrawable(terminal.getColor(), terminalRadius + 5f, terminal.getPriority());
            terminalBackDrawable.setPlayInterpolator(new AccelerateDecelerateInterpolator());
            terminalBackDrawables.add(terminalBackDrawable);
            //Log.i("TerminalSeekBar", "setTerminals : Terminal Id" + terminal.getId());

        }
        enablePriorityBlinking(TerminalAnimationType.BLINK_STILL);
    }


    /**
     * Remove all the terminals that were loaded to plot on the TerminalSeekBar
     */
    public void clearTerminals()
    {
        disablePriorityBlinking(TerminalAnimationType.BLINK_STILL);
        disablePriorityBlinking(TerminalAnimationType.BLINK_ACTIVE);
        mTerminals = null;
        terminalBackDrawables = null;
    }

    /**
     * Enable the outer radial blinking of the terminals which indicate the priorities
     */
    public void enablePriorityBlinking(TerminalAnimationType animType)
    {
        if (terminalBackDrawables !=null) {
            for(TerminalBackDrawable terminalBackDrawable : terminalBackDrawables){
                terminalBackDrawable.startAnimation();
            }
            if (animType == TerminalAnimationType.BLINK_STILL){
                isBlinkActiveAnimEnabled = false;
            } else {
                isBlinkActiveAnimEnabled = true;
            }

        }
    }

    /**
     * Disable the blinking of the terminals which imdicate the priorities
     */
    public void disablePriorityBlinking(TerminalAnimationType animType)
    {
        if (terminalBackDrawables !=null) {
            if (animType == TerminalAnimationType.BLINK_STILL) {
                for(TerminalBackDrawable terminalBackDrawable : terminalBackDrawables){
                    terminalBackDrawable.stopAnimation();
                }
            } else {
                isBlinkActiveAnimEnabled = false;
            }

        }
    }

    /**
     * Sets a listener to receive notifications of changes to the Terminal's of the TerminalSeekBar
     * Also provides notifications of when the user starts and stops a touch gesture within the TerminalSeekBar.
     * @param  terminalChangedListener  The TerminalSeekBar notification listener
     */
    public void setTerminalChangedListener(TerminalChangedListener terminalChangedListener){
        this.mTerminalChangedListener = terminalChangedListener;
    }

    /**
     * Sets a listener to receive notifications of changes to the Values of the TerminalSeekBar
     * Also provides notifications of when the user starts and stops a touch gesture within the TerminalSeekBar.
     * @param  seekBarValueChangedListener  The TerminalSeekBar notification listener
     */
    public void setSeekBarValueChangedListener(SeekBarValueChangedListener seekBarValueChangedListener){
        this.mSeekBarValueChangedListener = seekBarValueChangedListener;
    }


    /**
     * Change the Thumb and the Line components of the TerminalSeekBar
     * @param colors color of the TerminalSeekBar to be changed to
     */
    public void setBarColor(int colors){
        mBarColor = colors;
        invalidate();
    }


    /**
     * Set the range of the progress bar to 0...max. Default is 100.
     * @param mMaxValue the upper range of this seek bar
     */
    public void setMaxValue(int mMaxValue)
    {
        this.mMaxValue = mMaxValue;
    }


    /**
     * Set the current progress to the specified value.
     * @param value the new progress, between 0 and {@link #getMaxValue()}
     */
    public void setProgress(int value) {
        barCurrentValue = value;
        if (mSeekBarValueChangedListener != null) {
            mSeekBarValueChangedListener.onValueChanged(barCurrentValue, false);
        }
        callListener();
        invalidate();
    }

    /**
     * Get the progress bar's current level of progress.
     * @return @return the current progress, between 0 and {@link #getMaxValue()}
     */
    public synchronized int getProgress()
    {
        return barCurrentValue;
    }

    /**
     * Return the upper limit of this seek bar's range.
     * @return a positive integer
     */
    public int getMaxValue()
    {
        return mMaxValue;
    }

    @Override
    public synchronized boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        float value = (x - absLeft) / barWidth * mMaxValue;

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(isThumbOnBar(mSeekBarRect, x, y)){
                    isBarMoving = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                getParent().requestDisallowInterceptTouchEvent(true);
                if(isBarMoving){

                    barCurrentValue = (int) value ;
                    if (barCurrentValue < 0) barCurrentValue = 0;
                    if (barCurrentValue > mMaxValue) barCurrentValue = mMaxValue;
                    if (mSeekBarValueChangedListener!=null) {
                        mSeekBarValueChangedListener.onValueChanged(barCurrentValue, true);
                    }
                }

                callListener();

                //Log.i("TerminalSeekBar", "Current seek bar value : " + barCurrentValue);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                isBarMoving = false;
                if ((Math.abs(event.getDownTime() - event.getEventTime())) < 100l){
                    barCurrentValue = (int) value ;
                    if (barCurrentValue < 0) barCurrentValue = 0;
                    if (barCurrentValue > mMaxValue) barCurrentValue = mMaxValue;
                    if (mSeekBarValueChangedListener !=null) {
                        mSeekBarValueChangedListener.onValueChanged(barCurrentValue, false);
                    }
                    callListener();

                    invalidate();
                }
                break;

        }
        return true;
    }

    private synchronized void callListener(){
        if(mTerminalChangedListener != null) {
            synchronized (TerminalSeekBar.this) {
                for(Terminal terminal : mTerminals){
                    if (terminal.getPosition() == barCurrentValue){
                        mTerminalChangedListener.onTerminalChanged(new Terminal(barCurrentValue, terminal.getColor(), terminal.getInformation(), terminal.getPriority()));
                    }
                }

            }
        }
    }
    private float getTerminalAbsoluteX(int x) {
        float point = (float) x / mMaxValue * barWidth;
        return point + absLeft;
    }

    private boolean isThumbOnBar(Rect r, float x, float y){
        if(r.left - thumbRadius < x && x < r.right + thumbRadius && r.top - thumbRadius < y && y < r.bottom + thumbRadius){
            return true;
        }else{
            return false;
        }
    }

    private int dp2px(float dpValue, Context context)
    {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
