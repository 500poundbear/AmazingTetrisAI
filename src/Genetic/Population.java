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
 
  public void printPopulation() {
    for(int q = 0; q < population.size(); q++) {
      System.out.printf("%d\t", getIndividual(q).getFitness());
      if (q % 5 == 9) {
        System.out.printf("\n");
      }
    }
    System.out.printf("\n\n");
  }
  
  public static ArrayList<Individual> deepCopy (ArrayList<Individual> individuals) {
    int individualsSize = individuals.size();
    
    ArrayList<Individual> newIndividuals = new ArrayList<Individual>();
    
    for(int q = 0; q < individualsSize; q++) {
      Individual ni = new Individual();
      ni.setWeights(individuals.get(q).getWeights());
      ni.setFitness(individuals.get(q).getFitness());
      newIndividuals.add(ni);
    }
    
    return newIndividuals;
  }
}
