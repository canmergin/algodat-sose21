import java.util.Iterator;

/**
 * This class implements and evaluates game situations of a TicTacToe game.
 */
public class TicTacToe {

    /**
     * Returns an evaluation for player at the current board state.
     * Arbeitet nach dem Prinzip der Alphabeta-Suche. Works with the principle of Alpha-Beta-Pruning.
     *
     * @param board  current Board object for game situation
     * @param player player who has a turn
     * @return rating of game situation from player's point of view
     **/
    public static int alphaBeta(Board board, int player) {
        return alphaBetaReal(board, player, -Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    public static int alphaBetaReal(Board board, int player, int alpha, int beta) {
        if (!board.isGameWon() && board.validMoves().spliterator().getExactSizeIfKnown() == 0) { //Game is tie
            return 0;
        }
        if (board.isGameWon()) {
            if (player == 1) {
                return -(board.nFreeFields() + 1);
            } else {
                return (-board.nFreeFields() - 1);
            }
        }
        for (Position possibleMove : board.validMoves()) {
            board.doMove(possibleMove, player);
            int rating = -alphaBetaReal(board, -player, -beta, -alpha);
            board.undoMove(possibleMove);
            if (rating > alpha) {
                alpha = rating;
                if (alpha >= beta) {
                    break;
                }
            }
        }
        return alpha;


    }


    /**
     * Vividly prints a rating for each currently possible move out at System.out.
     * (from player's point of view)
     * Uses Alpha-Beta-Pruning to rate the possible moves.
     * formatting: See "Beispiel 1: Bewertung aller Zugmöglichkeiten" (Aufgabenblatt 4).
     *
     * @param board  current Board object for game situation
     * @param player player who has a turn
     **/
    public static void evaluatePossibleMoves(Board board, int player) {
        alphaBeta(board, player);
    }

    public static void main(String[] args) {
        Board board = new Board(3);
        Position pos1 = new Position(0, 2);
        Position pos2 = new Position(1, 1);
        Position pos3 = new Position(2, 0);
        board.setField(pos1, -1);
        board.setField(pos2, -1);
        board.setField(pos3, -1);
        System.out.println(alphaBeta(board, -1));
    }
}

