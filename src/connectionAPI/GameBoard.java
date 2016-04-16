package connectionAPI;

import connect4.GamePieces;

/**
 * @author Casey Beaird
 * @author Chris Everitt
 *         Created on 3/20/16.
 *         Virginia Commonwealth University
 *         Computer Science Department
 *         Course 612 Game Theory
 */

public abstract class GameBoard implements Board, WinRules {
    public static final String HEIGHT_COORDINATE = "H";
    public static final String WIDTH_COORDINATE = "W";
    public static final String DEPTH_COORDINATE = "D";
    protected int boardHeight;
    protected int boardWidth;
    protected int boardDepth;
    protected int winningConnectionLength;

    public GameBoard(int boardHeight) {
        this.boardHeight = boardHeight;
    }

    public GameBoard(int boardHeight, int boardWidth) {
        this.boardHeight = boardHeight;
        this.boardWidth = boardWidth;
    }

    public GameBoard(int boardHeight, int boardWidth, int boardDepth) {
        this.boardHeight = boardHeight;
        this.boardWidth = boardWidth;
        this.boardDepth = boardDepth;
    }

    @Override
    public int boardDepth() {
        return boardDepth;
    }

    @Override
    public int boardHeight() {
        return boardHeight;
    }

    @Override
    public int boardWidth() {
        return boardWidth;
    }

    @Override
    public boolean isWon() {
        return this.getWinner() != GamePieces.EMPTY;
    }

    /**
     * @return the connection length required to win
     */
    @Override
    public int getConnectionLength() {
        return this.winningConnectionLength;
    }

    /**
     * number of connected game pieces that are needed to win the game. the connection length must not be larger than
     * any dimension of the game board or there can be no winner in that dimension.
     *
     * @param length number of connected game pieces to win
     */
    @Override
    public void setConnectionLength(int length) {
        if ((length > boardWidth()) && (length > boardHeight()))
            throw new ConnectionGameException(String.format("This board is of shape height %d width %d a " +
                            "connection of length %d is not achievable",
                    boardHeight(), boardWidth(), length));
        this.winningConnectionLength = length;
    }

    public int hashCode() {
        return ((149 * boardHeight()) + (977 * boardWidth()) + (1103 * boardDepth()) + (3067 * getConnectionLength()));
    }
}
