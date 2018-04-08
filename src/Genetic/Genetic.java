package Genetic;
/*
 * Traditional genetic algorithm implementation
 */

import Engine.Trainer;
import Engine.ResultsLog;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class Genetic{

  Random rand;
  
  private double[] weights;

  public static final int CYCLES = 10;

  public static final int ISLAND_SIZE = 10;

  public static final int INDIVIDUAL_SIZE = 10;
  public static final int POPULATION_SIZE = 5;
  public static final int RUN_GAME_ITERATIONS = 1;

  public static final int NUMBER_OF_GENERATIONS = 2;

  private static final int NUMBER_SELECTION_MUTATION_ROUNDS = 10;

  // Select and mutate bottom X%
  private static final double SELECT_AND_MUTATE_BOTTOM_PERCENTAGE = 0.4;

  Population population;
  ArrayList<Individual> individuals;
  long[] fitnessValues;
  
  Generations generations;
  
  public static void main(String[] args) {
    Genetic x = new Genetic();
  }
  
  public Genetic() {
    
    rand = new Random(System.currentTimeMillis());
    
    ArrayList<Island> islands = new ArrayList<Island>();
    
    /* First iteration requires using new weights*/
    for(int q = 0; q < ISLAND_SIZE; q++) {
      System.out.printf("\nISLAND #%d\n", q + 1);
      Island n = new Island();
      // Configure the weights before calling other functions
      population = new Population();
      population.setPopulation(generatePopulation(POPULATION_SIZE)); 
          
      Generations g = runGenerationIterations();
    
      n.setPopulation(g.getLatestPopulation());
      islands.add(n);
    }
    
    
  }
  
  private Generations runGenerationIterations() {
    generations = new Generations();
    
    individuals = population.getPopulation();
    
    while(generations.populationCount() < NUMBER_OF_GENERATIONS) {
      System.out.printf("Generation %d\n", generations.populationCount() + 1);
      // Compute fitness for each individual
      
      fitnessValues = computeIndividualFitness();
      
      population.setFitnessScores(fitnessValues);
      
      // Save the population to generations
      generations.addPopulation(population);
      
      //selectionAndMutation();
      selectAndMutateBottomX();
    }
    
    return generations;  
  }
  
  private void selectAndMutateBottomX() {
    System.out.println("Performing selection and mutation for bottom n%");
    ArrayList<Long> fitnessList = new ArrayList<Long>();
    

    HashMap<Integer,Integer> fitnessLookup = new HashMap<Integer,Integer>();
    
    for(int w=0; w < fitnessValues.length; w++) {
      fitnessList.add(fitnessValues[w]); // REFACTOR TO REMOVE
      fitnessLookup.put(w, fitnessValues[w]);
      
    }
    
    Collections.sort(fitnessList);
    for(int w=0; w < fitnessValues.length; w++) {
      System.out.printf("%d ", fitnessList.get(w)); 
    }
    
    
    /* Defines the threshold. Anything < selectionThreshold, we mutate */
    long selectionThreshold = fitnessList.get((int) Math.ceil(fitnessValues.length * SELECT_AND_MUTATE_BOTTOM_PERCENTAGE));
    int firstInd = -1;
    int secondInd;
    
    System.out.printf("\n========Cross=========\n");
    System.out.printf("selectionThreshold: %d\n", selectionThreshold);
    
    for(int w=0; w < fitnessValues.length; w++) {
      if(fitnessList.get(w) <= selectionThreshold) {
        if(firstInd == -1) {
          firstInd = w;
        } else {
          secondInd = w;
          
          System.out.printf("Selection ind %d and %d\n", firstInd, secondInd);
          
          // TODO
          ArrayList<Individual> newIndividuals = 
              ParentCrossOver.co(population.getIndividual(firstInd), 
                                 population.getIndividual(secondInd));
            
          assert(newIndividuals.size() >= 2);
            
          population.setIndividual(firstInd, newIndividuals.get(0)); // First new individual
          population.setIndividual(secondInd, newIndividuals.get(1));
          
          firstInd = -1;  
        }
      }  
    }
  }
  
  private void selectionAndMutation() {
    System.out.println("Performing selection and mutation");
    
      for(int r = 0; r < NUMBER_SELECTION_MUTATION_ROUNDS; r++) {
      Parents pairToCross = ParentSelection.RouletteWheel(population, fitnessValues);
      
      int indFirstIndividual = pairToCross.f;
      int indSecondIndividual = pairToCross.s;
      
      System.out.printf("Chosen #1: %d and #2: %d\n", indFirstIndividual, indSecondIndividual);
      
      ArrayList<Individual> newIndividuals = 
        ParentCrossOver.co(population.getIndividual(indFirstIndividual), 
                           population.getIndividual(indSecondIndividual));
      
      assert(newIndividuals.size() >= 2);
      
      population.setIndividual(indFirstIndividual, MutateIndividual.m(newIndividuals.get(0))); // First new individual
      population.setIndividual(indSecondIndividual, MutateIndividual.m(newIndividuals.get(1))); // First new individual
    }
  }
  
  private long[] computeIndividualFitness() {
    int pSize = individuals.size();
    long[] fitnessValues = new long[pSize];
    System.out.println("Computing individual fitness"); 
    
    for(int q = 0; q < pSize; q++) {
    
      Individual i = individuals.get(q);

      Trainer x = new Trainer();
      x.setWeights(i.getWeights());
      
      fitnessValues[q] = x.runGame();
      
      for(int r = 0; r < RUN_GAME_ITERATIONS; r++){
        fitnessValues[q] = Math.max(fitnessValues[q], x.runGame());  
      }
      
      System.out.printf("setting fitnessvalues[%d] = %d \n", q, fitnessValues[q]);
      
      ResultsLog.writeTrainerToFile(x, fitnessValues[q]);
    }
     
    return fitnessValues;
  }
  
  private ArrayList<Individual> generatePopulation(int size) {
    ArrayList<Individual> population = new ArrayList<>();
    double[] randomWeights = new double[INDIVIDUAL_SIZE];
    
    for(int q = 0; q < size; q++) {
      Individual n = new Individual(INDIVIDUAL_SIZE);
      // set individual weights
      for(int w = 0; w < INDIVIDUAL_SIZE; w++) {
        randomWeights[w] = rand.nextDouble();
      }
      
      n.randomiseWeights(randomWeights);
      population.add(n);
    }
    
    return population;
  }
  
}
