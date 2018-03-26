package Genetic;
/*
 * Traditional genetic algorithm implementation
 */

import Engine.Trainer;
import java.util.ArrayList;

public class Genetic{
  private double[]weights;
  private static final int INDIVIDUAL_SIZE = 10;
  private static final int POPULATION_SIZE = 1;

  ArrayList<Individual> population;
  double[] fitnessValues;
  
  public static void main(String[] args) {
    Genetic x = new Genetic();
  }
  
  public Genetic() {
    weights = new double[INDIVIDUAL_SIZE];
    
    // Generate initial population
    population = generatePopulation(POPULATION_SIZE);
     
    // Compute fitness for each individual
    
    fitnessValues = computeIndividualFitness();
    
    // Print for q = 0
    int q = 0;
    System.out.println("==============");
    System.out.printf("%d. %e\n", q, fitnessValues[q]);
    
    for(int w=0; w < fitnessValues.length; w++) {
      System.out.printf("fitnessValues[%d] = %f\n", w, fitnessValues[w]);
    }
    
    selectionAndMutation();
    
  }
  
  private void selectionAndMutation() {
    
    population = ParentSelection.RouletteWheel(population, fitnessValues);
    
  }
  
  private double[] computeIndividualFitness() {
    int pSize = population.size();
    double[] fitnessValues = new double[pSize];
    System.out.println("Computing individual fitness"); 
    
    for(int q = 0; q < pSize; q++) {
      System.out.printf("============\nTraining #%d\n", q);
      Individual i = population.get(q);

      Trainer x = new Trainer();
      x.setWeights(i.getWeights());
      
      System.out.printf("Printing weights for training:\n");
      for(int e = 0; e < i.getWeights().length; e++) {
        System.out.printf("weight[%d] = %f\n", e, i.getWeights()[e]);
      }
      
      fitnessValues[q] = x.runGame();
      System.out.printf("setting fitnessvalues[%d] = %f\n", q, fitnessValues[q]);
    }
     
    return fitnessValues;
  }
  
  private ArrayList<Individual> generatePopulation(int size) {
    ArrayList<Individual> population = new ArrayList<>();
    
    for(int q = 0; q < size; q++) {
      Individual n = new Individual(INDIVIDUAL_SIZE);
      population.add(n);
    }
    
    return population;
  }
  
}
