import java.util.LinkedList;

/**
 * ParitialSolution provides at least the functionality which is required
 * for the use in searching for solutions of the game in a search tree.
 * It can store a game situation (Board) and a sequence of mooves.
 */
public class PartialSolution implements Comparable<PartialSolution> {
    public Board currentBoard;
    private LinkedList<Move> moves;


    public PartialSolution(Board board) {
        this.currentBoard = new Board(board);
        moves = new LinkedList<>();
    }

    public PartialSolution(PartialSolution that) {
        this.currentBoard = new Board(that.currentBoard);
        this.moves = new LinkedList<>();
        for (Move m : that.moveSequence()) {
            Move mNew = new Move(m);
            this.moves.add(mNew);
        }
    }

    public void doMove(Move move) {
        this.currentBoard.doMove(move);
        this.moves.add(move);
    }
    /**
     * Return the sequence of moves which resulted in this partial solution.
     *
     * @return The sequence of moves.
     */
    public LinkedList<Move> moveSequence() {
        return moves;
    }
    public int compareTo(PartialSolution that) {
        return this.moves.size() - that.moves.size();
    }

    @Override
    public String toString() {
        String str = "";
        int lastRobot = -1;
        for (Move move : moveSequence()) {
            if (lastRobot == move.iRobot) {
                str += " -> " + move.endPosition;
            } else {
                if (lastRobot != -1) {
                    str += ", ";
                }
                str += "R" + move.iRobot + " -> " + move.endPosition;
            }
            lastRobot = move.iRobot;
        }
        return str;
    }
}

