import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Callable;

public class RunIslandCallable implements Callable<Island>{

  Island i;

  public RunIslandCallable(Island i) {
    this.i = i;
  }

  @Override
  public Island call() throws Exception {
    
    return runIsland(this.i);
  }

  private Island runIsland(Island o) {
    System.out.printf("runIsland\n");
    Island i = new Island();
    i.addPopulation(o.getPopulation());
    
    ArrayList<Individual> newIndividuals = o.getPopulation().getPopulation();
    Population newPopulation;
    
    while(i.populationCount() < Genetic.NUMBER_OF_GENERATIONS) {
      System.out.printf("Generation %d\n", i.populationCount());
      newIndividuals = computeIslandFitness(newIndividuals);
      
      if(Genetic.DEBUG) {
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
    long selectionThreshold = sortedIndividuals.get((int) Math.ceil(individualsLength * Genetic.SELECT_AND_MUTATE_BOTTOM_PERCENTAGE)).getFitness();
    
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
    
    for(int r = 0; r < Genetic.RUN_GAME_ITERATIONS; r++){
      fitnessScore = Math.max(fitnessScore, x.runGame());
    }
    
    return fitnessScore;
  }

}
