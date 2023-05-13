package pl.kamiloglecki.SlidingPuzzle;

import sac.State;

import sac.StateFunction;

public class MisplacedTiles extends StateFunction {     //heurystyka Misplaced Tiles
    public double calculate(State state) {
        SlidingPuzzle slidingPuzzle = (SlidingPuzzle) state;
        byte[][] board = slidingPuzzle.getBoard();
        byte k = 0;
        double counter = 0;
        for(int i=0; i<slidingPuzzle.n; i++) {
            for (int j=0; j<slidingPuzzle.n; j++, k++) {
                if(board[i][j] != 0 && board[i][j] != k)
                    counter++;
            }
        }
        return counter;
    }
}
