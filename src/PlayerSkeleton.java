public class PlayerSkeleton {
    private static final Boolean VERBOSE = true;

    //implement this function to have a working system
    public int pickMove(State s, int[][] legalMoves) {
      int[][] field = s.getField();

      //printBoard(field);

      return 1;
    }

    

  public static void main(String[] args) {
    State s = new State();
    new TFrame(s);
    PlayerSkeleton p = new PlayerSkeleton();
    while(!s.hasLost()) {
      s.makeMove(p.pickMove(s,s.legalMoves()));
      //s.draw();
      //s.drawNext(0,0);
      try {
        Thread.sleep(300);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    System.out.println("You have completed "+s.getRowsCleared()+" rows.");
  }

}
