import java.util.LinkedList;

/**
 * PartialSolution is a class which represents a state of the game
 * from its initial state to its solution. It includes the current
 * state of the board and the move sequence from the initial state
 * to the current state.</br>
 * For the use in the A*-algorithm, the class implements Comparable
 * wrt the cost of the number of moves + heuristic.</br>
 * For the heuristic and game functionality, the respective methods
 * of class {@link Board} are used.
 */
public class PartialSolution implements Comparable<PartialSolution> {
    public Board currentBoard;
    public int cost;
    public int forwardCost;
    public int backwardCost;
    private LinkedList<Move> moves;

    /**
     * Constructor, generates an empty solution based on the provided
     * <em>board</em> with an empty move sequence.
     *
     * @param board initial state of the board
     */
    public PartialSolution(Board board) {
        this.currentBoard = new Board(board);
        moves = new LinkedList<>();
        this.forwardCost = this.currentBoard.manhattan();
        this.cost = this.currentBoard.manhattan();
    }

    /**
     * Copy constructor, generates a deep copy of the input
     *
     * @param that The partial solution that is to be copied
     */
    public PartialSolution(PartialSolution that) {
        this.currentBoard = new Board(that.currentBoard);
        this.cost = that.cost;
        this.forwardCost = that.forwardCost;
        this.backwardCost = that.backwardCost;
        this.moves = new LinkedList<>();
        for (Move m : that.moves) {
            Move newMove = new Move(m.pos, m.dir);
            this.moves.add(newMove);
        }
    }

    /**
     * Performs a move on the board of the partial solution and updates
     * the cost.
     *
     * @param move The move that is to be performed
     */
    public void doMove(Move move) {
        this.currentBoard.doMove(move);
        this.moves.add(move);
        this.forwardCost = this.currentBoard.manhattan();
        this.backwardCost++;
        this.cost = backwardCost + forwardCost;
    }

    /**
     * Tests whether the solution has been reached, i.e. whether
     * current board is in the goal state.
     *
     * @return {@code true}, if the board is in goal state
     */
    public boolean isSolution() {
        return this.currentBoard.isSolved();
    }

    /**
     * Return the sequence of moves which leads from the initial board
     * to the current state.
     *
     * @return move sequence leading to this state of solution
     */
    public Iterable<Move> moveSequence() {
        return this.moves;
    }

    /**
     * Generates all possible moves on the current board, <em>except</em>
     * the move which would undo the previous move (if there is one).
     *
     * @return moves to be considered in the current situation
     */
    public Iterable<Move> validMoves() {
        if (this.moves.isEmpty()) {
            return this.currentBoard.validMoves();
        } else {
            return this.currentBoard.validMoves(this.moves.getLast());
        }
    }

    /**
     * Compares partial solutions based on their cost.
     * (For performance reasons, the costs should be pre-calculated
     * and stored for each partial solution, rather than computed
     * here each time anew.)
     *
     * @param that the other partial solution
     * @return result of cost comparistion between this and that
     */
    public int compareTo(PartialSolution that) {
        return this.cost - that.cost;
    }

    @Override
    public String toString() {
        StringBuilder msg = new StringBuilder("Partial solution with moves: \n");
        for (Move move : moveSequence()) {
            msg.append(move).append(", ");
        }
        return msg.substring(0, msg.length() - 2);
    }

    public static void main(String[] args) {
        String filename = "samples/board-3x3-twosteps.txt";
        Board board = new Board(filename);
        PartialSolution psol = new PartialSolution(board);
        psol.doMove(new Move(new Position(1, 2), 0));
        psol.doMove(new Move(new Position(2, 2), 3));
        AStar15Puzzle.printBoardSequence(board, psol.moveSequence());
        Board board1 = new Board("samples/comparestest1.txt");
        Board board2 = new Board("samples/comparetest2.txt");
        PartialSolution psol1 = new PartialSolution(board1);
        PartialSolution psol2 = new PartialSolution(board2);
        System.out.println(psol1.cost);
        System.out.println(psol2.cost);
        System.out.println(psol1.compareTo(psol2));
    }
}

