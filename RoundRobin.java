
public class RoundRobin extends Scheduler {

    private int quantum;

    private int timesExecuted; // πόσες φορές έχει εκτελεστεί συνεχόμενα μια διεργασία
    private int previousProcessId; // το id της διεργασίας που εκτελέστηκε τελευταία
    
    public RoundRobin() {
        this.quantum = 1; // default quantum
        /* TODO: you _may_ need to add some code here */
        this.timesExecuted = 0;
        this.previousProcessId = 0;
    }
    
    public RoundRobin(int quantum) {
        this();
        this.quantum = quantum;
    }

    /* Χρησιμοποιούμε τον πίνακα processes της κλάσης-προγόνου Scheduler ως μία ουρά. Τα στοιχεία που έρχονται σε αυτόν τοποθετούνται στο τέλος
     * του, ενώ όταν χρειάζεται να επιλέξουμε την επόμενη διεργασία επιλέγουμε πάντα την πρώτη.
     */
    public void addProcess(Process p) {
        /* TODO: you need to add some code here */
        if (p != null){
            processes.add(p);
        }
    }

    public Process getNextProcess() {
        /* TODO: you need to add some code here
         * and change the return value */

        Process nextProcess = null;
        if (!processes.isEmpty()) {
            if (timesExecuted >= quantum) {
                timesExecuted = 0;
                boolean previousProcessExists = false;
                for (Process p : processes){ // ελέγχω αν υπάρχει ο προηγούμενη διεργασία (που διάλεξε ο αλγόριθμος) στην μνήμη
                    if (p.getPCB().getPid() == previousProcessId){
                        previousProcessExists = true;
                        break;
                    }
                }

                if (previousProcessExists){ // αν υπάρχει την τοποθετώ στο τέλος της ουράς
                    // Αν η προηγούμενη διεργασία (έστω p) έχει τρέξει συνεχομένες φορές ίσες με την τιμή του κβάντου που θέλουμε,
                    // τότε επιλέγουμε την επόμενη προς εκτέλεση και αφαιρούμε την p. Ελέγχουμε αν η p έχει τρέξει για χρόνο ίσο με το χρόνο
                    // καταιγισμού της και αν όχι την τοποθετούμε στο τέλος της ουράς.
                    Process firstInQueue = processes.get(0);
                    processes.remove(0);
                    int totalTimeExecuted = firstInQueue.getTotalTimeExecuted();
                    if (totalTimeExecuted < firstInQueue.getBurstTime()) {
                        // εάν δεν έχει τελειώσει η διεργασία την τοποθετούμε στο τέλος του πίνακα μας
                        processes.add(firstInQueue);
                    }
                }

                // επιλογή της επόμενης διεργασίας
                nextProcess = processes.get(0);
                previousProcessId = nextProcess.getPCB().getPid();
            } else {
                if (previousProcessId != processes.get(0).getPCB().getPid()){ // σε περίπτωση που η προηγούμενη διεργασία τελείωσε πριν ολοκληρωθούν όλοι οι χρόνοι ενός κβάντου
                    timesExecuted = 0;
                }
                nextProcess = processes.get(0);
                previousProcessId = nextProcess.getPCB().getPid();
            }
            timesExecuted++;
        }

        return nextProcess;
    }

}
