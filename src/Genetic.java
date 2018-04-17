import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Genetic{

  Random rand;
 
  long cyclesCount = 0;
  static final int CYCLES = 10;
  static final int NUMBER_OF_ISLANDS = 5;
  static final int NUMBER_OF_GENERATIONS = 5;
  static final int ISLAND_NUMBER_OF_INDIVIDUALS = 80;
  static final int INDIVIDUAL_NUMBER_OF_HEURISTICS = 10;
  static final int RUN_GAME_ITERATIONS = 1; 
  static final double SELECT_AND_MUTATE_BOTTOM_PERCENTAGE = 0.4;

  static final boolean DEBUG = true;

  public Genetic() throws InterruptedException, ExecutionException {
    
    rand = new Random(System.currentTimeMillis());
    
    runAlgorithm();
  }
  
  private void runAlgorithm() throws InterruptedException, ExecutionException {
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
      System.out.println("Creating Executor Service...");
      ExecutorService es = Executors.newCachedThreadPool();
 
      ArrayList<Future<Island>> islandResults = new ArrayList<Future<Island>>();
 
      for(int q = 0; q < NUMBER_OF_ISLANDS; q++) {
        
        if(DEBUG) {
          System.out.printf("\nISLAND #%d\n", q + 1);
        }
        
        Future<Island> result = es.submit(new RunIslandCallable(islands.get(q)));
        islandResults.add(result);
        
      }
      
      System.out.printf("OK TIME FOR THE RESULTS");
      
      for(int q = 0; q < NUMBER_OF_ISLANDS; q++) {
        Island p = islandResults.get(q).get();
        
        islands.set(q, p);
      }
      
      
      Islands.logIslands(cyclesCount, islands);
      
      islands = Islands.redistribute(islands);   
      System.out.printf("\n===================\n");
      
      cyclesCount++;    
    
    }
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
  
  public static void main(String[] args) throws InterruptedException, ExecutionException {
    new Genetic();
  }
  
}
