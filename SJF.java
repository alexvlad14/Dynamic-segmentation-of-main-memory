
public class SJF<minProcess> extends Scheduler {

    public SJF() {
        /* TODO: you _may_ need to add some code here */
    }

    /* Αυτή η συνάρτηση προσθέτει τη διεργασία στο ArrayList
       του Scheduler, όπου εκεί θα αποθηκευύονται όλες οι διεργασίες */
    public void addProcess(Process p) {
        if(p !=null ){ //Κάνω έλεγχο για να αποφύγω την περίπτωση η διεργασία να είναι null
            this.processes.add(p);
        }
    }

    private  boolean check = false; //Δημιουργώ αυτή  τη μεταβλητή γιατί θέλω την πρώτη φορά που θα καλέσει τη συναρτηση getNextProcess
    //να μου δώσει τη διεργασία με τον μικρότερο arrive time και τις επόμενες να μου δώσει τις διεργασίες με τον μικρότερο burst time.
    private int sumClockProcess = 0; //Μετράω πόσους κύκλους έκανε η διεργασία για να δω αν τελείωσε και μπορώ να πώω στην επόμενη
    private Process lastProcess = null;

    public Process getNextProcess() {

        sumClockProcess++;
        Process minProcess = null; //Αποθηκεύω τη διεργασία που χρησιμοποιώ τώρα
        int min = 2147483647;
        int minBT = 2147483647;
        if (!processes.isEmpty()) {
            if (!check) { //Ελέγχω ότι είναι η πρώτη διεργασία που ήρθε σύμφωνα με τον arrive time
                for (Process p : processes) { //Ψάχνω από όλες τις διεργασίες ποιά έχει τον μικρότερο arrive time
                    if (min > p.getArrivalTime()) {
                        min = p.getArrivalTime();
                        minProcess = p;
                    }
                }

                for (Process p : processes) {
                    if(minProcess.getArrivalTime() == p.getArrivalTime()){
                        if (minBT > p.getBurstTime()) {
                            minBT = p.getBurstTime();
                            minProcess = p;
                        }
                    }
                }

                if (sumClockProcess == minProcess.getBurstTime()) { //ελέγχω ότι έχουν ολοκληρωθεί οι κύκλοι της πρώτης διεργασίας που κάλεσα για να πάω στην επόμενη
                    check = true;
                }

            } else { //ελέγχω ποιά διεργασία έφτασε πρώτη σύμφωνα με τον burstTime
                for (Process p : processes) { //Ψάχνω όλες τις διεργασίες για να βρω αυτή με τον μικρότερο burst time
                    if (min > p.getBurstTime()) {
                        min = p.getBurstTime();
                        minProcess = p;
                    }
                }
            }
        }
        return minProcess;
    }
}
