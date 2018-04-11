package Genetic;
import java.util.Random;

public class Individual implements Comparable {

  public static int WEIGHT_LOWER_BOUND = -5;
  public static int WEIGHT_UPPER_BOUND = 5;

  public int size;
  private double[] weights;
  private long fitness;
  
  Random rand;
  
  public Individual() {

    this.size = Genetic.INDIVIDUAL_NUMBER_OF_HEURISTICS;
    weights = new double[size];
    this.fitness = 0;
    
    rand = new Random();
    
  }
  
  public void setFitness(long v) {
  
    fitness = v;
  
  }
  
  public long getFitness() {
  
    return fitness;
  
  }
  
  public double[] getWeights() {
  
    return weights;
  
  }
  
  public void setWeight(int pos, double value) {
   
    if (pos < 0 || pos >= size) {
      try {
        throw new Exception("Out of bound");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    
    weights[pos] = value;
  
  }
  
  public void setWeights(double[] weights) {
    this.weights = weights;
  }
  
  public void randomiseWeights(double[] smallRandomWeights) {
    for(int q = 0; q < size; q++) {
      weights[q] = smallRandomWeights[q] * (WEIGHT_UPPER_BOUND - WEIGHT_LOWER_BOUND) - WEIGHT_UPPER_BOUND;
    }
  }
  
  public void printWeights() {
    for(int q = 0; q < weights.length; q++) {
      System.out.print(weights[q] + " ");
    }
    System.out.print("\n");
  }
  
  public static void main(String[] args) {
    Individual x = new Individual();
  }

  @Override
  public int compareTo(Object i2) {
    long i2fitness = ((Individual)i2).getFitness();
    return (int)(this.fitness - i2fitness);
  }
  
  public static Individual deepCopy(Individual i) {
    Individual ni = new Individual();
    ni.setWeights(i.getWeights());
    ni.setFitness(i.getFitness());
    return ni;
  }
}
