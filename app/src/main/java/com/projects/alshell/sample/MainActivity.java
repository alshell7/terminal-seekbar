package com.projects.alshell.sample;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;

import com.projects.alshell.android.SeekBarValueChangedListener;
import com.projects.alshell.android.Terminal;
import com.projects.alshell.android.TerminalAnimationType;
import com.projects.alshell.android.TerminalChangedListener;
import com.projects.alshell.android.TerminalSeekBar;
import com.projects.alshell.terminalseekbar.R;
import java.util.ArrayList;

/**
 * Sample application that demonstrates the use of TerminalSeekBar.
 */
public class MainActivity extends Activity
    implements TerminalChangedListener, SeekBarValueChangedListener
{
    TerminalSeekBar terminalSeekBar;
    TextView textViewTerminalChanged;
    TextView textViewSeekChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        terminalSeekBar = (TerminalSeekBar) findViewById(R.id.terminalSeekBar);
        textViewTerminalChanged = (TextView) findViewById(R.id.textView);
        textViewSeekChanged = (TextView) findViewById(R.id.positionSeekTextView);

        ArrayList<Terminal> terminals = new ArrayList<>();

        //Note that an exception is thrown if TerminalPosition is greater than the setMaxValue of TerminalSeekBar
        terminals.add(new Terminal(10, Color.GREEN, "...this..." , Terminal.DEFAULT_PRIORITY_NORMAL));
        terminals.add(new Terminal(100, Color.RED, "...HIGH...", Terminal.DEFAULT_PRIORITY_HIGH));
        terminals.add(new Terminal(25, Color.BLUE, "...is...", Terminal.DEFAULT_PRIORITY_NORMAL));
        terminals.add(new Terminal(70, Color.YELLOW, "...gettING...", Terminal.DEFAULT_PRIORITY_MEDIATE));
        terminals.add(new Terminal(50, Color.GRAY, "...check...", Terminal.DEFAULT_PRIORITY_HIGH));

        terminalSeekBar.setTerminals(terminals);

        terminalSeekBar.enablePriorityBlinking(TerminalAnimationType.BLINK_ACTIVE);

        //Or
        //terminalSeekBar.enablePriorityBlinking(TerminalAnimationType.BLINK_STILL);

        terminalSeekBar.setTerminalChangedListener(this);
        terminalSeekBar.setSeekBarValueChangedListener(this); //Optional

    }

    @Override
    public void onTerminalChanged(Terminal terminal)
    {
        textViewTerminalChanged.setTextColor(terminal.getColor());
        CharSequence sequence = terminal.getInformation() + terminal.getPosition();
        textViewTerminalChanged.setText(sequence);
    }


    @Override
    public void onValueChanged(int position, boolean thumbPressed)
    {
        CharSequence sequence  = "Current seek position : " + position;
        textViewSeekChanged.setText(sequence);
    }

    int i;
    public void simulate(View view){
        Thread seeker = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                for( i = 0 ; i<= 100; i++){
                    MainActivity.this.runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            terminalSeekBar.setProgress(i);
                        }
                    });
                    SystemClock.sleep(20);
                }
            }
        });
        seeker.start();
    }


}
