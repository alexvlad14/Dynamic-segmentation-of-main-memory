# Dynamic Segmentation of Main Memory with Various Allocation Algorithms

# Introduction

This project implements dynamic segmentation of main memory with several allocation algorithms. The implemented algorithms include:

1. First Come First Serve (FCFS)
2. Shortest Job First (SJF)
3. Round Robin
4. Best Fit
5. Worst Fit
6. First Fit
7. Next Fit
These algorithms are used to efficiently allocate and manage memory segments in a dynamic environment.

#  Supported Algorithms

# First Come First Serve (FCFS)
This algorithm allocates memory to processes in the order they arrive.

# Shortest Job First (SJF)
SJF allocates memory to the process with the shortest estimated run time first.

# Round Robin
Round Robin allocates memory to processes in a cyclic manner, giving each process a fixed time slice.

# Best Fit
Best Fit allocates memory to the process that best fits the available memory block, minimizing wasted space.

# Worst Fit
Worst Fit allocates memory to the process that leaves the largest available memory block, potentially leading to fragmentation.

# First Fit
First Fit allocates memory to the first available memory block that is large enough to accommodate the process.

# Next Fit
Next Fit is similar to First Fit but starts searching for a suitable memory block from the last allocation point.

