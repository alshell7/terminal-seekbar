package com.projects.alshell.android;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.view.animation.Animation;
import android.view.animation.Interpolator;

/**
 * Created by Alshell7 @(Ashraf Khan Workstation)
 * 07:17 AM.
 * 06/Jul/2016
 */
public class TerminalBackDrawable extends Drawable
{

    private Paint terminalPaint;
    private int color;
    private float radius;
    private long priority = Terminal.DEFAULT_PRIORITY_NORMAL;
    protected float blinkScale;
    protected int alpha;

    private float cx;
    private float cy;

    private Interpolator playInterpolator;
    private Interpolator alphaInterpolator;

    private Animator animator;
    private AnimatorSet animatorSet;

    public TerminalBackDrawable(int color, float radius, long priority) {
        this(color, radius);
        this.priority = priority;
    }

    public TerminalBackDrawable(int color, float radius) {
        this.color = color;
        this.radius = radius;
        this.blinkScale = 0f;
        this.alpha = 255;

        terminalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        animatorSet = new AnimatorSet();

    }

    @Override
    public void draw(Canvas canvas) {

        terminalPaint.setStyle(Paint.Style.FILL);
        terminalPaint.setColor(color);
        terminalPaint.setAlpha(alpha);
        //int[] toAlpha = new int[]{Color.argb(255, Color.red(255), Color.green(255), Color.blue(255)), Color.argb(0, Color.red(0), Color.green(0), Color.blue(0))};
        int[] toAlpha = new int[]{Color.argb(255, 255, 255, 225),Color.argb(0, 0, 0, 0)};
        RadialGradient shader = new RadialGradient(cx, cy,  radius, toAlpha, null, Shader.TileMode.MIRROR);
        terminalPaint.setShader(shader);
        terminalPaint.setStrokeJoin(Paint.Join.ROUND);
        terminalPaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawCircle(cx, cy, radius, terminalPaint);
    }

    public void setColor(int color)
    {
        this.color = color;
    }

    public void setCx(float cx)
    {
        this.cx = cx;
    }

    public void setCy(float cy)
    {
        this.cy = cy;
    }

    public void setRadius(float radius)
    {
        this.radius = radius;
    }

    public void setPlayInterpolator(Interpolator interpolator) {
        this.playInterpolator = interpolator;
    }


    public void setAlphaInterpolator(Interpolator interpolator) {
        this.alphaInterpolator = interpolator;
    }

    public void startAnimation() {
        animator = generateAnimation();
        animator.start();
    }

    public void stopAnimation() {
        if (animator.isRunning()) {
            animator.end();
        }
    }

    public boolean isAnimationRunning() {
        if (animator != null) {
            return animator.isRunning();
        }
        return false;
    }

    @Override
    public void setAlpha(int alpha) {
        this.alpha = alpha;
        invalidateSelf();
    }

    public void setPriority(long priority)
    {
        this.priority = priority;
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        terminalPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return terminalPaint.getAlpha();
    }

    protected void setBlinkScale(float blinkScale) {
        this.blinkScale = blinkScale;
        invalidateSelf();
    }

    protected float getBlinkScale() {
        return blinkScale;
    }

    private Animator generateAnimation() {

        ObjectAnimator blink = ObjectAnimator.ofFloat(this, "waveScale", 0f, 1f);
        blink.setDuration(priority);
        if (playInterpolator != null) {
            blink.setInterpolator(playInterpolator);
        }

        blink.setRepeatCount(Animation.INFINITE);
        blink.setRepeatMode(Animation.INFINITE);

        ObjectAnimator alphaAnimator = ObjectAnimator.ofInt(this, "alpha", 255, 0);
        alphaAnimator.setDuration(priority);
        if (alphaInterpolator != null) {
            alphaAnimator.setInterpolator(alphaInterpolator);
        }
        alphaAnimator.setRepeatCount(Animation.INFINITE);
        alphaAnimator.setRepeatMode(Animation.INFINITE);

        animatorSet.playTogether(blink, alphaAnimator);

        return animatorSet;
    }

}
