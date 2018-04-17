# Branches

1) Single threaded - master branch 

2) Multi threaded - parallel branch

# How to run on HPC

1) Change the job1_script.txt file (change to your NUSID 
2) ssh into HPC (we used atlas7) 
3) run `qsub job1_script.txt`
4) run `qstat` to verify that your job is queued
5) To delete a task, `qdel [taskname]`

Set run_me.sh to runnable `chmod +x run_me.sh`

# Hello there

You can pull directly into your eclipse workspace.

Navigate to Genetic.java and run it from there.

## Overview of project directory structure

Genetic is the main function that calls the rest. 

## Objects in the GA

*Individual* represents the smallest unit in the GA here, with weights that can
be modified.

*Population* is a collection (ArrayList) of Individuals

*Island* is a representation of an ArrayList of Individuals too. The difference
between population and island is that islands can store multiple copies of
population. This is useful if we want to compare between generations to see how
the Individuals are evolving.

## Parameters and their meanings

*CYCLES* Number of times this cross island best selection thing is being carried
out.

*RUN_GAME_ITERATIONS* Due to the non-deterministic nature of the tile orders,
the scores might be different. Right now, the highest of all the iterations is
used as its fitness score.

The rest are pretty self explanatory.
*NUMBER_OF_ISLANDS*

*NUMBER_OF_GENERATIONS*

*ISLAND_NUMBER_OF_INDIVIDUALS*

## Misc
There is a debug flag for controlling verbosity of output.

*ResultLog* class contains a csv printer which is useful when we run it on the
server.
