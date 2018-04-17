public class NextState extends State {

  public NextState(State s, int[] move) {
    super();

    // Copy the current state s.
    nextPiece = s.getNextPiece();
    for (int i = 0; i < State.ROWS; i++) {
      for (int j = 0; j < State.COLS; j++) {
        getField()[i][j] = s.getField()[i][j];
      }
    }

    for (int i = 0; i < State.COLS; i++) {
      getTop()[i] = s.getTop()[i];
    }

    // Make the move.
    super.makeMove(move);
  }
}
