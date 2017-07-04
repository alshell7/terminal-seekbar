package com.projects.alshell.android;

import android.graphics.Color;
import android.graphics.Paint;

/*
 * Discrete plot of the TerminalSeekBar
 * Created by Alshell7 @(Ashraf Khan Workstation)
 * 01:14 PM.
 * 06/Jul/2016
 */
public class Terminal
{
    public static final int DEFAULT_TERMINAL_COLOR = Color.parseColor("#009688");
    public static final String DEFAULT_TERMINAL_INFO  = "Nothing";

    public static final int DEFAULT_PRIORITY_NORMAL = 900;
    public static final int DEFAULT_PRIORITY_MEDIATE = 600;
    public static final int DEFAULT_PRIORITY_HIGH = 250;

    private int mColor;
    private String mInformation;
    private int mPosition;
    private Paint mPaint;
    private int mPriority;


    /**
     *
     * @param TerminalPosition position of the terminal in the TerminalSeekBar
     */
    public Terminal(int TerminalPosition) {
        init(DEFAULT_TERMINAL_COLOR, TerminalPosition, DEFAULT_TERMINAL_INFO, DEFAULT_PRIORITY_NORMAL);
    }

    /**
     * @param TerminalPosition position of the terminal in the TerminalSeekBar
     * @param TerminalColor color of the Terminal
     */
    public Terminal( int TerminalPosition, int TerminalColor) {
        init(TerminalColor, TerminalPosition, DEFAULT_TERMINAL_INFO, DEFAULT_PRIORITY_NORMAL);
    }

    /**
     * @param TerminalPosition position of the terminal in the TerminalSeekBar
     * @param TerminalColor color of the Terminal
     * @param Priority status of the the Terminal
     */
    public Terminal( int TerminalPosition, int TerminalColor, int Priority) {
        init(TerminalColor, TerminalPosition, DEFAULT_TERMINAL_INFO, Priority);
    }

    /**
     *
     * @param TerminalPosition position of the terminal in the TerminalSeekBar
     * @param TerminalColor color of the Terminal
     * @param TerminalInformation Terminal describing brief information of its cause
     */
    public Terminal( int TerminalPosition, int TerminalColor, String TerminalInformation) {
        init(TerminalColor, TerminalPosition, TerminalInformation, DEFAULT_PRIORITY_NORMAL);
    }


    /**
     * @param TerminalPosition position of the terminal in the TerminalSeekBar
     * @param TerminalColor color of the Terminal
     * @param TerminalInformation Terminal describing brief information of its cause
     * @param Priority status of the the Terminal
     */
    public Terminal( int TerminalPosition, int TerminalColor, String TerminalInformation, int Priority) {
        init(TerminalColor, TerminalPosition, TerminalInformation, Priority);
    }

    private void init(int color, int position, String info, int priority) {
        this.mColor = color;
        this.mPosition = position;
        this.mInformation = info;
        this.mPriority = priority;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(color);
    }

    /**
     * @param mColor sets the color of the Terminal
     */
    public void setColor(int mColor)
    {
        this.mColor = mColor;
        mPaint.setColor(mColor);
    }

    /**
     * @param mPosition sets the position of the terminal in the TerminalSeekBar
     */
    public void setPosition(int mPosition)
    {
        this.mPosition = mPosition;
    }


    /**
     * @param mPriority sets the status of the the Terminal
     */
    public void setPriority(int mPriority)
    {
        this.mPriority = mPriority;
    }


    /**
     * @param mInformation sets the Terminal describing brief information of its cause
     */
    public void setInformation(String mInformation)
    {
        this.mInformation = mInformation;
    }

    protected Paint getPaint()
    {
        return mPaint;
    }

    /**
     * @return gets the color of the Terminal
     */
    public int getColor()
    {
        return mColor;
    }

    /**
     * @return gets the position of the terminal in the TerminalSeekBar
     */
    public int getPosition()
    {
        return mPosition;
    }


    /**
     * @return gets the status of the the Terminal
     */
    public int getPriority()
    {
        return mPriority;
    }


    /**
     * @return gets the Terminal describing brief information of its cause
     */
    public String getInformation()
    {
        return mInformation;
    }

}
