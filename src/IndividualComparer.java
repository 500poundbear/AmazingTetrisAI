

import java.util.Comparator;

public class IndividualComparer implements Comparator<Individual> {
  @Override
  public int compare(Individual x, Individual y) {
    return (int) (x.getFitness() - y.getFitness());
  }
}