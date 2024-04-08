
public class PC {

    public static void main(String[] args) {
        /* TODO: You may change this method to perform any tests you like */
        
        final Process[] processes = {
                // Process parameters are: arrivalTime, burstTime, memoryRequirements (kB)

                new Process(0, 6, 10),
                new Process(1, 2, 20), //!
                new Process(2, 1, 15), //! P3
              //  new Process(8, 2, 322), // P
                new Process(5, 3, 4), //! P4
                new Process(7, 4, 16), //! P5


             //   new Process(8, 4, 2), //! P6
             //   new Process(8, 2, 40), // P7


                /*
                new Process(0, 3, 5),
                new Process(1, 1, 1), //!
                new Process(2, 6, 4), //!
                new Process(6, 2, 20), //!
                new Process(8, 7, 3), //! P5
                new Process(9, 10, 4), //!
                new Process(10, 3, 1), //!
                new Process(11, 11, 2), //!
                new Process(12, 11, 1), //!

                 */
        };
        final int[] availableBlockSizes = {15, 40, 10, 20}; // sizes in kB
        MemoryAllocationAlgorithm algorithm = new BestFit(availableBlockSizes);
        MMU mmu = new MMU(availableBlockSizes, algorithm);        
        Scheduler scheduler = new FCFS();
        CPU cpu = new CPU(scheduler, mmu, processes);
        cpu.run();

    }

}

