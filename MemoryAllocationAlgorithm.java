import java.util.ArrayList;

public abstract class MemoryAllocationAlgorithm {

    protected final int[] availableBlockSizes;

    protected ArrayList<Integer> startAvailable; // Στον πίνακα αυτό περιλαμβάνονται οι διευθύνσεις αρχής των ελεύθερων τμημάτων της κύριας μνήμης.
    protected ArrayList<Integer> sizeAvailable;  // Στον πίνακα αυτό περιλαμβάνονται τα μεγέθη των ελεύθερων τμημάτων της κύριας μνήμης.
    protected ArrayList<Integer> endAvailable;   // Στον πίνακα αυτό περιλαμβάνονται οι διευθύνσεις που τελειώνουν τα ελεύθερα τμήματα της κύριας μνήμης.

    protected ArrayList<MemorySlot>[] mainMemorySlots;  // πίνακας που έχει πίνακες οι οποίοι αποθηκεύουν όλα τα memorySlots που υπάρχουν στην RAM, ο κάθε πίνακας για διαφορετικό μπλοκ από τα αρχικά μπλοκς (availableBlockSizes)
    private int[] startingBlocks; // οι διευθύνσεις που αρχίζουν τα αρχικά μπλοκς του πίνακα availableBlockSizes

    public MemoryAllocationAlgorithm(int[] availableBlockSizes) {
        this.availableBlockSizes = availableBlockSizes;

        this.startAvailable = new ArrayList<>();
        this.sizeAvailable = new ArrayList<>();
        this.endAvailable = new ArrayList<>();
        int blocksCount = availableBlockSizes.length;
        this.mainMemorySlots = new ArrayList[blocksCount];

        for (int i=0;i<blocksCount;i++){
            mainMemorySlots[i] = new ArrayList<>();
        }

        // Υπολογισμός των διευθύνσεων αρχής του κάθε μπλοκ (εκτελείται μόνο την πρώτη φορά)
        this.startingBlocks = new int[blocksCount]; // οι διευθύνσεις που αρχίζουν τα αρχικά μπλοκς του πίνακα availableBlockSizes
        // ο πίνακας startingBlocks είναι βοηθητικός και χρησιμοποιείται μόνο για να αποφασίσουμε σε ποιο από τα αρχικά διαθέσιμα μπλοκς ανηκούν τα Memory Slots του currentlyUsedMemorySlots, βάση της start address τους
        startingBlocks[0] = 0; // η πρώτη διεύθυνση αρχής είναι πάντα το 0
        for (int i=1;i<blocksCount;i++){
            startingBlocks[i] = startingBlocks[i-1] + availableBlockSizes[i-1];
        }

    }

    public abstract int fitProcess(Process p, ArrayList<MemorySlot> currentlyUsedMemorySlots);


    /* Η μέθοδος αυτή βρίσκει τα ελεύθερα τμήματα της RAM (που σε αυτά γίνεται να μπουν οι καινούργιες διεργασίες) και ορίζει κατάλληλα τους
     * πίνακες startAvailable και sizeAvailable.
     * Η μέθοδος αυτή χρησιμοποιείται μόνο από τους αλγορίθμους δυναμικής τμηματοποίησης μνήμης.
     * Με τον όρο μπλοκ εννοούμε ένα από τα αρχικά μπλοκς της RAM, δηλαδή ένα κομμάτι μνήμης από τον πίνακα availableBlockSizes.
     */
    protected void findFreeMemorySpace(ArrayList<MemorySlot> currentlyUsedMemorySlots){

        // κάθε φορά που καλείται αυτή η μέθοδος βρίσκει από την αρχή όλα τα διαθέσιμα κενά της μνήμης και γι' αυτό "καθαρίζουμε" τους πίνακες
        startAvailable.clear();
        endAvailable.clear();
        sizeAvailable.clear();

        int blocksCount = availableBlockSizes.length;
        for (int i=0;i<blocksCount;i++){
            mainMemorySlots[i].clear();
        }

        // Κατηγοριοποίηση των memory slots με βάση σε ποιο αρχικό μπλοκ ανήκουν
        for (MemorySlot memorySlot : currentlyUsedMemorySlots){
            for (int i=0;i<blocksCount;i++){
                if (memorySlot.getBlockStart() == startingBlocks[i]){
                    mainMemorySlots[i].add(memorySlot);
                    break;
                }
            }
        }

        // αντιγραφή του currentlyUsedMemorySlots σε ένα καινούργιο ArrayList για να μην χαλάσει η αρχική διάταξή του και η αντιστοιχία με το idProcessesInRAM της MMU
        // ΥΛΟΠΟΙΗΣΗ ΓΙΑ ΕΝΑ ΜΟΝΟ ΜΠΛΟΚ ArrayList<MemorySlot> sortedCurrentlyUsedMemorySlots = new ArrayList<>(currentlyUsedMemorySlots);
        // ΥΛΟΠΟΙΗΣΗ ΓΙΑ ΕΝΑ ΜΟΝΟ ΜΠΛΟΚ bubbleSortArrayList(sortedCurrentlyUsedMemorySlots);

        // ΥΛΟΠΟΙΗΣΗ ΓΙΑ ΠΟΛΛΑ ΜΠΛΟΚΣ
        ArrayList<MemorySlot>[] sortedMainMemorySlots = new ArrayList[blocksCount]; // αντιγραφή των mainMemorySlots σε καινούργια ArrayList για να μην χαλάσει η αρχική διάταξή τους και αλλοιώσει την χρήση τους στους αλγορίθμους FirstFit, NextFit κτλ
        for (int i=0;i<blocksCount;i++){
            sortedMainMemorySlots[i] = new ArrayList<MemorySlot>(mainMemorySlots[i]);
        }
        for (int i=0; i<blocksCount ; i++){
            bubbleSortArrayList(sortedMainMemorySlots[i]);
        }

        // Προκαθοριμός αρχικών τιμών των πινάκων startAvailable, endAvailable, sizeAvailable
        for (int k=0; k < blocksCount ; k++) {
            ArrayList<MemorySlot> memorySlotsUsedInOneBlock = sortedMainMemorySlots[k];
            if (memorySlotsUsedInOneBlock.size() == 0) { // αν το συγκεκριμένο μπλοκ είναι κενό από διεργασίες
                startAvailable.add(startingBlocks[k]);
                endAvailable.add(startingBlocks[k] + availableBlockSizes[k] - 1);
                sizeAvailable.add(availableBlockSizes[k]);
            }
        }


        int j=0; //δημιουργω μια μεταβλητη j για να ειναι ο μετρητης των arraylist που δημιουργω παρακατω
        for (ArrayList<MemorySlot> memorySlotsUsedInOneBlock : sortedMainMemorySlots) {
            for (int i = 0; i < memorySlotsUsedInOneBlock.size(); i++) {
                MemorySlot curr = memorySlotsUsedInOneBlock.get(i); //αποθηκευω σε μια μεταβλητη curr το τρεχον slot χρησιμοποιημενης μνημης με ολα του τα στοιχεια

                if (i == 0 && curr.getStart() != curr.getBlockStart()) { //Ειδική περιπτωση για να εντοπισουμε και το πρωτο διαθεσιμο memorySlot.
                    startAvailable.add(j, curr.getBlockStart());
                    endAvailable.add(j, curr.getStart() - 1);
                    int size = endAvailable.get(j) - startAvailable.get(j) + 1;
                    sizeAvailable.add(j, size);
                    j++;
                }

                if (curr.getEnd() != curr.getBlockEnd()) {  //αυτη η συγκριση μπαίνει γιατί αν ειναι ίδιο το τέλος του τρεχον slot με το τέλος του block τότε δεν μπορώ να συνεχισω
                    if (i + 1 < memorySlotsUsedInOneBlock.size()) { //ελεγγω αν υπαρχει το επομενο στοιχειο αλλιως θα ειχα σφαλμα στην μεταβλητη next
                        MemorySlot next = memorySlotsUsedInOneBlock.get(i + 1); //αποθηκευω σε μια μεταβλητη next το επομενο slot χρησιμοποιημενης μνημης με ολα του τα στοιχεια
                        if (curr.getEnd() + 1 == next.getStart()) { // Αν τα 2 μπλοκς είναι διαδοχικά και άρα δεν υπάρχει διαθέσιμος χώρος ανάμεσά τους
                            continue;
                        } else {
                            endAvailable.add(j, next.getStart() - 1); //το τελος της διαθεσιμης μνημης ειναι -1 θεση απο την αρχη του επομενου
                        }
                    }
                    else {
                        endAvailable.add(j, curr.getBlockEnd());// αν δεν εχει επομενο ,τοτε αποθηκευω σαν τελος του slot το τελος του block
                    }

                    startAvailable.add(j, curr.getEnd() + 1);//η αρχη της διαθεσιμης μνημης ειναι +1 θεση απο το τελος του τρεχοντος slot
                    sizeAvailable.add(j, endAvailable.get(j) - startAvailable.get(j) + 1); //η πραξη αυτη ειναι για να κραταω και το μεγεθος του διαθεσιμου σλοτ καθε φορα
                    j++; //αυξανω την μεταβλητη j,ωστε την επομενη φορα να ερθει καινουρια διαθεσiμη μνημη και να μπει στα arraylist μου
                }
            }
        }

        // ταξινόμηση Bubblesort για να έχουμε πάντα με στην σωστή σειρά τα διαθέσιμα ελεύθερα μπλοκς (που αυτά θα χρησιμοποιηθούν στους αλγορίθμους δέσμευσης μνήμης
        int temp;
        boolean sorted = false;
        while (!sorted) {
            sorted = true;
            for (int i = 0; i < startAvailable.size()-1; i++) {
                if (startAvailable.get(i) > startAvailable.get(i+1)) {
                    temp = startAvailable.get(i);
                    startAvailable.set(i, startAvailable.get(i + 1));
                    startAvailable.set(i + 1, temp);
                    temp = endAvailable.get(i);
                    endAvailable.set(i, endAvailable.get(i + 1));
                    endAvailable.set(i + 1, temp);
                    temp = sizeAvailable.get(i);
                    sizeAvailable.set(i, sizeAvailable.get(i + 1));
                    sizeAvailable.set(i + 1, temp);
                    sorted = false;
                }
            }
        }


    }

    // υλοποίηση του αλγορίθμου ταξινόμησης Bubblesort γιατί δεν επιτρέπεται το import άλλων κλάσεων
    private void bubbleSortArrayList(ArrayList<MemorySlot> list) {
        MemorySlot temp;
        boolean sorted = false;

        while (!sorted) {
            sorted = true;
            for (int i = 0; i < list.size()-1; i++) {
                if (list.get(i).getStart() > list.get(i+1).getStart()) {
                    temp = list.get(i);
                    list.set(i, list.get(i + 1));
                    list.set(i + 1, temp);
                    sorted = false;
                }
            }
        }
    }



    // TEST CODE
    public ArrayList<Integer> getStartAvailable() {
        return startAvailable;
    }

    public ArrayList<Integer> getEndAvailable() {
        return endAvailable;
    }

    public ArrayList<Integer> getSizeAvailable() {
        return sizeAvailable;
    }
}
