package pl.kamiloglecki.SlidingPuzzle;

import sac.State;
import sac.StateFunction;

public class Manhattan extends StateFunction {      //heurystyka Manhattan
    public double calculate(State state) {
        SlidingPuzzle slidingPuzzle = (SlidingPuzzle) state;
        byte [][] board = slidingPuzzle.getBoard();
        double counter = 0;
        byte k = 0;
        for(int i=0; i<slidingPuzzle.n; i++)
            for(int j=0; j<slidingPuzzle.n; j++, k++)
                if(board[i][j] != 0 && board[i][j] != k)
                    counter += manhattan(slidingPuzzle.n, board[i][j], i, j);
        return counter;
    }

    private double manhattan(int boardSize, byte index, int i0, int j0) {   //index = numer klocka dla którego szukamy miejsca
        byte m = 0;
        int i1 = 0, j1 = 0;
        indexFinder:
        for(int i=0; i<boardSize; i++)
            for(int j=0; j<boardSize; j++, m++)
                if(m == index) {
                    i1 = i;
                    j1 = j;
                    break indexFinder;
                }
        return Math.abs(i1-i0) + Math.abs(j1-j0);
    }
}
