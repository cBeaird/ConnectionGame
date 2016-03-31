package connect4;

import connectionAPI.*;

import java.util.HashMap;

/**
 * @author Casey Beaird
 * @author Chris Everitt
 *         Created on 3/20/16.
 *         Virginia Commonwealth University
 *         Computer Science Department
 *         Course 612 Game Theory
 */

public class Connect4Board extends GameBoard {
    /*  game board of game pieces with the following orientation
    * |(2,0)|(2,1)|(2,2)|
    * |(1,0)|(1,1)|(2,2)|
    * |(0,0)|(0,1)|(0,2)|
    */
    private GamePieces[][] playingBoard;

    public Connect4Board(int boardHeight, int boardWidth) {
        super(boardHeight, boardWidth);
        playingBoard = new GamePieces[boardHeight][boardWidth];
        initializeBoard(GamePieces.EMPTY);
    }

    @Override
    public int boardDepth() {
        return 0;
    }

    /**
     * Print the game board to stdout: prints the coordinate plane along with the board
     */
    @Override
    public void display() {
        int heightCoordinatePlane = Integer.toString(boardHeight()).length() + 2;
        int widthCoordinatePlane = Integer.toString(boardWidth()).length() + 1;

        System.out.println();
        for (int i = boardHeight() - 1; i > -1; i--) {
            System.out.print(String.format("%" + (heightCoordinatePlane + 1) + "s", " "));
            for (int l = 0; l < boardWidth() * (widthCoordinatePlane + 3) + 1; l++)
                System.out.print('-');
            System.out.println();
            System.out.print(String.format("%s%d", HEIGHT_CORDINATE, i) + String.format("%" + (heightCoordinatePlane - Integer.toString(i).length() - 1) + "s", " "));
            for (int j = 0; j < boardWidth(); j++) {
                System.out.print(String.format("|%c", getBoardSpace(i, j).visualization()) + String.format("%" + (widthCoordinatePlane + 1) + "s", " "));
            }
            System.out.print(String.format("%" + (widthCoordinatePlane - Integer.toString(boardWidth()).length()) + "s", " ") + "|\n");
        }

        System.out.print(String.format("%" + (heightCoordinatePlane + 1) + "s", " "));
        for (int l = 0; l < boardWidth() * (widthCoordinatePlane + 3) + 1; l++)
            System.out.print('-');
        System.out.println();

        System.out.print(String.format("%" + (heightCoordinatePlane + 1) + "s", " "));
        for (int l = 0; l < boardWidth(); l++) {
            System.out.print(String.format("|%s%d ", WIDTH_CORDINATE, l) + String.format("%" + (widthCoordinatePlane - Integer.toString(l).length()) + "s", " "));
        }
        System.out.print("|\n");
    }

    /**
     * @return deep copy of the board in its current configuration
     */
    @Override
    public Board copy() {
        Connect4Board copiedBoard = new Connect4Board(this.boardHeight(), this.boardWidth());
        copiedBoard.setConnectionLength(this.getConnectionLength());
        for (int i = 0; i < boardHeight(); i++)
            for (int j = 0; j < boardWidth(); j++)
                copiedBoard.setBoardSpace(i, j, this.getBoardSpace(i, j));
        return copiedBoard;
    }

    /**
     * @return a hash map of legal moves from the board state key is the move hash
     */
    @Override
    public HashMap<Integer, PlayerMove> getLegalMoves() {
        HashMap<Integer, PlayerMove> legalMoves = new HashMap<>();
        for (int i = 0; i < boardHeight(); i++)
            for (int j = 0; j < boardWidth(); j++) {
                if ((i == 0) && getBoardSpace(i, j) == GamePieces.EMPTY) {
                    Connect4Move m = new Connect4Move(i, j);
                    legalMoves.put(m.hashCode(), m);
                }

                if ((i > 0) && (getBoardSpace(i - 1, j) != GamePieces.EMPTY) && getBoardSpace(i, j) == GamePieces.EMPTY) {
                    Connect4Move m = new Connect4Move(i, j);
                    legalMoves.put(m.hashCode(), m);
                }
            }

        return legalMoves;
    }

    /**
     * Initialize the board to be an empty board
     */
    private void initializeBoard(GamePieces piece) {
        for (int row = 0; row < boardHeight(); row++)
            for (int col = 0; col < boardWidth(); col++)
                this.setBoardSpace(row, col, piece);
    }

    /**
     * @param column in accordance with the conceptual definition of the board
     * @param row    in accordance with the conceptual definition of the board
     * @return the Enum player GamePieces occupying the space at (col, row)
     */
    public GamePieces getBoardSpace(int row, int column) {
        return playingBoard[row][column];
    }

    /**
     * This is mainly so that you can get a move hash that will represent the space with an occupant
     *
     * @param row    row integer
     * @param column column integer
     * @return the board space as a move object
     */
    public PlayerMove getBoardSpaceAsMove(int row, int column) {
        Connect4Move m = new Connect4Move(row, column);
        m.setOwner(this.playingBoard[row][column]);
        return m;
    }

    /**
     * @param column in accordance with the conceptual definition of the board
     * @param row    in accordance with the conceptual definition of the board
     * @param piece  assign the GamePieces piece to occupy the space at (col, row)
     */
    public void setBoardSpace(int row, int column, GamePieces piece) {
        this.playingBoard[row][column] = piece;
    }

    /**
     * @return returns the winning GamePieces or empty if the game is not won
     */
    @Override
    public Player getWinner() {
        for (int i = 0; i < boardHeight(); i++) {
            for (int j = 0; j < boardWidth(); j++) {
                // Any winning connection cannot start with an empty space
                if (playingBoard[i][j] == GamePieces.EMPTY)
                    continue;

                // Check for a row winner
                if ((j + getConnectionLength() - 1) < boardWidth()) {
                    for (int c = 1; c < getConnectionLength(); c++) {
                        if (playingBoard[i][j] != playingBoard[i][j + c])
                            break;
                        if (c == getConnectionLength() - 1)
                            return playingBoard[i][j];
                    }
                }

                // Check for a column winner
                if ((i + getConnectionLength() - 1) < boardHeight()) {
                    for (int c = 1; c < getConnectionLength(); c++) {
                        if (playingBoard[i][j] != playingBoard[i + c][j])
                            break;
                        if (c == getConnectionLength() - 1)
                            return playingBoard[i][j];
                    }
                }

                // Check for the up right vector winner
                if ((i + getConnectionLength() - 1 < boardHeight()) && (j + getConnectionLength() - 1 < boardWidth())) {
                    for (int ur = 1; ur < getConnectionLength(); ur++) {
                        if (playingBoard[i][j] != playingBoard[i + ur][j + ur])
                            break;
                        if (ur == getConnectionLength() - 1)
                            return playingBoard[i][j];
                    }
                }

                // Check for the up left vector winner
                if ((i + getConnectionLength() - 1 < boardHeight()) && (j - getConnectionLength() - 1) >= 0) {
                    for (int ul = 1; ul < getConnectionLength(); ul++) {
                        if (playingBoard[i][j] != playingBoard[i + ul][j + (-1 * ul)])
                            break;
                        if (ul == getConnectionLength() - 1)
                            return playingBoard[i][j];
                    }
                }
            }
        }

        // No winner as been returned
        return GamePieces.EMPTY;
    }

    /**
     * if there are no more moves to be made and there is no winner true else false
     *
     * @return boolean if game is finish with out a winner
     */
    @Override
    public boolean isDraw() {
        return isFinished(boardHeight() * boardWidth()) && getWinner() == GamePieces.EMPTY;
    }

    /**
     * @param movesPlayed number of moves played to this point
     * @return true if the number of moves played >= total possible moves
     */
    @Override
    public boolean isFinished(int movesPlayed) {
        return ((boardHeight() * boardWidth())) <= movesPlayed;
    }

    public int hashCode() {
        int hash = super.hashCode();
        for (int i = 0; i < boardHeight(); i++)
            for (int j = 0; j < boardWidth(); j++)
                hash += getBoardSpaceAsMove(i, j).hashCode();
        return hash;
    }
}