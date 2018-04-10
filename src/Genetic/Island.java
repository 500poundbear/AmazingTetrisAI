package Genetic;

import java.util.ArrayList;

import Engine.ResultsLog;

public class Island {
  ArrayList<Population> populations;
  
  public Island() {
    populations = new ArrayList<Population>();
  }
  
  public void addPopulation(Population p) {
    populations.add(p);
    
    ResultsLog.writePopulationToFile(populations.size(), 
                                     getMaximumFitnessScore(p), 
                                     getMinimumFitnessScore(p), 
                                     getMeanFitnessScore(p));
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
    ArrayList<Long> fitnessScores = p.getFitnessScores();
    
    long maxValue = Long.MIN_VALUE;
    for(int q = 0; q < fitnessScores.size(); q++) {
      maxValue = (long) Math.max(maxValue, fitnessScores.get(q));
    }
    
    return maxValue;
  }
  
  public long getMinimumFitnessScore(Population p) {
    ArrayList<Long> fitnessScores = p.getFitnessScores();
    
    long minValue = Long.MAX_VALUE;
    
    for(int q = 0; q < fitnessScores.size(); q++) {
      minValue = Math.min(minValue,  fitnessScores.get(q));
    }
    
    return minValue;
  }
  
  public double getMeanFitnessScore(Population p) {
    ArrayList<Long> fitnessScores = p.getFitnessScores();
    
    long accum = 0;
    
    for(int q = 0; q < fitnessScores.size(); q++) {
      accum += fitnessScores.get(q);
    }
    
    return (double)accum/fitnessScores.size();
  }
}
