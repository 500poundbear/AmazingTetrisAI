package Genetic;
/*
 * Traditional genetic algorithm implementation
 */

import Engine.Trainer;
import Engine.ResultsLog;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Genetic{

  Random rand;
 
  long cyclesCount = 0;
  public static final int CYCLES = 500;
  public static final int NUMBER_OF_ISLANDS = 100;
  public static final int NUMBER_OF_GENERATIONS = 50;
  public static final int ISLAND_NUMBER_OF_INDIVIDUALS = 100;
  public static final int INDIVIDUAL_NUMBER_OF_HEURISTICS = 10;
  public static final int RUN_GAME_ITERATIONS = 10; 
  private static final double SELECT_AND_MUTATE_BOTTOM_PERCENTAGE = 0.4;

  private static final boolean DEBUG = true;

  public Genetic() {
    
    rand = new Random(System.currentTimeMillis());
    
    runAlgorithm();
  }
  
  private void runAlgorithm() {
    ArrayList<Island> islands = new ArrayList<Island>();
    // Round 0 Initialise Individuals
    for(int q = 0; q < NUMBER_OF_ISLANDS; q++) {
      Population np = new Population();
      np.setPopulation(generatePopulation());
      Island x = new Island();
      x.addPopulation(np);
      islands.add(x); 
    }
     
    while(cyclesCount < CYCLES) {
          
      for(int q = 0; q < NUMBER_OF_ISLANDS; q++) {
        
        Population pp = islands.get(q).getPopulation();
        
        if(DEBUG) {
          System.out.printf("\nISLAND #%d\n", q + 1);
        }
        
        islands.set(q, runIsland(pp));
      }
      
      System.out.printf("logging islands");
      Islands.logIslands(cyclesCount, islands);
      
      islands = Islands.redistribute(islands);   
      System.out.printf("\n===================\n");
      
      cyclesCount++;    
       
    }
  }
  
  /*
   * Every island can store "generations" of population weights.
   * 
   * Returns a new island containing a series of snapshots of popultion
   */
  private Island runIsland(Population p) {
    System.out.printf("runIsland\n");
    Island i = new Island();
    i.addPopulation(p);
    
    ArrayList<Individual> newIndividuals = p.getPopulation();
    Population newPopulation;
    
    while(i.populationCount() < NUMBER_OF_GENERATIONS) {
      System.out.printf("Generation %d\n", i.populationCount());
      newIndividuals = computeIslandFitness(newIndividuals);
      
      if(DEBUG) {
        for(int q = 0; q < newIndividuals.size(); q++) {
          System.out.printf("fitness[%d] = %d\n", q, newIndividuals.get(q).getFitness());
        }
      }
      
      newIndividuals = selectAndMutateBottomX(newIndividuals);
      
      newPopulation = new Population();
      newPopulation.setPopulation(newIndividuals);  
      i.addPopulation(newPopulation);
    }
    
    return i;
  }
  
  /* 
   * selectAndMutateBottomX takes an arraylist of individuals
   * sort them
   * construct new set of individuals
   * return
   */
  @SuppressWarnings("unchecked")
  private ArrayList<Individual> selectAndMutateBottomX(ArrayList<Individual> individuals) {
    // Sort Individuals
    ArrayList<Individual> sortedIndividuals = Population.deepCopy(individuals);
    Collections.sort(sortedIndividuals);  
    
    individuals = filterCross(individuals, sortedIndividuals);
    return individuals;
  }
  
  /*
   * Performs the filter based on a percentile value 
   * 
   * Returns another arraylist with changes
   */
  private ArrayList<Individual> filterCross(ArrayList<Individual> individuals, ArrayList<Individual> sortedIndividuals) {
    
    int individualsLength = individuals.size();
    System.out.printf("individualsLength: %d\n", individualsLength);
    // Defines the threshold. Anything < selectionThreshold, we mutate 
    long selectionThreshold = sortedIndividuals.get((int) Math.ceil(individualsLength * SELECT_AND_MUTATE_BOTTOM_PERCENTAGE)).getFitness();
    
    int firstInd = -1;
    int secondInd;
    
    for(int w=0; w < individualsLength; w++) {
      if(individuals.get(w).getFitness() <= selectionThreshold) {
        
        if(firstInd == -1) {
          firstInd = w;
        } else {
          secondInd = w;
          
          //System.out.printf("Selection ind %d and %d\n", firstInd, secondInd);
          
          ArrayList<Individual> newIndividuals = 
              ParentCrossOver.co(individuals.get(firstInd), 
                                 individuals.get(secondInd));
            
          individuals.set(firstInd, newIndividuals.get(0));
          individuals.set(secondInd, newIndividuals.get(1));
          
          firstInd = -1;  
        }
      }  
    }
    
    return individuals;
  }
  
  /*
   * Given an ArrayList of individuals, compute their fitness scores
   * 
   * Returns an array with their fitness scores
   */
  private ArrayList<Individual> computeIslandFitness(ArrayList<Individual> individuals) {
    System.out.printf("computeIslandFitness\n");
    ArrayList<Individual> newIndividuals = new ArrayList<Individual>();
    
    int individualsSize = individuals.size();
    
    for(int q = 0; q < individualsSize; q++) {
      long fitnessValue = computeIndividualFitness(individuals.get(q));
      
      // We reinitialise an Individual so that their values are independent
      newIndividuals.add(createNewIndividualFitness(individuals.get(q), fitnessValue));
    }
    
    return newIndividuals;
  }
  
  /* 
   * Given an individual and fitnessScore, return an new individual with the fitnessScore
   */
  private Individual createNewIndividualFitness(Individual i, long v) {
    Individual n = new Individual();
    n.setWeights(i.getWeights());
    n.setFitness(v);
    return n;
  }
  
  /*
   * Given an Individual instance with weights, copy the weights
   * to a Trainer instance and run game simulation with the weights.
   * 
   * Return the highest score obtained.
   */
  private long computeIndividualFitness(Individual i) {
    
    long fitnessScore = Long.MIN_VALUE;
    
    Trainer x = new Trainer();
    x.setWeights(i.getWeights()); /* Apply individual's weights to trainer */
    
    for(int r = 0; r < RUN_GAME_ITERATIONS; r++){
      fitnessScore = Math.max(fitnessScore, x.runGame());
    }
    
    return fitnessScore;
  }
  
  /*
   * Generates a list of N individuals with M weights
   * N: ISLAND_NUMBER_OF_INDIVIDUALS 
   * M: INDIVIDUAL_NUMBER_OF_HEURISTICS
   */
  private ArrayList<Individual> generatePopulation() {
    ArrayList<Individual> population = new ArrayList<>();
    double[] randomWeights = new double[INDIVIDUAL_NUMBER_OF_HEURISTICS];
    
    for(int q = 0; q < ISLAND_NUMBER_OF_INDIVIDUALS; q++) {
      Individual n = new Individual();
      
      for(int w = 0; w < INDIVIDUAL_NUMBER_OF_HEURISTICS; w++) {
        randomWeights[w] = rand.nextDouble();
      }
      
      n.randomiseWeights(randomWeights);
      population.add(n);
      
    }
    
    return population;
  }
  
  public static void main(String[] args) {
    new Genetic();
  }
  
}
