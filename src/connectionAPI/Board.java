package connectionAPI;

import java.util.HashMap;

/**
 * @author Casey Beaird
 * @author Chris Everitt
 *         Created on 3/20/16.
 *         Virginia Commonwealth University
 *         Computer Science Department
 *         Course 612 Game Theory
 */

public interface Board {
    /**
     * interface method for access to game board height
     *
     * @return boardHeight
     */
    int boardHeight();

    /**
     * interface method for access to game board width
     *
     * @return boardWidth
     */
    int boardWidth();

    /**
     * interface method for access to game board depth
     *
     * @return boardDepth
     */
    int boardDepth();

    /**
     * interface method to print game board to stdout
     */
    void display();

    /**
     * interface method to copy the board this method should return a deep copy object
     * ie. Board == Board.copy() = False
     * Board.equals(Board.Copy()) = True
     * Deep being that each attribute within the board should also be new and copied.
     *
     * @return copied board object
     */
    Board copy();

    /**
     * @return a map of Player move objects that encompass all legal moves given the
     * current board configuration.
     */
    HashMap<Integer, PlayerMove> getLegalMoves();
}
