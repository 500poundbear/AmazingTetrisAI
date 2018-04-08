package Genetic;

import java.util.ArrayList;

public class Population {
  ArrayList<Individual> population;
  ArrayList<Long> fitnessScores;
  
  public Population() {
    population = new ArrayList<Individual>();
    fitnessScores = new ArrayList<Long>();
  }
  
  public void setPopulation(ArrayList<Individual> population) {
    this.population = population;
  }
  
  public ArrayList<Individual> getPopulation() {
    return this.population;
  }
  
  public Individual getIndividual(int ind) {
    if(ind < 0 || ind > population.size()) {
      throw new Error("index specified is out of bounds");
    }
    
    return population.get(ind);
  }
  
  public void setIndividual(int ind, Individual newIndividual) {
    this.population.set(ind, newIndividual);
  }
  
  public void setFitnessScores(long[] scores) {
    for(int q = 0; q < scores.length; q++) {
      fitnessScores.add(scores[q]);
    }
  }
  
  public ArrayList<Long> getFitnessScores() {
    return fitnessScores;
  }
}
