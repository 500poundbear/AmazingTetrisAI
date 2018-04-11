package Genetic;

import java.util.ArrayList;
import java.util.Random;

public class ParentCrossOver {
  /*
   * Given two parents, choose a random point [1,size - 2] where
   * everything before is copied from A and everything 
   * after is copied from BS
   */
  public static ArrayList<Individual> co(Individual a, Individual b) {
    ArrayList<Individual> newIndividuals = new ArrayList<Individual>();
    
    assert(a.size == b.size);
     
    Random rand;
    rand = new Random();
    
    int randIndex = rand.nextInt(a.size - 2) + 1;
    
    //System.out.printf("randIndex: 0 to %d, %d to %d\n", randIndex, randIndex + 1, a.size - 1);
    
    Individual newA = new Individual();
    Individual newB = new Individual();
    
    double[] aWeights = a.getWeights();
    double[] bWeights = b.getWeights();
    
    for(int q = 0; q <= randIndex; q++) {
       newA.setWeight(q, aWeights[q]);
       newB.setWeight(q, bWeights[q]);
    }
    
    for(int q = randIndex + 1; q < a.size; q++) {
      newA.setWeight(q, bWeights[q]);
      newB.setWeight(q, aWeights[q]);
    }
    
    newIndividuals.add(newA);
    newIndividuals.add(newB);
    
    return newIndividuals;
  }
  
  private static void printWeights(double[] weights) {
    for(int q = 0; q < weights.length; q++) {
      System.out.print(weights[q] + " ");
    }
    System.out.print("\n");
  }
  
  public static void main(String[] args) {
    Individual testA = new Individual();
    Individual testB = new Individual();
    double[] AWeights = {0.1234, 4.3, 2.3, 4.1, 1.4, 5.6, -2, -3, -1, -0.5};
    double[] BWeights = {0.3, 4.3, 9.3, 1.2, -4.5, 3.4, -2.3, 3, -3, -0.1};
    
    testA.setWeights(AWeights);
    testB.setWeights(BWeights);
    
    co(testA, testB);
  }
}
