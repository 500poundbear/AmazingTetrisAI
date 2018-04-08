package Genetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Islands {
  
  public static double TOP_INDIVIDUALS_PERCENTAGE = 0.2;
  
  public static ArrayList<Island> redistribute(ArrayList<Island> islands) {
    
    int islandsSize = islands.size();
    int topPerformersSize;   
    
    ArrayList<Individual> topPerformers = new ArrayList<Individual>();
    ArrayList<Individual> allIndividuals = new ArrayList<Individual>();
    
    ArrayList<Island> newIslands = new ArrayList<Island>();
    
    // Collect a list of top performers from each island
    
    for(int q = 0; q < islandsSize; q++) {
      topPerformers.addAll(returnTopN(islands.get(q)));
    }
    
    topPerformersSize = topPerformers.size();
    
    System.out.printf("For %d islands, we have %d top performers.\n", islandsSize, topPerformersSize);
    
    // For all the top performers, we generate random mutations of them to     
    
    int individualsToGenerate = (int) ((Genetic.ISLAND_SIZE * Genetic.POPULATION_SIZE) - topPerformersSize);
    
    Random rand = new Random();
    
    for(int q = 0; q < individualsToGenerate; q++) {
      Individual n = topPerformers.get(rand.nextInt(topPerformersSize));
      allIndividuals.add(MutateIndividual.m(n));
    }
    
    allIndividuals.addAll(topPerformers);
    Collections.shuffle(allIndividuals);
    
    assert(allIndividuals.size() == (Genetic.ISLAND_SIZE * Genetic.POPULATION_SIZE));
    
    // Assign individuals back into populations and islands
    
    for(int q = 0; q < Genetic.ISLAND_SIZE; q++) {
      Island ni = new Island();
      
      Population p = new Population();
      ArrayList<Individual> population = new ArrayList<Individual>();
      
      int lb = q*Genetic.POPULATION_SIZE;
      int ub = (q + 1)*Genetic.POPULATION_SIZE;
      for(int pt = lb; pt < ub; pt++) {
        population.add(allIndividuals.get(pt));
      }
      
      ni.setPopulation(p);
      newIslands.add(ni);
    }
    
    return newIslands;
  }
  
  private static ArrayList<Individual> returnTopN(Island island) {
  
    ArrayList<Individual> topPerformers = new ArrayList<Individual>();
  
    Population islandPopulation = island.getPopulation();
    ArrayList<Individual> individuals = islandPopulation.getPopulation();
    ArrayList<Long> scores = islandPopulation.getFitnessScores();
    Collections.sort(scores);
    
    long threshold = scores.get((int) Math.floor((1 - TOP_INDIVIDUALS_PERCENTAGE) * scores.size()));
    
    for(int q = 0; q < individuals.size(); q++) {
      // if score is above threshold, add to new arraylist and return
      if (scores.get(q) >= threshold) {
        topPerformers.add(individuals.get(q));
      }
    }
  
    return topPerformers;
  }
}
