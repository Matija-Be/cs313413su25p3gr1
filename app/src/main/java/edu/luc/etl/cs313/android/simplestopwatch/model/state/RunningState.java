package edu.luc.etl.cs313.android.simplestopwatch.model.state;

import edu.luc.etl.cs313.android.simplestopwatch.R;

class RunningState implements StopwatchState {

    public RunningState(final StopwatchSMStateView sm) {

        this.sm = sm;
    }

    private final StopwatchSMStateView sm;

    /*
    This is our methods for when we are working with start and stoping.
    Also lap and reset
    As well as when it reaches zero with onTick.
     */
    @Override
    public void onStartStop() {
        sm.actionStop();//Stops
        sm.actionReset();//Resets
        sm.toStoppedState();//The state in which it stops
    }

    @Override
    public void onLapReset() {
        sm.actionStop();//Stop
        sm.actionStopNotification();//Notification that it stopped.
        sm.actionReset();//Reset
        sm.toStoppedState();//State
    }

    @Override
    public void onTick() {
        sm.actionDec();
        if(sm.getTime() == 0){//Check when timer reaches zero.
            sm.actionStop();//Stops
            sm.actionPlayNotification();//Timer reaches zero
            sm.toStoppedState();//Stop State
        }
        else{
        sm.toRunningState();}//it runs/

    }

    @Override
    public void updateView() {
        sm.updateUIRuntime();
    }

    @Override
    public int getId() {
        return R.string.RUNNING;
    }
}
