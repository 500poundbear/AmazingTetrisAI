

import java.util.ArrayList;

public class Island {
  ArrayList<Population> populations;
  
  public Island() {
    populations = new ArrayList<Population>();
  }
  
  public void addPopulation(Population p) {
    populations.add(p);
  }

  /*
   * Get the latest population stored
   */
  public Population getPopulation() {
    return populations.get(populations.size() - 1);
  }
  
  public long populationCount() {
    return populations.size();
  }
  
  /* 
   * Functions to return queries about the population
   */
  
  public long getMaximumFitnessScore(Population p) {
    
    ArrayList<Individual> individuals = p.getPopulation();
    long maxValue = Long.MIN_VALUE;
    for(int q = 0; q < individuals.size(); q++) {
      maxValue = (long) Math.max(maxValue, individuals.get(q).getFitness());
    }
    
    return maxValue;
  }
  
  public long getMinimumFitnessScore(Population p) {

    ArrayList<Individual> individuals = p.getPopulation();
    long minValue = Long.MAX_VALUE;    
    for(int q = 0; q < individuals.size(); q++) {
      minValue = Math.min(minValue,  individuals.get(q).getFitness());
    }
    
    return minValue;
  }
  
  public double getMeanFitnessScore(Population p) {
    
    ArrayList<Individual> individuals = p.getPopulation();
    long accum = 0;
    
    for(int q = 0; q < individuals.size(); q++) {
      accum += individuals.get(q).getFitness();
    }
    
    return (double)accum/individuals.size();
  }
}
