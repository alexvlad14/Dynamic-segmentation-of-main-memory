import java.util.ArrayList;

public class MMU {

    private final int[] availableBlockSizes;
    private MemoryAllocationAlgorithm algorithm;
    private ArrayList<MemorySlot> currentlyUsedMemorySlots;

    private ArrayList<Integer> idProcessesInRAM; // Περιλαμβάνει όλα τα pid των διεργασιών που βρίσκονται στην RAM. Αυτός ο πίνακας λειτουργεί παράλληλα με τον currentlyUsedMemorySlots.
    
    public MMU(int[] availableBlockSizes, MemoryAllocationAlgorithm algorithm) {
        this.availableBlockSizes = availableBlockSizes;
        this.algorithm = algorithm;
        this.currentlyUsedMemorySlots = new ArrayList<MemorySlot>();

        this.idProcessesInRAM = new ArrayList<>();
    }

    public boolean loadProcessIntoRAM(Process p) {
        boolean fit = false;
        /* TODO: you need to add some code here
         * Hint: this should return true if the process was able to fit into memory
         * and false if not */

        System.out.print("loadProcessIntoRAM " + p.getPCB().getPid()+"   "); //!RAM
        int address = algorithm.fitProcess(p,currentlyUsedMemorySlots); // η διεύθυνση που θα μπει η καινούργια διεργασία p
        System.out.println(address + " = address   "); //!RAM
        if ( address != -1 ){ // μπορεί να τοποθετησεί η διεργασία p στην RAM, στην διεύθυνση address
            int addressEnd = address + p.getMemoryRequirements() - 1;
            int blockStart = 0, blockEnd = 0;
            int counter = 0; // είναι διεύθυνση μνήμης, αθροίζει όλες τις προηγούμενες διεθύνσεις των μπλοκ
            for (int i=0; i< availableBlockSizes.length; i++){
                if (address < availableBlockSizes[i] + counter){
                    blockEnd = availableBlockSizes[i] + counter - 1;
                    blockStart = blockEnd + 1 - availableBlockSizes[i];
                    break;
                } else {
                    counter += availableBlockSizes[i];
                }
            }
            MemorySlot newMemorySlot = new MemorySlot(address, addressEnd, blockStart, blockEnd);
            currentlyUsedMemorySlots.add(newMemorySlot); // γίνεται η δέσμευση του χώρου μνήμης στην RAM
            idProcessesInRAM.add(p.getPCB().getPid()); // προσθήκη pid της διεργασίας που αυτή καταλαμβάνει το newMemorySlot
            fit = true;
            p.getPCB().setState(ProcessState.READY,CPU.clock); // Η διεργασία p από την κατάσταση NEW μεταβαίνει στην READY // Το CPU.clock εδω δεν παιζει ρολο
        }
        return fit;
    }

    public ArrayList<MemorySlot> getCurrentlyUsedMemorySlots() {
        return currentlyUsedMemorySlots;
    }

    public ArrayList<Integer> getIdProcessesInRAM() {
        return idProcessesInRAM;
    }

    // TEST CODE
    public MemoryAllocationAlgorithm getAlgorithm() {
        return algorithm;
    }


}
