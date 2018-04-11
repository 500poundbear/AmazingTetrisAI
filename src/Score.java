/*
 * Heuristics.Score calculates the heuristic score based on the field (2D array)
 * given.
 */


import java.util.ArrayList;

public class Score{
  private double[] weights;
  private ArrayList<Integer> heuristics;
  
  public Score(int[][] field, double[] weights) {
    Heuristic h = new Heuristic(field);
    
    heuristics = new ArrayList<Integer>();
    heuristics.add(h.maximumColumnHeight());
    heuristics.add(h.minimumColumnHeight());
    heuristics.add(h.cumulColumnHeightDiff());
    heuristics.add(h.numberofHoles());
    heuristics.add(h.rangeColumnHeight());
    heuristics.add(h.cumulativeColumnHeight());
    heuristics.add(h.rowsCleared());
    heuristics.add(h.numberColumnsWithHoles());
    heuristics.add(h.numberColumnsWithBigRecesses());
    heuristics.add(h.cumulativePits());
    
    this.weights = weights;
  }
  
  /*
   * Multiply each heuristic value by it's corresponding weight value
   */
  public double getScore() {
    double score = 0;
    if (heuristics.size() > weights.length) throw new Error("Insufficient weights specified");
    
    for(int q = 0; q < heuristics.size(); q++) {
      score += weights[q] * heuristics.get(q);
    }
    
    return score;
  }
}
