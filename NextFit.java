import java.util.ArrayList;

public class NextFit extends MemoryAllocationAlgorithm {

    private int lastAllocatedEndAddress; // η διεύθυνση τέλους του τελευταίου memoryslot που εκχωρήθηκε στην μνήμη

    public NextFit(int[] availableBlockSizes) {
        super(availableBlockSizes);
        this.lastAllocatedEndAddress = 0;
    }

    public int fitProcess(Process p, ArrayList<MemorySlot> currentlyUsedMemorySlots) {
        boolean fit = false;
        int address = -1;
        /* TODO: you need to add some code here
         * Hint: this should return the memory address where the process was
         * loaded into if the process fits. In case the process doesn't fit, it
         * should return -1. */

        findFreeMemorySpace(currentlyUsedMemorySlots); //Εύρεση των διαθέσιμων τμημάτων της RAM

        /*
         * Αλγόριθμος Next Fit
         * Ξεκινάει από τη θέση που έγινε η τελευταία ανάθεση block και
         * βρίσκει το πρώτο μεγάλο διαθέσιμο block ώστε να χωράει η διεργασία
         */

        // Ταξινόμηση Bubblesort του πίνακα startAvailable (οι πίνακες startAvailable και sizeAvailable λειτουργούν ως ένα για να μην υπάρχει απώλεια πληροφορίας, δηλαδή ποιο διαθέσιμο μέγεθος αντιστοιχεί σε ποια διεύθυνση αρχής)
        // Η ταξινόμιση του startAvailable γίνεται στην findFreeMemorySpace
        // Έτσι ελέγχουμε διαδοχικά τα ελεύθερα μπλοκς από την αρχή μέχρι και το τέλος της RAM

        if (startAvailable.size() != 0) { // υπάρχει διαθέσιμος χώρος για να μπει η διεργασία
            int pos = -1; // θέση στον πίνακα startAvailable από την οποία και μετά ελέγχουμε αν υπάρχει free block για την υλοποίηση της next process
            for (int i = 0; i < startAvailable.size(); i++) { // βρίσκουμε τον δείκτη του επόμενου διαθέσιμου μπλοκ στο οποίο θα εξετάσουμε αν γίνεται να μπει η καινούργια διεργασία
                if (lastAllocatedEndAddress <= startAvailable.get(i)) { // το startAvailable είναι ταξινομημένο, οπότε το πρώτο διαθέσιμο μπλοκ που βρίσκεται μετά την lastAllocatedEndAddress είναι το ζητούμενο
                    pos = i;
                    break;
                }
            }
            int posNF = -1; // η θέση του πίνακα startAvailable που περιέχει την διεύθυνση που επιστρέφει ο αλγόριθμος
            if (pos != -1) {
                for (int i = pos; i < sizeAvailable.size(); i++) { // από το μπλοκ που βρήκα πριν ελέγχω διαδοχικά τα διαθέσιμα μπλοκ μέχρι να βρω το πρώτο στο οποίο χωράει η καινούργια διεργασία
                    if (p.getMemoryRequirements() <= sizeAvailable.get(i)) {
                        posNF = i;
                        fit = true;
                        break;
                    }
                }
            }

            if (!fit) { // δεν έχει βρεθεί ακόμα κάποιο ελεύθερο μπλοκ που να χωράει η καινούργια διεργασία p
                for (int i = 0; i < sizeAvailable.size(); i++) { // έλεγχος από την αρχή της RAM
                    if (p.getMemoryRequirements() <= sizeAvailable.get(i)) {
                        posNF = i;
                        fit = true;
                        break;
                    }
                }
            }

            if (fit) { // η διεργασία χωράει σε κάποια διαθέσιμη θέση
                address = startAvailable.get(posNF); //τότε η διευθυνση που θα επιστραφεί είναι η διεύθυνση αρχής του διαθεσιμού slot που βρέθηκε
                lastAllocatedEndAddress = address + p.getMemoryRequirements() - 1;
            }
        }
        return address;
    }


}
