package connectionAPI;

import connect4.GamePieces;

/**
 * @author Casey Beaird
 * @author Chris Everitt
 *         Created on 3/20/16.
 *         Virginia Commonwealth University
 *         Computer Science Department
 *         Course 612 Game Theory
 *         <p/>
 *         When creating the Enum class that implements this interface
 *         ensure that the value for an EMPTY board space is listed first
 *         and has the moveOnTrun value of -1 since this is not a real player
 */

public interface Player {
    /**
     * get the character representation for the piece
     *
     * @return GamePieces display character
     */
    char visualization();

    /**
     * get the game piece that moves first
     *
     * @return the player that moves first
     */
    GamePieces movesFirst();

    /**
     * get the integer value for the player calculated as
     * current move mod number of players.
     *
     * @return the integer corresponding to the players turn
     */
    int turn();

    /**
     * @param playerStrategy reference to the players strategy
     */
    void setPlayerStrategy(Strategy playerStrategy);

    /**
     * @return this players Strategy
     */
    Strategy getPlayerStrategy();

    /**
     * @param i the turn in the game currently being played mod the number of players
     * @return the player who's turn it is to move
     */
    Player getMover(int i);

    /**
     * @return unique hash id assigned to each player
     */
    int getPlayerHashID();
}
