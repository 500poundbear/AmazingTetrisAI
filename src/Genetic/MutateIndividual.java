package Genetic;

import java.util.Random;

public class MutateIndividual {
  public static double MUTATION_PROBABILITY = 0.1;
  public static double MUTATION_PERCENTAGE_RANGE = 0.1;

  static Random rand;

  public static Individual m(Individual i) {
    
    rand = new Random();
    double[] iWeights = i.getWeights();
    
    for(int q = 0; q < i.size; q++) {
      if (rand.nextDouble() < MUTATION_PROBABILITY) {
        // Time to do some mutation
        i.setWeight(q, mutateWeight(iWeights[q]));
      }
    }
    
    return i;
  }
  
  private static double mutateWeight(double oldWeight) {
    double delta = rand.nextDouble() * MUTATION_PERCENTAGE_RANGE - (MUTATION_PERCENTAGE_RANGE / 2);
    return oldWeight * (1 + delta);
  }
  
  public static void main(String[] args) {
    Individual test = new Individual(10);
    double[] testWeights = {0.1234, 4.3, 2.3, 4.1, 1.4, 5.6, -2, -3, -1, -0.5};
    test.setWeights(testWeights);
    System.out.printf("Old weights: \n");
    test.printWeights();
    test = m(test);
    System.out.printf("New weights: \n");
    test.printWeights();
  }
}
