package edu.luc.etl.cs313.android.simplestopwatch.model;

import edu.luc.etl.cs313.android.simplestopwatch.common.Startable;
import edu.luc.etl.cs313.android.simplestopwatch.common.StopwatchModelSource;
import edu.luc.etl.cs313.android.simplestopwatch.common.StopwatchUIListener;

/**
 * A thin model facade. Following the Facade pattern,
 * this isolates the complexity of the model from its clients (usually the adapter).
 *
 * @author laufer
 */
public interface StopwatchModelFacade extends Startable, StopwatchUIListener, StopwatchModelSource {

    //called when the activity resumes
    void onResume();

    //called whe the activity is paused
    void onPause();

    //called when activity is fully stopped
    void onStop();

    //called when the activity is destroyed
    void onDestroy();
    void onAddTime();

}
