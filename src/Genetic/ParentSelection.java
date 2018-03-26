package Genetic;

import java.util.ArrayList;
import java.util.Random;

public class ParentSelection {
  
  static double esp = 1e-10;
  
  public static ArrayList<Individual> RouletteWheel(
    ArrayList<Individual> population, double[] fitnessValues) {
    
    @SuppressWarnings("unchecked")
    ArrayList<Individual> newPopulation = 
      (ArrayList<Individual>) population.clone();
    
    double totalFitnessSum = sumOfFitness(fitnessValues);
    
    System.out.println("THE FITNESS VALUES OF THE INDIVIDUALS");
    for(int q = 0; q < fitnessValues.length; q++) {
      System.out.println(fitnessValues[q]);
    }
    
    Parents parents = selectParentTargets(fitnessValues, totalFitnessSum);  
  
    return newPopulation; 
  }
  
  public static Parents selectParentTargets(double[] fitnessValues, double totalFitnessSum) {
    
    int target1Ind, target2Ind;
    
    target1Ind = selectTarget(fitnessValues, totalFitnessSum);
    target2Ind = target1Ind;
    
    /* Ensure that target2 is different, unless there is only one value */
    while(target2Ind == target1Ind && fitnessValues.length >= 1) {
      target2Ind = selectTarget(fitnessValues, totalFitnessSum);
    }
    
    Parents parents = new Parents(target1Ind, target2Ind);
    
    return parents;
  }
  
  public static int selectTarget(double[] fitnessValues, double totalFitnessSum) {
    Random rand = new Random(System.currentTimeMillis());
    
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
  
  public static double sumOfFitness(double[] fitnessValues) {
    double sum = 0;
    int fitnessValueLen = fitnessValues.length;
    
    for(int q = 0; q < fitnessValueLen; q++) {
      sum += fitnessValues[q];
      System.out.println(sum);
    }
    
    return sum;
  }
  
}
