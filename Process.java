import java.util.ArrayList;

public class Process {
    private ProcessControlBlock pcb;
    private int arrivalTime;
    private int burstTime;
    private int memoryRequirements;

    private int totalTimeExecuted;
    
    public Process(int arrivalTime, int burstTime, int memoryRequirements) {
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.memoryRequirements = memoryRequirements;
        this.pcb = new ProcessControlBlock();
        this.totalTimeExecuted = 0;
    }
    
    public ProcessControlBlock getPCB() {
        return this.pcb;
    }
   
    public void run() {
        /* TODO: you need to add some code here
         * Hint: this should run every time a process starts running */

        // Αυτή η μέθοδος καλείται μόνο όταν η διεργασία είναι σε κατάσταση READY ή RUNNING

        totalTimeExecuted++;
        pcb.setState(ProcessState.RUNNING, CPU.clock);
        if (totalTimeExecuted == burstTime){
            pcb.setState(ProcessState.TERMINATED,CPU.clock);
        }

    }
    
    public void waitInBackground() {
        /* TODO: you need to add some code here
         * Hint: this should run every time a process stops running */

        pcb.setState(ProcessState.READY, CPU.clock);

        // Ελέγχω αν την στιγμή που σταμάτησε να εκτελείται η διεργασία, έχει συμπληρωθεί ο συνολικός χρόνος εκτέλεσης και άρα η διεργασία τερματίζει.
        if (totalTimeExecuted == burstTime){
            pcb.setState(ProcessState.TERMINATED, CPU.clock);
        }

    }

    public double getWaitingTime() {
        /* TODO: you need to add some code here
         * and change the return value */
        ArrayList<Integer> startTimes = pcb.getStartTimes();
        ArrayList<Integer> stopTimes = pcb.getStopTimes();

        double waitingTime = getResponseTime();
        int timesExecuted = startTimes.size();
        for (int i=1; i < timesExecuted ; i++){
            waitingTime += startTimes.get(i) - stopTimes.get(i-1);
        }
        return waitingTime;
    }
    
    public double getResponseTime() {
        /* TODO: you need to add some code here
         * and change the return value */
        double responeTime = pcb.getStartTimes().get(0) - arrivalTime;
        return responeTime;
    }
    
    public double getTurnAroundTime() {
        /* TODO: you need to add some code here
         * and change the return value */
        int size = pcb.getStopTimes().size();
        double endedAt ;
        if (size == 0){
            endedAt = CPU.clock;
        } else {
            endedAt = pcb.getStopTimes().get(size - 1);
        }
        double turnAroundTime = endedAt - arrivalTime + 1;//- startedAt + 1;
        return turnAroundTime;
    }


    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public int getMemoryRequirements() {
        return memoryRequirements;
    }

    public int getTotalTimeExecuted() {
        return totalTimeExecuted;
    }
}
