package Genetic;
import java.util.Random;

public class Individual {

  public static int WEIGHT_LOWER_BOUND = -5;
  public static int WEIGHT_UPPER_BOUND = 5;

  public int size;
  private double[] weights;
  
  Random rand;
  
  public Individual(int size) {
    weights = new double[size];
    this.size = size;
    
    rand = new Random();
    
  }
  
  public double[] getWeights() {
    return weights;
  }
  
  public void randomiseWeights(double[] smallRandomWeights) {
    for(int q = 0; q < size; q++) {
      weights[q] = smallRandomWeights[q] * (WEIGHT_UPPER_BOUND - WEIGHT_LOWER_BOUND) - WEIGHT_UPPER_BOUND;
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
  
  public void setWeights(double[] weights) {
    this.weights = weights;
  }
  
  public void printWeights() {
    for(int q = 0; q < weights.length; q++) {
      System.out.print(weights[q] + " ");
    }
    System.out.print("\n");
  }
  
  public static void main(String[] args) {
    Individual x = new Individual(10);
  }
}
