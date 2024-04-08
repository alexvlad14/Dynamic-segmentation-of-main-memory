import java.util.ArrayList;

public class CPU {

    public static int clock = 0; // this should be incremented on every CPU cycle
    
    private Scheduler scheduler;
    private MMU mmu;
    private Process[] processes;
    private int currentProcess;


    public CPU(Scheduler scheduler, MMU mmu, Process[] processes) {
        this.scheduler = scheduler;
        this.mmu = mmu;
        this.processes = processes;
    }
    
    public void run() {
        /* TODO: you need to add some code here
         * Hint: you need to run tick() in a loop, until there is nothing else to do... */

        int totalCPUCycles = getTotalTimeRequiredToExecute();
        int cyclesDone = 0;
        int maxArrivalTime = -1;
        for (Process p : processes){ // εύρεση του χρόνου που ήρθε η τελευταία διεργασία για να εκτελεστεί
            if (p.getArrivalTime() >= maxArrivalTime){
                maxArrivalTime = p.getArrivalTime();
            }
        }

        boolean willRunAgain = true;

        while ( cyclesDone <= totalCPUCycles + maxArrivalTime && willRunAgain){

            // Έλεγχος αν κάποια διεργασία γίνεται να μπει στην μνήμη.
            for (Process p : processes){
                if (p.getArrivalTime() <= CPU.clock && !scheduler.getProcesses().contains(p) && p.getPCB().getState() != ProcessState.TERMINATED){ // έχει φτάσει η διεργασία ΚΑΙ δεν υπάρχει ήδη στην μνήμη ΚΑΙ η η διεργασία δεν έχει τελειώσει
                    if (mmu.loadProcessIntoRAM(p)){ // χωράει στην μνήμη
                        // Η p μπαίνει στην μνήμη
                        scheduler.addProcess(p);
                    }
                }
            }

            // START TEST CODE
            System.out.println("Used Memory slots in RAM:");
            for (MemorySlot m : mmu.getCurrentlyUsedMemorySlots()){
                System.out.println(m.getStart()+ " " + m.getEnd() + " " +m.getBlockStart()+" "+m.getBlockEnd());
            }
            /*
            System.out.println("StartAvailable = "+ mmu.getAlgorithm().getStartAvailable());
            System.out.println("EndAvailable = "+ mmu.getAlgorithm().getEndAvailable());
            System.out.println("SizeAvailable = "+ mmu.getAlgorithm().getSizeAvailable());
             */
            // END TEST CODE


            // Επιλογή και εκτέλεση της επόμενης διεργασίας βάση του scheduler.
            Process nextProcess = scheduler.getNextProcess(); // η επόμενη διεργασία προς εκτέλεση
            if (nextProcess != null){ // υπάρχει κι άλλη διεργασία προς εκτέλεση
                if (nextProcess.getPCB().getPid() == currentProcess){ // αν η καινούργια διεργασία που επίλεξα είναι αυτή που έτρεχε και πριν, απλά συνεχίζει να τρέχει
                    // εκτελείται η διεργασία που έτρεχε και πριν
                    nextProcess.run();
                } else { // αλλιώς
                    // σταματάει η παλιά (τρέχουσα) διεργασία
                    for (Process p : processes){ // ψάχνει να βρει την παλιά διεργασία
                        if (p.getPCB().getPid() == currentProcess){
                            p.waitInBackground();
                            break;
                        }
                    }
                    // και εκτελείται η καινούργια
                    currentProcess = nextProcess.getPCB().getPid();
                    nextProcess.run();
                }
            } else {
                currentProcess = 0;
            }

            // START TESTING CODE
            System.out.println("t = " + clock + "   Τρεχει η: " + currentProcess + " (PID)");
            System.out.println("Βρίσκονται στην RAM: ");
            for (Process p : scheduler.getProcesses()){
                System.out.println(p.getPCB().getPid() +" " + "("+p.getPCB().getState()+") "); //startTimes = " + p.getPCB().getStartTimes() +"  stopTimes = " + p.getPCB().getStopTimes());
            }
            System.out.println();
            System.out.println("----------------------------------------------");
            // END TESTING CODE

            tick();
            cyclesDone++;
            // Τέλος κύκλου ρολογιού.

            // ΕΝΗΜΕΡΩΣΗ ΔΙΕΡΓΑΣΙΩΝ ΣΤΗΝ ΜΝΗΜΗ
            // Αφού εκτελέστηκε μία διεργασία σε αυτόν τον CPU κύκλο, ελέγχω αν έχει τερματίσει και αν ναι την αφαιρώ από την μνήμη
            // αυτή η διεργασία αφαιρείται την ίδια χρονική στιγμή που τελείωσε!
            // Μία διεργασία την στιγμή που τρέχει και τελείωνει μεταβαίνει κατευθείαν στην κατάσταση TERMINATED (έχει περάσει όμως από πρώτα από την RUNNING και άλλαξε αμέσως)
            for (Process p : scheduler.getProcesses()){
                if (p.getPCB().getPid() == currentProcess) { // βρίσκω την process που μόλις εκτελέστηκε και άρα μόνο αυτή έχει νόημα να ελέγξω αν τελείωσε
                    if (p.getPCB().getState() == ProcessState.TERMINATED) {

                        //test code
                        System.out.println("---------- "+ p.getPCB().getPid()+" ---------------");
                        System.out.println("Wait     = "+p.getWaitingTime());
                        System.out.println("Response = "+p.getResponseTime());
                        System.out.println("Turn     = "+p.getTurnAroundTime());
                        System.out.println("----------------------------------------------");
                        //end test code

                        scheduler.removeProcess(p); // η p έχει τελειώσει και δεν θα ξαναεκτελεστεί

                        // απελευθέρωση των πόρων μνήμης που έπιανε η p
                        int pid = p.getPCB().getPid(); // το pid της διεργασίας p που διαγράφεται γιατί τελείωσε
                        int sizeOfProcessesInRAM = mmu.getIdProcessesInRAM().size();
                        for (int i = 0; i < sizeOfProcessesInRAM; i++) {  // βρίσκω
                            if (mmu.getIdProcessesInRAM().get(i) == pid) {
                                mmu.getCurrentlyUsedMemorySlots().remove(i); // ελευθερώνω το memory slot που περιείχε τη διεργασία p
                                mmu.getIdProcessesInRAM().remove(i);         // διαγράφω την διεργασία p που υπήρχε σε αυτό το memory slot
                                break;
                            }
                        }
                    }
                    break;
                }
            }

            // Αν όλες οι διεργασίες είναι στην κατάσταση TERMINATED τότε δεν έχει νόημα να συνεχίσει η εκτέλεση της CPU
            int numberOfTerminatedProcess = 0;
            for (Process p : processes){
                if (p.getPCB().getState() != ProcessState.TERMINATED){
                    break;
                }
                numberOfTerminatedProcess++;
            }
            if (numberOfTerminatedProcess == processes.length){
                willRunAgain = false;
            }

        }


    }
    
    public void tick() {
        /* TODO: you need to add some code here
         * Hint: this method should run once for every CPU cycle */
        clock++;
    }

    private int getTotalTimeRequiredToExecute(){
        // Συνολικός χρόνος που χρειάζεται για να τελειώσουν όλες οι διεργασίες. Από την χρονική στιγμή t=0.
        int totalTimeRequiredToExecute = 0;
        for (Process p : processes){
            totalTimeRequiredToExecute += p.getBurstTime();
        }
        // + την χρονική στιγμή που εκτελείται η πρώτη διεργασία. Αυτό χρειάζεται για την περίπτωση που ξεκινάει να τρέχει η CPU (ενδεικτικά) στο clock time = 0
        // και η πρώτη διεργασία δεν έρχεται κατευθείαν στην μνήμη.
        totalTimeRequiredToExecute += CPU.clock;
        return  totalTimeRequiredToExecute;
    }


}
