package edu.luc.etl.cs313.android.simplestopwatch.model.state;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import edu.luc.etl.cs313.android.simplestopwatch.common.StopwatchModelListener;
import edu.luc.etl.cs313.android.simplestopwatch.model.clock.ClockModel;
import edu.luc.etl.cs313.android.simplestopwatch.model.time.TimeModel;

import java.io.IOException;

/**
 * An implementation of the state machine for the stopwatch.
 *
 * @author laufer
 */
public class DefaultStopwatchStateMachine implements StopwatchStateMachine {
    public int getTime() {
        return timeModel.getTime();
    }
    public DefaultStopwatchStateMachine(final TimeModel timeModel, final ClockModel clockModel, final Context context) {
        this.timeModel = timeModel;
        this.clockModel = clockModel;
        this.context = context;
    }
    private final TimeModel timeModel;

    private final ClockModel clockModel;
    private MediaPlayer alarmPlayer;

    /**
     * The internal state of this adapter component. Required for the State pattern.
     */
    private StopwatchState state;

    protected void setState(final StopwatchState state) {
        this.state = state;
        listener.onStateUpdate(state.getId());
    }

    private StopwatchModelListener listener;

    @Override
    public void setModelListener(final StopwatchModelListener listener) {
        this.listener = listener;
    }
    public void setRuntime(int time) {
        timeModel.setRuntime(time);
    }
    // forward event uiUpdateListener methods to the current state
    // these must be synchronized because events can come from the
    // UI thread or the timer thread
    @Override public synchronized void onStartStop() { state.onStartStop(); }
    @Override public synchronized void onLapReset()  { state.onLapReset(); }
    @Override public synchronized void onTick()      { state.onTick(); }

    @Override public void updateUIRuntime() { listener.onTimeUpdate(timeModel.getTime()); }

    @Override
    public void updateUILaptime() {

    }

    @Override
    public void actionPlayNotification() {
        //plays the sound
        if (alarmPlayer == null) {
            try {
                final Uri defaultRingtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                alarmPlayer = new MediaPlayer(); // create and assign to alarmPlayer directly
                alarmPlayer.setAudioAttributes(new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build());
                alarmPlayer.setDataSource(context, defaultRingtoneUri);
                alarmPlayer.setLooping(true); // keep looping until manually stopped
                alarmPlayer.prepare();
                alarmPlayer.start();
            } catch (IOException e) {
                throw new RuntimeException("Error playing notification", e);
            }
        }
    }

    @Override
    public void actionStopNotification() {
        /// stops the sound
    if(alarmPlayer != null) {
        alarmPlayer.stop();
            alarmPlayer.release();
            alarmPlayer = null;
        }
    }



    // known states
    private final StopwatchState STOPPED     = new StoppedState(this);
    private final StopwatchState RUNNING     = new RunningState(this);
    private final StopwatchState LAP_RUNNING = new LapRunningState(this);
    private final StopwatchState LAP_STOPPED = new LapStoppedState(this);
    private final Context context;
    // transitions
    @Override public void toRunningState()    { setState(RUNNING); }
    @Override public void toStoppedState()    { setState(STOPPED); }
    @Override public void toLapRunningState() { setState(LAP_RUNNING); }
    @Override public void toLapStoppedState() { setState(LAP_STOPPED); }

    // actions
    public void actionDec() {
        //counts down the time
        timeModel.decTime();
        actionUpdateView();
    }
    @Override public void actionInit()       { toStoppedState(); actionReset(); }
    @Override public void actionReset()      { timeModel.resetRuntime(); actionUpdateView(); }
    @Override public void actionStart()      { clockModel.start(); }
    @Override public void actionStop()       { clockModel.stop(); }

    @Override
    public void actionLap() {

    }

    @Override public void actionInc()        { timeModel.setTime(timeModel.getTime() + 1);}
    @Override public void actionUpdateView() { state.updateView(); }
private final Handler handler = new Handler(Looper.getMainLooper());

    /**
     * This will make sure that when the time is greater then zero it will start the time.
     */
    private final Runnable delayRunnable = new Runnable() {
        public void run() {
            if (getTime() >0) {

                toRunningState();
                actionStart();
            }
        }
    };

    /**
     * This handles the delay before it runs.
     * It will wait 3 milliseconds and then it will run.
     */
    public void restartDelayTimer() {
    handler.removeCallbacks(delayRunnable);
    handler.postDelayed(delayRunnable,3000);
}

}
