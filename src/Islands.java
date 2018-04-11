

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Islands {
  
  public static double TOP_INDIVIDUALS_PERCENTAGE = 0.2;
  
  public static ArrayList<Island> redistribute(ArrayList<Island> islands) {
    
    Random rand = new Random();  
    
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
    
    for(int q = 0; q < topPerformersSize; q++) {
      ResultsLog.writeBestNodes(topPerformers.get(q).getWeights(), topPerformers.get(q).getFitness());
      System.out.printf("%d\t", topPerformers.get(q).getFitness());
    }
    System.out.printf("\n");
    
    // For all the top performers, we generate random mutations of them to     
    
    int individualsToGenerate = (int) ((Genetic.NUMBER_OF_ISLANDS * Genetic.ISLAND_NUMBER_OF_INDIVIDUALS) - topPerformersSize);
    
    for(int q = 0; q < individualsToGenerate; q++) {
      Individual n = topPerformers.get(rand.nextInt(topPerformersSize));
      Individual mutatedI = MutateIndividual.m(n);
      allIndividuals.add(mutatedI);
    }
    
    allIndividuals.addAll(topPerformers);
    
    Collections.shuffle(allIndividuals);
    
    assert(allIndividuals.size() == (Genetic.NUMBER_OF_ISLANDS * Genetic.ISLAND_NUMBER_OF_INDIVIDUALS));

    // Assign individuals back into populations and islands
    
    for(int q = 0; q < Genetic.NUMBER_OF_ISLANDS; q++) {
      Island ni = new Island();
      
      Population p = new Population();
      ArrayList<Individual> population = new ArrayList<Individual>();
      
      int lb = q*Genetic.ISLAND_NUMBER_OF_INDIVIDUALS;
      int ub = (q + 1)*Genetic.ISLAND_NUMBER_OF_INDIVIDUALS;
      for(int pt = lb; pt < ub; pt++) {
        
        population.add(allIndividuals.get(pt));
      }
      p.setPopulation(population);
      ni.addPopulation(p);
      
      newIslands.add(ni);
    }
    
    return newIslands;
  }
  
  @SuppressWarnings("unchecked")
  private static ArrayList<Individual> returnTopN(Island island) {
    ArrayList<Individual> topPerformers = new ArrayList<Individual>();
  
    Population islandPopulation = island.getPopulation();
    ArrayList<Individual> individuals = islandPopulation.getPopulation();
    
    Collections.sort(individuals);
    
    int lb = ((int) Math.floor((1 - TOP_INDIVIDUALS_PERCENTAGE) * individuals.size()));
    
    for(int q = lb; q < individuals.size(); q++) {
      topPerformers.add(individuals.get(q));
    }
  
    return topPerformers;
  }

  public static void logIslands(long cycle, ArrayList<Island> islands) {
    
    for(int q = 0; q < islands.size(); q++) {
      Island x = islands.get(q);
      ArrayList<Individual> y = x.getPopulation().getPopulation();
      
      for(int e = 0; e < y.size(); e++) {
        ResultsLog.writeTrainingRound(cycle, q, y.get(e).getWeights(), y.get(e).getFitness());
      }
    }
  }
}
