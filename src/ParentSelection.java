

import java.util.ArrayList;
import java.util.Random;

public class ParentSelection {
  
  static double esp = 1e-10;
  
  public static Parents RouletteWheel(
    Population population, long[] fitnessValues) {
    
    ArrayList<Individual> populationIndividuals = new ArrayList<>();
    
    @SuppressWarnings("unchecked")
    
    long totalFitnessSum = sumOfFitness(fitnessValues);
    
    Parents parents = selectParentTargets(fitnessValues, totalFitnessSum);  
    
    return parents; 
  }
  
  public static Parents selectParentTargets(long[] fitnessValues, double totalFitnessSum) {
    System.out.printf("selectParentTargets([], %f)", totalFitnessSum);
    int target1Ind, target2Ind;
    
    target1Ind = selectTarget(fitnessValues, totalFitnessSum);
    target2Ind = target1Ind;
    
    /* Ensure that target2 is different, unless there is only one value */
    while(target2Ind == target1Ind && fitnessValues.length >= 1) {
      System.out.println("picking 2nd target index");
      target2Ind = selectTarget(fitnessValues, totalFitnessSum);
    }
    
    Parents parents = new Parents(target1Ind, target2Ind);
    
    return parents;
  }
  
  public static int selectTarget(long[] fitnessValues, double totalFitnessSum) {
    Random rand = new Random();
    
    double targetCumulative = rand.nextDouble() * totalFitnessSum;
    double currentCumulative = 0;
    int currentCumulativeIndex = 0;
    
    System.out.printf("total fitness sum: %f\n", totalFitnessSum);
    
    System.out.printf("target cumulative: %f\n", targetCumulative);
    
    while(targetCumulative - currentCumulative > esp) {
      currentCumulative += fitnessValues[currentCumulativeIndex];
      currentCumulativeIndex++;
    }
    
    // The target is the previous value of currentCumulativeIndex
    return currentCumulativeIndex - 1;
  }
  
  public static long sumOfFitness(long[] fitnessValues) {
    long sum = 0;
    int fitnessValueLen = fitnessValues.length;
    
    for(int q = 0; q < fitnessValueLen; q++) {
      sum += fitnessValues[q];
    }
    
    return sum;
  }
  
}
