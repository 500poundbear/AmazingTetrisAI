

import java.util.Arrays;

public class Heuristic {
  private static boolean VERBOSE = false;
  
  private int[][] field;
  private int[] top;
  private int rows;
  private int cols;

  public Heuristic(int[][] field) {
    this.field = field;
    rows = field.length;
    cols = field[0].length;
    
    top = new int[cols];
    
    for(int c = 0; c < cols; c++) {
      top[c] = columnHeight(c);
    }
  }
  
  public int maximumColumnHeight() {
    int maxHeight = -1;
    for(int c = 0; c < cols; c++) {
      maxHeight = Math.max(maxHeight, top[c]);
    }
    
    if (VERBOSE) {
      System.out.printf("maximum column height: %d\n", maxHeight);
    }
    return maxHeight;
  }
  
  public int minimumColumnHeight() {
    int minHeight = top[0];
    for(int c = 1; c < cols; c++) {
      minHeight = Math.min(minHeight, top[c]);
    }
    
    minHeight += 1; // To get an output [0, State.ROWS - 1]
    
    if (VERBOSE) {
      System.out.printf("minimum column height: %d\n", minHeight);
    }
    return minHeight; 
  }

  public int cumulColumnHeightDiff() {
    int diffHeight = 0;
    for(int c = 1; c < cols; c++) {
      diffHeight += Math.abs(top[c] - top[c - 1]);
    }
    
    if (VERBOSE) {
      System.out.printf("culm sum of column difference: %d\n", diffHeight);
    }
    return diffHeight;
  }

  /*
   * Returns the number of 0 fields that are not in the top column
   */
  public int numberofHoles() {
    int numHoles = 0;
    for(int c = 0; c < cols; c++) {
      for(int r = top[c] - 1; r >=0; r--) {
        if (field[r][c] == 0) numHoles++;
      }
    }
    
    if (VERBOSE) {
      System.out.printf("number of holes: %d\n", numHoles);
    }
    
    return numHoles;
  }

  /*
   * Returns the absolute difference between the highest and lowest column
   */
  public int rangeColumnHeight() {
    int maxHeight = -1;
    int minHeight = Integer.MAX_VALUE;
    
    for(int c = 0; c < cols; c++) {
      maxHeight = Math.max(maxHeight, top[c]);
      minHeight = Math.min(minHeight, top[c]);
    }
    
    int absoluteDifference = Math.abs(maxHeight - minHeight);
    if (VERBOSE) {
      System.out.printf("abs(highest - lowest): %d\n", absoluteDifference);
    }
    return absoluteDifference;
  }

  public int cumulativeColumnHeight() {
    int heightSum = 0;
    for(int c = 0; c < cols; c++) {
      heightSum = Math.max(heightSum, top[c]);
    }
    
    if (VERBOSE) {
      System.out.printf("total column height: %d\n", heightSum);
    }
    return heightSum;
  }
  
  public int rowsCleared() {
    int rowsCleared = 0;
    boolean rowFull;
    for(int r = 0; r < rows; r++) {
      rowFull = true;
      for(int c = 0; c < cols; c++) {
        if (field[r][c] == 0) {
          rowFull = false;
          break;
        }
      }
      if (rowFull) rowsCleared++;
    }
    
    if (VERBOSE) {
      System.out.printf("total rows cleared: %d\n", rowsCleared);
    }
    return rowsCleared;
  }
  
  /*
   * A hole is defined to be an empty block that has a filled block
   * in the same row
   */
  
  public int numberColumnsWithHoles() {
    int numColumnsWithHoles = 0;
    
    for(int c = 0; c < cols; c++) {
      for(int r = top[c] - 1; r >=0; r--) {
        if (field[r][c] == 0) {
          numColumnsWithHoles++;
          break;
        }
      }
    }
    
    if (VERBOSE) {
      System.out.printf("Number of columns w holes: %d\n", numColumnsWithHoles);
    }
    
    return numColumnsWithHoles;
  }
  
  /*
   * A big recess is defined to be an area of more than 2 adjacent empty tiles 
   * that are contained - meaning 
   *        e.g  BBB              BBBB             BBBB
   *             B B              BB B              BB
   *             B B              B  B              BB
   *             BBB              BBBB              BB
   *          counts as 1      counts as 1      counts as 0
   *          
   * Done by flood filling grid
   */
   
  public int numberColumnsWithBigRecesses() {
    // Flood the entire field starting from an empty cell [20, 0]
    int[][] floodField = deepCopy(field);
    // Find holes, fill them up and increase count every time floodfill is called.
    int fillCount = 0;
    
    floodfill(floodField, 20, 0);
    
    for(int r = 0; r < rows; r++) {
      for(int c = 0; c < cols; c++) {
        if (floodField[r][c] == 0) {
          floodfill(floodField, r, c);
          fillCount++;    
        }
      }
    }
    
    if (VERBOSE) {
      System.out.printf("Number of big recesses: %d\n", fillCount);
    }
    
    return fillCount;
  }
  
  /*
   * A pit is found at the highest filled point of a column if it is at least two pieces
   * shorter than its adjacent neighbour's highest filled point
   */
  public int cumulativePits() {
    int pitCount = 0;
    for(int c = 0; c < cols; c++) {
      int leftIndex = Math.max(c - 1, 0);
      int rightIndex = Math.min(c + 1, cols - 1);
      
      if ((top[leftIndex] - top[c]) >= 2 &&
          (top[rightIndex] - top[c]) >= 2) pitCount++;
    }
    
    if (VERBOSE) {
      System.out.printf("Number of pits: %d\n", pitCount);
    }
    
    return pitCount;
  }

  private int columnHeight(int c) {
    int r = -1;
    for(r = rows - 1; r >=0; r--) {
      if (field[r][c] != 0) {
        break;
      }
    }
    return r;
  }
  
  private void floodfill(int[][] ffield, int row, int col) {
    if (row < 0 || row > rows - 1 || col < 0 || col >= cols) return;
    if(ffield[row][col] != 0) return;
    
    ffield[row][col] = Integer.MAX_VALUE;
    
    floodfill(ffield, row + 1, col);
    floodfill(ffield, row - 1, col);
    floodfill(ffield, row, col + 1);
    floodfill(ffield, row, col - 1);
  }
  
  /*
   * TODO: Refactor duplicate function
   */
  public static int[][] deepCopy(int[][] a) {
    if (a == null) return null;
    
    final int[][] c = new int[a.length][];
    
    for(int q = 0; q < a.length; q++) {
      c[q] = Arrays.copyOf(a[q],  a[q].length);
    }
    
    return c;
  }
}
