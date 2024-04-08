
public class FCFS extends Scheduler {

    private Process previousProcess; // η προηγούμενη διεργασία που επιλέχθηκε από τον αλγόριθμο

    public FCFS() {
        /* TODO: you _may_ need to add some code here */
        previousProcess = null;
    }

    /*Αυτή η συνάρτηση προσθέτει τη διεργασία στο ArrayList
      του Scheduler,που εκεί θα αποθηκεύονται όλες οι διεργασίες */
    public void addProcess(Process p) {
        if(p !=null){ //Κάνω έλεγχο για να αποφύγω την περίπτωση η διεργασία να είναι null
            this.processes.add(p);
        }
    }
    
    public Process getNextProcess() {

        Process minProcess = null;//αποθηκεύω την διεργασία που χρησιμοποιώ τώρα
        int min = 2147483647;//Κάνω min για να βρω τη διεργασία που έρχεται πρώτη στον επεξεργαστή

        for (Process p:processes) {//Ψαχνω ολες της διεργασιες για να βρω αυτην με τον μικροτερο Arrive time.
            if (min > p.getArrivalTime()) {
                min = p.getArrivalTime();
                minProcess = p;
            }
        }

        int minBT = 2147483647;
        for (Process p : processes) { // σε περίπτωση που έχουν έρθει 2 διεργασίες στον ίδιο χρόνο επιλέγω αυτήν με το μικρότερο burstTime
            if(minProcess.getArrivalTime() == p.getArrivalTime()){
                if (minBT > p.getBurstTime()) {
                    minBT = p.getBurstTime();
                    minProcess = p;
                }
            }
        }

        return minProcess;
    }

}
