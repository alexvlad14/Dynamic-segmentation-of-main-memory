import java.util.ArrayList;

public class BestFit extends MemoryAllocationAlgorithm {
    
    public BestFit(int[] availableBlockSizes) {
        super(availableBlockSizes);
    }

    public int fitProcess(Process p, ArrayList<MemorySlot> currentlyUsedMemorySlots) {
        boolean fit = false;
        int address = -1;
        /* TODO: you need to add some code here
         * Hint: this should return the memory address where the process was
         * loaded into if the process fits. In case the process doesn't fit, it
         * should return -1. */

        /*
        System.out.println("////////////////////");
        System.out.println("startAvailable = "+ startAvailable);
        System.out.println("endAvailable = "+ endAvailable);
        System.out.println("sizeAvailable = "+ sizeAvailable);
        System.out.println("////////////////////");

         */

        findFreeMemorySpace(currentlyUsedMemorySlots); //Εύρεση των διαθέσιμων slots

        /*
         * Αλγόριθμος Best Fit
         * Bρίσκει το min όλων των διαθέσιμων block που χωράει η διεργασία
         */
        int posBF=-1;
        int sizeBF=-1;
        for (int i=0; i<sizeAvailable.size(); i++){
            if(sizeAvailable.get(i)>=p.getMemoryRequirements()){ //βρίσκει τα slots που χωράει η διεργασία
                if(sizeBF==-1 || sizeBF>=sizeAvailable.get(i)){ //βρίσκει το min slot
                    posBF=i;
                    sizeBF=sizeAvailable.get(i);
                    fit=true;
                }
            }
        }

        if (fit) {
            address=startAvailable.get(posBF); //εφόσον βρέθηκε το κατάλληλο block επιστρέφει την αρχική θέση του slot που θα τοποθετηθεί
        }

        return address;
    }

}
