import java.util.ArrayList;


public class WorstFit extends MemoryAllocationAlgorithm {

    public WorstFit(int[] availableBlockSizes) {
        super(availableBlockSizes);
    }


    public int fitProcess(Process p, ArrayList<MemorySlot> currentlyUsedMemorySlots) {
        boolean fit = false;
        int address = -1;
        /* TODO: you need to add some code here
         * Hint: this should return the memory address where the process was
         * loaded into if the process fits. In case the process doesn't fit, it
         * should return -1. */

        findFreeMemorySpace(currentlyUsedMemorySlots); //Εύρεση των διαθέσιμων τμημάτων της RAM

        //Worst fit αλγοριθμος
        /* Εδώ βρίσκουμε το max  όλων των ελευθερων slot που χωράει να μπει η διεργασία μας
         * γιατί ο αλγόριθμος WorstFit χρησιμοποιεί το πιο μεγαλο σε μέγεθος μπλοκ
         * για την διεργασία που θέλει να βάλει
         */
        int posWorstFit=-1;
        int sizeWorstFit=-1;
        for(int i=0;i<sizeAvailable.size();i++){
            if(sizeAvailable.get(i)>sizeWorstFit){
                sizeWorstFit=sizeAvailable.get(i);
                posWorstFit=i;
            }
        }
        if(p.getMemoryRequirements()<=sizeWorstFit) //Ελεγχουμε αν τελικα χωραει η διεργασια στο slot που μας υπεδειξε ο αλγορθιθμος WorstFit
            fit=true;

        if(fit)
            address=startAvailable.get(posWorstFit);//Βαζω στην address  την αρχικη θεση του slot που μπηκε

        return address;
    }
}


