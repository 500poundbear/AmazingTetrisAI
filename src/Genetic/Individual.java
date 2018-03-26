package Genetic;
import java.util.Random;

public class Individual {

  private static int WEIGHT_LOWER_BOUND = -5;
  private static int WEIGHT_UPPER_BOUND = 5;

  private int size;
  private double[] weights;
  
  Random rand;
  
  public Individual(int size) {
    weights = new double[size];
    this.size = size;
    
    rand = new Random(System.currentTimeMillis());
    
    this.randomiseWeights();
  }
  
  public double[] getWeights() {
    return weights;
  }
  
  private void randomiseWeights() {
    for(int q = 0; q < size; q++) {
      weights[q] = this.rand.nextDouble() * (WEIGHT_UPPER_BOUND - WEIGHT_LOWER_BOUND) - WEIGHT_UPPER_BOUND;
    }
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
  
  public static void main(String[] args) {
    Individual x = new Individual(10);
  }
}
