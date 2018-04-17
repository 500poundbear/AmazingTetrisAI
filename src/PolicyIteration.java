import Jama.*;

public class PolicyIteration {
  private static final Boolean VERBOSE = false;
  private static final int HEURISTICS_COUNT = 10;
  private static final int GAMES_PER_ITERATION = 40;
  private static final int STATES_PER_GAME = 20000;

  private static State[][] games = new State[GAMES_PER_ITERATION][STATES_PER_GAME];
  private static double[][] rewards = new double[GAMES_PER_ITERATION][STATES_PER_GAME];
  private static double[] rt = new double[HEURISTICS_COUNT];
  private static double lambda = 0.0;

  public static double[] getNext(double[] input_rt) {
    // System.out.printf(input_rt.length);
    for (int i = 0; i < HEURISTICS_COUNT; i++) {
			rt[i] = input_rt[i];
		}
    int game_i = 0;
		int state_i = 0;
		while (true) {
			game_i += 1;
			state_i = 0;
			if (game_i == GAMES_PER_ITERATION) {
				lambdaPolicyIteration();
        return rt;
			}
      State s = new State();
			// new TFrame(s);
			while(!s.hasLost()) {
				int[] move = pickMove(s,s.legalMoves());
				int oldCleared = s.getRowsCleared();
				NextState nextState = new NextState(s, move);
				s.makeMove(move);
				int newCleared = s.getRowsCleared();
				double reward = (double) (newCleared - oldCleared);
				if (state_i < STATES_PER_GAME) {
					games[game_i][state_i] = nextState;
					rewards[game_i][state_i] = reward;
				}
				state_i += 1;
				if (s.hasLost() && state_i < STATES_PER_GAME) games[game_i][state_i] = null;
				// s.draw();
				// s.drawNext(0,0);
			}
      // System.out.printf("Game %d reward %d \n", game_i, s.getRowsCleared());
    }
  }

  public static int[] pickMove(State s, int[][] legalMoves) {
    int[][] field = s.getField();

    // For each s, see what happens when we perform some legal Move
    int[] optimal_move = s.legalMoves()[0];
    double optimal_value = -1000000;
    for (int i = 0; i < s.legalMoves().length; i++) {
      int[] move = s.legalMoves()[i];
      NextState nextState = new NextState(s, move);
      int reward = nextState.getRowsCleared();
      double utility = J(nextState, rt);
      double value = reward + utility;
      if (value > optimal_value) {
        optimal_move = move;
        optimal_value = value;
      }
    }
    return optimal_move;
  }

  private static void lambdaPolicyIteration() {
    // System.out.println("Policy Iteration \n");
    // System.out.printf("r_t: ");
    // for (int j = 0; j < HEURISTICS_COUNT; j++) {
    //   System.out.printf("%.2f ", rt[j]);
    // }
    // System.out.println();
    int total_states = 0;
    for (int g = 0; g < games.length; g++) {
      for (int i = 0; i < games[g].length; i++) {
        if (games[g][i] == null) break;
        total_states += 1;
      }
    }
    double[][] X = new double[total_states][HEURISTICS_COUNT];
    double[] Y = new double[total_states];
    int i = 0;
    for (int m = 0; m < games.length; m++) {
      State[] game = games[m];
      for (int k = 0; k < game.length; k++) {
        if (game[k] == null) break;
        State state = game[k];
        double[] f = features(state);
        for (int j = 0; j < HEURISTICS_COUNT; j++) X[i][j] = f[j];
        Y[i] = J(state, rt);
        for (int s = k; s < game.length - 1; s++) {
          if (game[s+1] == null) break;
          Y[i] += Math.pow(lambda, s - k) * (rewards[m][k] + J(game[s+1], rt) - J(game[s], rt));
        }
        i += 1;
      }
    }

    Matrix noise = Matrix.identity(HEURISTICS_COUNT, HEURISTICS_COUNT).times(0.0000001);
    Matrix mX = new Matrix(X, total_states, HEURISTICS_COUNT);
    Matrix mY = new Matrix(Y, total_states);
    Matrix new_r = mX.transpose().times(mX).plus(noise).inverse().times(mX.transpose()).times(mY);
    double[][] array_r = new_r.getArray();
    // System.out.printf("r_t+1: ");
    // for (int j = 0; j < HEURISTICS_COUNT; j++) {
    //   rt[j] = array_r[j][0];
    //   System.out.printf("%.2f ", rt[j]);
    // }
    // System.out.println();
  }

  private static double[] features(State s) {
    Heuristic h = new Heuristic(s.getField());

    double[] heuristics = new double[HEURISTICS_COUNT];
    heuristics[0] = h.maximumColumnHeight();
    heuristics[1] = h.minimumColumnHeight();
    heuristics[2] = h.cumulColumnHeightDiff();
    heuristics[3] = h.numberofHoles();
    heuristics[4] = h.rangeColumnHeight();
    heuristics[5] = h.cumulativeColumnHeight();
    heuristics[6] = h.rowsCleared();
    heuristics[7] = h.numberColumnsWithHoles();
    heuristics[8] = h.numberColumnsWithBigRecesses();
    heuristics[9] = h.cumulativePits();

    return heuristics;
  }

  private static double J(State s, double[] r) {
    double[] f = features(s);
    double J = 0;
    for (int i = 0; i < HEURISTICS_COUNT; i++) {
      J += f[i] * r[i];
    }
    return J;
  }
}
