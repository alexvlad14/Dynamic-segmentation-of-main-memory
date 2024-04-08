import java.util.ArrayList;

public class FirstFit extends MemoryAllocationAlgorithm {
    
    public FirstFit(int[] availableBlockSizes) {
        super(availableBlockSizes);
    }

    public int fitProcess(Process p, ArrayList<MemorySlot> currentlyUsedMemorySlots) {
        boolean fit = false;
        int address = -1;
        /* TODO: you need to add some code here
         * Hint: this should return the memory address where the process was
         * loaded into if the process fits. In case the process doesn't fit, it
         * should return -1. */

        findFreeMemorySpace(currentlyUsedMemorySlots); //Εύρεση των διαθέσιμων slots

        /*
         * Αλγόριθμος First Fit
         * Βρίσκει το πρώτο μεγάλο διαθέσιμο block ώστε να χωράει η διεργασία
         */
        int posFF=-1;
        for (int i=0; i<sizeAvailable.size(); i++){
            if(sizeAvailable.get(i) >= p.getMemoryRequirements()){
                fit=true;
                posFF=i;
                break;
            }
        }

        if (fit) {
            address=startAvailable.get(posFF); //εφόσον βρέθηκε το κατάλληλο block επιστρέφει την αρχική θέση του slot που θα τοποθετηθεί
        }

        return address;
    }

}
