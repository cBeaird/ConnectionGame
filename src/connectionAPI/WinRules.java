package connectionAPI;

/**
 * @author Casey Beaird
 * @author Chris Everitt
 *         Created on 3/20/16.
 *         Virginia Commonwealth University
 *         Computer Science Department
 *         Course 612 Game Theory
 */

public interface WinRules {
    /**
     * @return true is a player has won the game
     */
    boolean isWon();

    /**
     * @return the player that has won the game or the empty player if no player won
     */
    Player getWinner();

    /**
     * @return true if the game is finished and there is no winner
     */
    boolean isDraw();

    /**
     * @param length the number of connection needed to win the game
     */
    void setConnectionLength(int length);

    /**
     * @return number of connected pieces needed to win the game
     */
    int getConnectionLength();

    /**
     * @param movesPlayed number of moves that have been played
     * @return true if there are no more moves left to play
     */
    boolean isFinished(int movesPlayed);
}
