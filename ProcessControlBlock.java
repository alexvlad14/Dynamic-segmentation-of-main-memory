import java.util.ArrayList;

public class ProcessControlBlock {
    
    private final int pid;
    private ProcessState state;
    // the following two ArrayLists should record when the process starts/stops
    // for statistical purposes
    private ArrayList<Integer> startTimes; // when the process starts running
    private ArrayList<Integer> stopTimes;  // when the process stops running

    private static int pidTotal= 0;
    
    public ProcessControlBlock() {
        this.state = ProcessState.NEW;
        this.startTimes = new ArrayList<Integer>();
        this.stopTimes = new ArrayList<Integer>();
        /* TODO: you need to add some code here
         * Hint: every process should get a unique PID */
        this.pid = ++pidTotal; ///

    }

    public ProcessState getState() {
        return this.state;
    }
    
    public void setState(ProcessState state, int currentClockTime) {
        /* TODO: you need to add some code here
         * Hint: update this.state, but also include currentClockTime
         * in startTimes/stopTimes */

        if (this.state == ProcessState.NEW){
            if (state == ProcessState.READY) {
                this.state = state;
            }
        } else if (this.state == ProcessState.READY){
            if (state == ProcessState.RUNNING) {
                startTimes.add(currentClockTime);
                this.state = state;
            }
        } else if (this.state == ProcessState.RUNNING){
            if (state == ProcessState.READY){
                stopTimes.add(currentClockTime);
                this.state = state;
            } else if (state == ProcessState.TERMINATED){
                stopTimes.add(currentClockTime); //?
                this.state = state;
            }
        }

    }
    
    public int getPid() { 
        return this.pid;
    }
    
    public ArrayList<Integer> getStartTimes() {
        return startTimes;
    }
    
    public ArrayList<Integer> getStopTimes() {
        return stopTimes;
    }


}
