import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Stack;

import static java.lang.Math.abs;

/**
 * This class represents a generic TicTacToe game board.
 */
public class Board {
    private int n;
    int[][] gameBoard;
    private int freeFields;

    /**
     * Creates Board object, am game board of size n * n with 1<=n<=10.
     */
    public Board(int n) {
        if (n < 1 || n > 10) {
            throw new InputMismatchException();
        }
        this.n = n;
        this.gameBoard = new int[n][n];
        this.freeFields = (int) Math.pow(this.gameBoard.length, 2);
    }

    /**
     * @return length/width of the Board object
     */
    public int getN() {
        return n;
    }

    /**
     * @return number of currently free fields
     */
    public int nFreeFields() {
        return this.freeFields;
    }

    /**
     * @return token at position pos
     */
    public int getField(Position pos) throws InputMismatchException {
        if ((pos.x < n) && (pos.x >= 0) && (pos.y < n) && (pos.y >= 0)) {
            int temp;
            temp = this.gameBoard[pos.x][pos.y];
            return temp;
        } else {
            throw new InputMismatchException();
        }
    }

    /**
     * Sets the specified token at Position pos.
     */
    public void setField(Position pos, int token) throws InputMismatchException {
        if ((token == 1 || token == -1 || token == 0) && (pos.x < n) && (pos.x >= 0) && (pos.y < n) && (pos.y >= 0)) {
            if (gameBoard[pos.x][pos.y] == 0 && token != 0) { // If that position is empty(=0) and will not be set to 0
                this.gameBoard[pos.x][pos.y] = token;
                this.freeFields--;
            } else if ((gameBoard[pos.x][pos.y] == 1 || gameBoard[pos.x][pos.y] == -1) && token == 0) {   //If that position is not empty and will be set to 0
                this.gameBoard[pos.x][pos.y] = token;
                this.freeFields++;
            } else {  // If that position is not empty and will not be set to 0 OR If that position is empty(=0) and will be set to 0
                this.gameBoard[pos.x][pos.y] = token;
            }

        } else {
            throw new InputMismatchException();
        }
    }

    /**
     * Places the token of a player at Position pos.
     */
    public void doMove(Position pos, int player) {
        if (this.gameBoard[pos.x][pos.y] == 0) {
            setField(pos, player);
        } else {
            throw new InputMismatchException();
        }
    }

    /**
     * Clears board at Position pos.
     */
    public void undoMove(Position pos) {
        if (this.gameBoard[pos.x][pos.y] == 1 || this.gameBoard[pos.x][pos.y] == -1) {
            setField(pos, 0);
        } else {
            throw new InputMismatchException();
        }

    }

    /**
     * @return true if game is won, false if not
     */
    public boolean isGameWon() {
        for (int i = 0; i < gameBoard.length; i++) { // Rows
            int sum = 0;
            for (int j = 0; j < gameBoard.length; j++) {
                sum += gameBoard[i][j];
            }
            if (sum == this.n || sum == -this.n) {
                return true;
            }
        }
        for (int i = 0; i < gameBoard.length; i++) { // Columns
            int sum = 0;
            for (int j = 0; j < gameBoard.length; j++) {
                sum += gameBoard[j][i];
            }
            if (sum == this.n || sum == -this.n) {
                return true;
            }
        }
        int sum = 0;
        for (int i = 0; i < gameBoard.length; i++) { // Diagonal 1
            sum += gameBoard[i][i];
        }
        if (sum == this.n || sum == -this.n) {
            return true;
        }
        sum = 0;
        for (int i = 0, j = this.n - 1; i < gameBoard.length && j >= 0; i++, j--) { // Diagonal 2
            sum += gameBoard[i][j];
        }
        if (sum == this.n || sum == -this.n) {
            return true;
        }
        return false;
    }

    /**
     * @return set of all free fields as some Iterable object
     */
    public Iterable<Position> validMoves() {
        Stack<Position> Positions = new Stack<>();
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard.length; j++) {
                if (gameBoard[i][j] == 0) {
                    Position tempPos = new Position(i, j);
                    Positions.add(tempPos);
                }
            }
        }
        return Positions;
    }

    /**
     * Outputs current state representation of the Board object.
     * Practical for debugging.
     */
    public void print() {
        for (int[] ints : gameBoard) {
            for (int j = 0; j < gameBoard.length; j++) {
                System.out.print(ints[j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Board test = new Board(10);
        //System.out.println(test.nFreeFields());
        Position pos1 = new Position(0, 2);
        Position pos2 = new Position(1, 1);
        Position pos3 = new Position(2, 0);
        test.setField(pos1, -1);
        test.setField(pos2, -1);
        test.setField(pos3, -1);
        test.print();
        System.out.println(test.isGameWon());
        //System.out.println(test.getField(pos1));
        //System.out.println(test.nFreeFields());
        //for (Position p : test.validMoves()){
        //System.out.println("(" + p.x + "," + p.y + ")");
        //}
    }

}

