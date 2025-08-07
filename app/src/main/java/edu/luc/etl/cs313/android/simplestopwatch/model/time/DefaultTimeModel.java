package edu.luc.etl.cs313.android.simplestopwatch.model.time;

import static edu.luc.etl.cs313.android.simplestopwatch.common.Constants.*;

/**
 * An implementation of the stopwatch data model.
 */
public class DefaultTimeModel implements TimeModel {
    public void setRuntime(int time) {
    Runtime = time = Math.min(time,99);
    }
    private int Runtime = 0;
    @Override
    public void resetRuntime() {
        Runtime = 0;
    }



    @Override
   public void setTime(final int time) {
        this.Runtime = Math.min(time,99);
    }

    @Override
    public void decTime() {
        if (Runtime > 0) {
            Runtime--;
        }
    }

    @Override
    public int getTime() {
        return Runtime;
    }
    public void incRuntime(){
            if(Runtime < 99) {
                Runtime++;
            }
    }

}