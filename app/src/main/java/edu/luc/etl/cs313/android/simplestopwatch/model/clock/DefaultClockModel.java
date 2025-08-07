package edu.luc.etl.cs313.android.simplestopwatch.model.clock;

import android.os.Handler;
import android.os.Looper;

import java.util.Timer;
import java.util.TimerTask;

/**
 * An implementation of the internal clock.
 *
 * @author laufer
 */
public class DefaultClockModel implements ClockModel {

    // TODO make accurate by keeping track of partial seconds when canceled etc.

    private TickListener listener;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable tickRunnable = new Runnable(){
        @Override
        public void run() {
            if (listener != null) {
                listener.onTick();
                handler.postDelayed(this, 1000);
            }

        }
    };
    @Override
    public void setTickListener(final TickListener listener) {
        this.listener = listener;
    }

    @Override
    public void start() {
        handler.removeCallbacks(tickRunnable);
        handler.post(tickRunnable);
        }

        // The clock model runs onTick every 1000 milliseconds


        @Override
        public void stop () {
        handler.removeCallbacks(tickRunnable);
    }

    }
