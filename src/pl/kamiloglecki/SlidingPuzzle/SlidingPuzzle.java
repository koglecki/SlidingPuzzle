package pl.kamiloglecki.SlidingPuzzle;

import sac.graph.*;

import java.util.*;

public class SlidingPuzzle extends GraphStateImpl {
    Random random = new Random();
    private byte[][] board;
    private static final byte up = 0, right = 1, down = 2, left = 3;
    private byte zeroX;    //miejsce w układance, gdzie znajduje się puste miejsce
    private byte zeroY;

    //wartości do zmieniania
    public static final int n = 3;  //długość boku układanki
    private static final byte mode = 1;     //0 wykonuje test porównawczy heurystyk
                                            //1 wykonuje określoną ilość zamieszań, a następnie rozwiązuje układankę
    private static final int a = 100;       //ilość układanek do testów
    // ----------------------------------

    public static void main(String args[]) {
        switch(mode) {
            case 0:
                int[] openedMisplacedTiles = new int[a];
                int[] closedMisplacedTiles = new int[a];
                int[] timeMisplacedTiles = new int[a];
                int[] pathLengthMisplacedTiles = new int[a];

                int[] openedManhattan = new int[a];
                int[] closedManhattan = new int[a];
                int[] timeManhattan = new int[a];
                int[] pathLengthManhattan = new int[a];

                for (int i = 0; i < a; i++) {
                    SlidingPuzzle p = new SlidingPuzzle();
                    p.shuffle(1000);
                    SlidingPuzzle p1 = new SlidingPuzzle(p);

                    SlidingPuzzle.setHFunction(new MisplacedTiles());
                    GraphSearchAlgorithm algo = new AStar(p);
                    algo.execute();
                    GraphState solution = algo.getSolutions().get(0);
                    openedMisplacedTiles[i] = algo.getOpenSet().size();
                    closedMisplacedTiles[i] = algo.getClosedStatesCount();
                    timeMisplacedTiles[i] = (int) algo.getDurationTime();
                    pathLengthMisplacedTiles[i] = (int) solution.getG();

                    SlidingPuzzle.setHFunction(new Manhattan());
                    GraphSearchAlgorithm algo1 = new AStar(p1);
                    algo1.execute();
                    GraphState solution1 = algo1.getSolutions().get(0);
                    openedManhattan[i] = algo1.getOpenSet().size();
                    closedManhattan[i] = algo1.getClosedStatesCount();
                    timeManhattan[i] = (int) algo1.getDurationTime();
                    pathLengthManhattan[i] = (int) solution1.getG();
                }
                System.out.println("\nMisplaced Tiles (average values):\n");
                System.out.println("Open: " + SlidingPuzzle.mean(openedMisplacedTiles));
                System.out.println("Closed: " + SlidingPuzzle.mean(closedMisplacedTiles));
                System.out.println("Time [ms]: " + SlidingPuzzle.mean(timeMisplacedTiles));
                System.out.println("Path length: " + SlidingPuzzle.mean(pathLengthMisplacedTiles));
                System.out.println("\n-----\n");

                System.out.println("Manhattan (average values):\n");
                System.out.println("Open: " + SlidingPuzzle.mean(openedManhattan));
                System.out.println("Closed: " + SlidingPuzzle.mean(closedManhattan));
                System.out.println("Time [ms]: " + SlidingPuzzle.mean(timeManhattan));
                System.out.println("Path length: " + SlidingPuzzle.mean(pathLengthManhattan));
                break;
            case 1:
                SlidingPuzzle p = new SlidingPuzzle();
                p.shuffle(1000);

                SlidingPuzzle.setHFunction(new Manhattan());
                GraphSearchAlgorithm algo = new AStar(p);
                algo.execute();
                GraphState solution = algo.getSolutions().get(0);

                List<GraphState> path = solution.getPath();
                Iterator<GraphState> iterator = path.iterator();
                while (iterator.hasNext()) {
                    System.out.println(iterator.next() + "-----");
                }

                System.out.println(solution.getMovesAlongPath());
                System.out.println("Path length: " + (int) solution.getG() + "\n"); //długość ścieżki rozwiązania
                System.out.println("Time [ms]: " + algo.getDurationTime());
                System.out.println("Closed: " + algo.getClosedStatesCount());
                System.out.println("Open: " + algo.getOpenSet().size());
                break;
            default:        //możliwość samodzielnego ustawienia elementów układanki (niezabezpieczone)
                SlidingPuzzle puzzle = new SlidingPuzzle(true);

                SlidingPuzzle.setHFunction(new Manhattan());
                GraphSearchAlgorithm algopuz = new AStar(puzzle);
                algopuz.execute();
                GraphState solutionpuz = algopuz.getSolutions().get(0);

                List<GraphState> pathpuz = solutionpuz.getPath();
                Iterator<GraphState> iteratorpuz = pathpuz.iterator();
                while (iteratorpuz.hasNext()) {
                    System.out.println(iteratorpuz.next() + "-----");
                }

                System.out.println(solutionpuz.getMovesAlongPath());
                System.out.println("Path length: " + (int) solutionpuz.getG() + "\n");
                System.out.println("Time [ms]: " + algopuz.getDurationTime());
                System.out.println("Closed: " + algopuz.getClosedStatesCount());
                System.out.println("Open: " + algopuz.getOpenSet().size());
                break;
        }
    }

    private static int mean(int[] array) {
        int sum = 0;
        for(int i=0; i<array.length; i++) {
            sum += array[i];
        }
        return sum / array.length;
    }

    public SlidingPuzzle() {
        board = new byte[n][n];
        int k = 0;
        for(int i=0; i<n; i++)
            for(int j=0; j<n; j++, k++)
                board[i][j] = (byte) k;
        zeroX = 0;
        zeroY = 0;
    }

    public SlidingPuzzle(SlidingPuzzle parent) {        //konstruktor kopiujący
        board = new byte[n][n];
        for(int i=0; i<n; i++)
            for(int j=0; j<n; j++)
                board[i][j] = parent.board[i][j];
        zeroX = parent.zeroX;
        zeroY = parent.zeroY;
    }

    public SlidingPuzzle(boolean b) {    //własna plansza
        Scanner scanner = new Scanner(System.in);
        board = new byte[n][n];
        System.out.println("Podaj własną planszę na wejście: ");
        for(int i=0; i<n; i++)
            for(int j=0; j<n; j++) {
                System.out.print("[" + i + "][" + j + "] = ");
                board[i][j] = scanner.nextByte();
            }
        zeroFinder:
        for(int i=0; i<n; i++)
            for(int j=0; j<n; j++)
                if(board[i][j] == 0) {
                    zeroX = (byte) i;
                    zeroY = (byte) j;
                    break zeroFinder;
                }
    }

    public byte[][] getBoard() {
        return board;
    }

    public String toString() {
        StringBuilder txt = new StringBuilder();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                txt.append(board[i][j] + ",");
            }
            txt.append("\n");
        }
        return txt.toString();
    }

    public int hashCode() {
        byte[] puzzleFlat = new byte[n * n];
        int k = 0;
        for(int i=0; i<n; i++)
            for(int j=0; j<n; j++)
                puzzleFlat[k++] = board[i][j];
        return Arrays.hashCode(puzzleFlat);
    }

    private boolean makeMove(byte direction) {       //kierunki odnoszą się do pustego klocka 0
        switch(direction) {
            case up:
                if(zeroX == 0)
                    return false;   //jeżeli ruch jest nielegalny
                board[zeroX][zeroY] = board[zeroX - 1][zeroY];
                board[zeroX - 1][zeroY] = 0;
                zeroX--;
                break;
            case right:
                if(zeroY == n-1)
                    return false;
                board[zeroX][zeroY] = board[zeroX][zeroY + 1];
                board[zeroX][zeroY + 1] = 0;
                zeroY++;
                break;
            case down:
                if(zeroX == n-1)
                    return false;
                board[zeroX][zeroY] = board[zeroX + 1][zeroY];
                board[zeroX + 1][zeroY] = 0;
                zeroX++;
                break;
            case left:
                if(zeroY == 0)
                    return false;
                board[zeroX][zeroY] = board[zeroX][zeroY - 1];
                board[zeroX][zeroY - 1] = 0;
                zeroY--;
                break;
        }
        return true;
    }

    public void shuffle(int howManyMoves) {
        for(int i=0; i<howManyMoves; i++) {
            int move = random.nextInt(4);   //losowa liczba od 0 do 3 (losowanie ruchów)
            if(!this.makeMove((byte) move))
                i--;
        }
    }

    @Override
    public List<GraphState> generateChildren() {
        List<GraphState> children = new ArrayList();
        if(isSolution())
            return children;
        for(byte i=up; i<=left; i++) {
            SlidingPuzzle child = new SlidingPuzzle(this);
            if(child.makeMove(i)) {
                children.add(child);
                String name = "";
                switch(i) {
                    case up:
                        name = "U";
                        break;
                    case right:
                        name = "R";
                        break;
                    case down:
                        name = "D";
                        break;
                    case left:
                        name = "L";
                        break;
                }
                child.setMoveName(name);
            }
        }
        return children;
    }

    @Override
    public boolean isSolution() {
        byte k = 0;
        for(int i=0; i<n; i++) {
            for (int j=0; j<n; j++, k++) {
                if (board[i][j] != k)
                    return false;
            }
        }
        return true;
    }
}