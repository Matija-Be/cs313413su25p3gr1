package edu.luc.etl.cs313.android.simplestopwatch.model.state;

import edu.luc.etl.cs313.android.simplestopwatch.R;

class StoppedState implements StopwatchState {

    public StoppedState(final StopwatchSMStateView sm) {
        this.sm = sm;
    }

    private final StopwatchSMStateView sm;

    @Override
    public void onStartStop() {

        sm.actionInc();               // <-- this adds 1 second
        sm.updateUIRuntime();
        sm.restartDelayTimer();
    }

    @Override
    public void onLapReset() {
      sm.actionStop();//stop count
      sm.actionStopNotification();//stop sound
        sm.actionReset();//reset time
        sm.toStoppedState();//stay stopped
    }

    @Override
    public void onTick() {
    //do nothing
    }

    @Override
    public void updateView() {
        sm.updateUIRuntime();
    }

    @Override
    public int getId() {
        return R.string.STOPPED;
    }
}
