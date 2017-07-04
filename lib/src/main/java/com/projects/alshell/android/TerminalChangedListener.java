package com.projects.alshell.android;

/**
 * <p> Called when the Terminal is shifted from one to another
 * Created by Alshell7 @(Ashraf Khan Workstation)
 * 01:17 PM.
 * 06/Jul/2016
 */
public interface TerminalChangedListener
{
    /**
     * @param terminal terminal that just got triggered on the seek bar
     */
     public void onTerminalChanged(final Terminal terminal);
}
